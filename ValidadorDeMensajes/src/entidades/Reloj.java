/*
 * El reloj es una clase que simula el paso del tiempo para poder observar el
 * comportamiento de la planificacion de procesos.
 * Coordina que todos los sub hilos terminen las tareas que deben realizar en
 * la unidad de tiempo antes de avanzar el contador.
 */
package entidades;

import datasource.IDatasource;
import datasource.NuestroLogger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author
 */
public class Reloj implements Runnable {

    /**
     * Atributes **************************************************************
     */
    private final String nombre;
    private final long intervaloDeTiempo;
    private Long tiempoActual;
    private IDatasource fuenteDeDatos;
    private final AtomicBoolean monitorPL;
    private final AtomicBoolean monitorPC;
    private final MonitoresCPUs monitoresCPUs;
    private final Integer CANTIDAD_CPUS = 4;
    private final Integer CANTIDAD_PL = 1;
    private final Integer CANTIDAD_PC = 1;
    private final Integer DELAY = 1;
    // End Atributes **********************************************************

    /**
     * Constructors ***********************************************************
     */

    /**
     * Constructor que setea la fuente de datos.
     *
     * @param pnombre
     * @param pintervalo
     * @param pDatos
     */
    public Reloj(String pnombre, int pintervalo, IDatasource pDatos) {
        this.nombre = pnombre;
        this.intervaloDeTiempo = pintervalo;
        this.tiempoActual = new Long(0);
        this.fuenteDeDatos = pDatos;
        this.monitorPL = new AtomicBoolean();
        this.monitorPC = new AtomicBoolean();
        this.monitoresCPUs = new MonitoresCPUs(CANTIDAD_CPUS);
    }
    // End Constructors *******************************************************

    /**
     * Methods ****************************************************************
     */
    /**
     * METODO PRINCIPAL QUE SE EJECUTA CUANDO UN HILO CORRE
     */
    @Override
    public void run() {
        
        //Meto los cpu en un array para pasarlos al Planificador Corto que los
        //va a gestionar. Al parametrizar la cantidad de CPUs hay que arreglar esto.
        ICpu[] CPUs = new ICpu[CANTIDAD_CPUS];

        //Creamos hilo del planificador a corto plazo que es el objeto encargado
        //de planificar el uso de CPU de forma eficiente.
        IPlanificadorCorto ru_planificadorCorto = new PlanificadorCorto(this.monitorPC, this, CPUs);
        Thread th_planificadorCorto = new Thread(ru_planificadorCorto);

        //Creamos hilos de planificador largo que son los objetos que admiten los
        //procesos desde la fuente de datos.
        IPlanificadorLargo ru_planificadorLargo = new PlanificadorLargo(this.monitorPL, this, fuenteDeDatos, ru_planificadorCorto);
        Thread th_planificadorLargo = new Thread(ru_planificadorLargo);

        try {
                    //Creamos los CPU y junto todos los monitores en una coleccion
            for ( int i = 0 ; i < this.CANTIDAD_CPUS ; i++) {
                MonitorCPU monitor = new MonitorCPU();
                monitoresCPUs.getMonitoresCPUs()[i] = monitor;
                CPUs[i] = new Cpu("CPU-"+ i, monitor, this, ru_planificadorCorto);;
                Thread th_cpu = new Thread(CPUs[i]);
                th_cpu.start();
            }

            // Arranco todos los hilos. Su primera instruccion es parar a la espera
            // de una notificacion en su correspondiente monitor (mutexPL, mutexPC, mutexCPUs).
            th_planificadorLargo.start();
            th_planificadorCorto.start();

            // Para test hacemos solo 30 ciclos
            while (this.getTiempoActual() < 30000) {
                System.out.println("                      0 - TIEMPO SIMULADO: " + this.getTiempoActual() + " - thread: " + Thread.currentThread().getName());
                NuestroLogger.logConsola(this.getTiempoActual() + " [RELOJ] Tiempo simulado: " + this.getTiempoActual());
                Thread.sleep(DELAY);
                // Arranca a trabajar el planificador a largo plazo
                synchronized (monitorPL) {
                    monitorPL.set(true);
                    monitorPL.notify();
                    while (monitorPL.get()) {
                        // el hilo reloj espera
                        monitorPL.wait();
                    }
                }
                
                // Terminado el planificadorLargo comienza el planificadorCorto         //Aca asgina procesos a las CPu vacias.
                synchronized (monitorPC) {
                    monitorPC.set(true);
                    monitorPC.notify();
                    while (monitorPC.get()) {
                        // El hilo reloj espera nuevamente
                        monitorPC.wait();
                    }
                }

                // Terminada la planificacion, comienza el procesamiento
                synchronized (monitoresCPUs) {
                    monitoresCPUs.getMonitorReloj().set(true);
                    monitoresCPUs.notificarATodos();
                }
                synchronized (monitoresCPUs.getMonitorReloj()){
                    while (monitoresCPUs.getMonitorReloj().get()) {
                        // El hilo reloj espera nuevamente
                        monitoresCPUs.getMonitorReloj().wait();
                    }
                }
                
                 // Terminado el procesamiento comienza el planificadorCorto NUEVAMENTE         // Aca hace todo el trabajo restante.
                synchronized (monitorPC) {
                    monitorPC.set(true);
                    monitorPC.notify();
                    while (monitorPC.get()) {
                        // El hilo reloj espera nuevamente
                        monitorPC.wait();
                    }
                }
                
                //Ultima orden antes de comenzar otro ciclo. Aumentar el tiempo.
                this.tiempoActual = this.getTiempoActual() + this.getIntervaloDeTiempo();
            }
        } catch (InterruptedException ex) {
            System.out.println(" :( Algo salio mal en el reloj..." + ex.getMessage());
            NuestroLogger.logConsola(this.getTiempoActual() + "[RELOJ] Algo salio mal en el reloj..." + ex.getMessage());
        }
        System.out.println("----- TERMINO EL RELOJ -----");
        NuestroLogger.logConsola(this.getTiempoActual() + " [RELOJ] Termino el reloj");
    }
    // End Methods ************************************************************

    /**
     * Getters and Setters ****************************************************
     */
    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the intervaloDeTiempo
     */
    public long getIntervaloDeTiempo() {
        return intervaloDeTiempo;
    }

    /**
     * @return the tiempoActual
     */
    public long getTiempoActual() {
        return tiempoActual;       //Ya que hay que pasarlo a un entero por la entrada de datos.
    }

    public MonitoresCPUs getMonitoresCPUs(){
        return this.monitoresCPUs;
    }
    // End Getters and Setters ************************************************
}
