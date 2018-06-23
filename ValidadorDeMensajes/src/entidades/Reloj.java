/*
 * El reloj es una clase que simula el paso del tiempo para poder observar el
 * comportamiento de la planificacion de procesos.
 * Coordina que todos los sub hilos terminen las tareas que deben realizar en
 * la unidad de tiempo antes de avanzar el contador.
 */
package entidades;

import datasource.IDatasource;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author
 */
public class Reloj implements Runnable {

    /**
     * Atributes *****************************************************
     */
    private String nombre;
    private long intervaloDeTiempo;
    private Long tiempoActual;
    private IDatasource datos;
    private final AtomicBoolean monitorPL;
    private final AtomicBoolean monitorPC;
    private final AtomicBoolean monitorCPUs;
    private final Integer CANTIDAD_CPUS = 2;
    private final Integer DELAY = 600;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
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
        this.datos = pDatos;
        this.monitorPL = new AtomicBoolean();
        this.monitorPC = new AtomicBoolean();
        this.monitorCPUs = new AtomicBoolean();
    }
    // End Constructors ***********************************

    /**
     * Methods ****************************************************
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
        IPlanificadorLargo ru_planificadorLargo = new PlanificadorLargo(this.monitorPL, this, datos, ru_planificadorCorto);
        Thread th_planificadorLargo = new Thread(ru_planificadorLargo);

        try {
                    //Creamos los CPU. Esto se podria parametrizar y que se creen N CPUs
            for ( int i = 0 ; i < this.CANTIDAD_CPUS ; i++) {
               CPUs[i] = new Cpu("CPU-"+ i, this.monitorCPUs, this, ru_planificadorCorto);;
               Thread th_cpu = new Thread(CPUs[i]);
               th_cpu.start();
            }

            // Arranco todos los hilos. Su primera instruccion es parar a la espera
            // de una notificacion en su correspondiente monitor (mutexPL, mutexPC, mutexCPUs).
            th_planificadorLargo.start();
            th_planificadorCorto.start();

            // Para test hacemos solo 30 ciclos
            while (this.getTiempoActual() < 30000) {
                System.out.println("0 - TIEMPO SIMULADO: " + this.getTiempoActual() + " - thread: " + Thread.currentThread().getName());
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
                synchronized (monitorCPUs) {
                    monitorCPUs.set(true);
                    monitorCPUs.notifyAll();
                    while (monitorCPUs.get()) {
                        // El hilo reloj espera nuevamente
                        monitorCPUs.wait();
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
        }
        System.out.println("----- TERMINO EL RELOJ -----");
    }
    // End Methods ****************************************

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
    // End Getters and Setters ****************************
}
