/*
 * El reloj es una clase que simula el paso del tiempo para poder observar el
 * comportamiento de la planificacion de procesos.
 * Coordina que todos los sub hilos terminen las tareas que deben realizar en
 * la unidad de tiempo antes de avanzar el contador.
 */
package entidades;

import datasource.IDatasource;
import datasource.ProcesosDatasource;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Inaki
 */
public class Reloj implements Runnable {

    /**
     * Atributes *****************************************************
     */
    private String nombre;
    private long intervaloDeTiempo;
    private Long tiempoActual;
    private ProcesosDatasource datos;
    private AtomicBoolean monitorPL;
    private AtomicBoolean monitorPC;
    private AtomicBoolean monitorCPUs;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */

    /**
     * Constructor que setea la fuente de datos.
     *
     * @param pnombre
     * @param pintervalo
     * @param pFuenteDeDatosProcesos
     */
    public Reloj(String pnombre, int pintervalo, ProcesosDatasource pDatos) {
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
        //ACA VA LO QUE HACE EL RELOJ

        //Creamos los CPU. Esto se podria parametrizar y que se creen N CPUs
        ICpu ru_cpu1 = new Cpu("CPU-1", this.monitorCPUs);
        ICpu ru_cpu2 = new Cpu("CPU-2", this.monitorCPUs);
        Thread th_cpu1 = new Thread(ru_cpu1);
        Thread th_cpu2 = new Thread(ru_cpu2);
        
        //Meto los cpu en un array para pasarlos al Planificador Corto que los
        //va a gestionar. Al parametrizar la cantidad de CPUs hay que arreglar esto.
        ICpu[] CPUs = new ICpu[2];
        CPUs[0] = ru_cpu1;
        CPUs[1] = ru_cpu2;
        
        //Creamos hilo del planificador a corto plazo que es el objeto encargado
        //de planificar el uso de CPU de forma eficiente.
        IPlanificadorCorto ru_planificadorCorto = new PlanificadorCorto(this.monitorPC, CPUs);
        Thread th_planificadorCorto = new Thread(ru_planificadorCorto);

        //Creamos hilos de planificador largo que son los objetos que admiten los
        //procesos desde la fuente de datos.
        Runnable ru_planificadorLargo = new PlanificadorLargo(this.monitorPL, this, datos, ru_planificadorCorto);
        Thread th_planificadorLargo = new Thread(ru_planificadorLargo);



        try {
            // Arranco todos los hilos. Su primera instruccion es parar a la espera
            // de una notificacion en su correspondiente monitor (mutexPL, mutexPC, mutexCPUs).
            th_planificadorLargo.start();
            th_planificadorCorto.start();
            th_cpu1.start();
            th_cpu2.start();

            // Para test hacemos solo 3 ciclos
            while (this.getTiempoActual() < 3000) {
                System.out.println("0 - TIEMPO SIMULADO: " + this.getTiempoActual() + " - thread: " + Thread.currentThread().getName());
                // Arranca a trabajar el planificador a largo plazo
                synchronized (monitorPL) {
                    monitorPL.set(true);
                    monitorPL.notify();
                    while (monitorPL.get()) {
                        // el hilo reloj espera
                        monitorPL.wait();
                    }
                }
                
                // Terminado el planificadorLargo comienza el planificadorCorto
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
                
                //Ultima orden antes de comenzar otro ciclo. Aumentar el tiempo.
                this.tiempoActual = this.getTiempoActual() + this.getIntervaloDeTiempo();
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            System.out.println(" :( Algo salio mal en el reloj...");
            Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
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
        return tiempoActual;
    }

    /**
     * @return the fuenteDeDatosProcesos
     */
    
    // End Getters and Setters ****************************
}
