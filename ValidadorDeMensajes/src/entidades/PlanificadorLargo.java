package entidades;

import datasource.IDatasource;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author
 */
public class PlanificadorLargo implements IPlanificadorLargo {

    /**
     * Atributes *****************************************************
     */
    private final AtomicBoolean monitorPL;
    private Reloj reloj;
    private IDatasource fuenteDeDatosProcesos;
    private IPlanificadorCorto planificadorCorto;
    private LinkedList<IProceso> listaProcesosPL;
    private final Integer DELAY = 600;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */
    /**
     * Constructor con objeto sincronizador
     *
     * @param pmonitorPL
     * @param preloj
     * @param pFuenteDeDatosProcesos
     * @param pplanificadorCorto
     */
    public PlanificadorLargo(AtomicBoolean pmonitorPL, Reloj preloj, IDatasource pFuenteDeDatosProcesos, IPlanificadorCorto pplanificadorCorto) {
        this.monitorPL = pmonitorPL;
        this.reloj = preloj; 
        this.fuenteDeDatosProcesos = pFuenteDeDatosProcesos;
        this.planificadorCorto = pplanificadorCorto;
        this.listaProcesosPL = new LinkedList<>();   
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
        try {
            while (true) {
                synchronized (monitorPL) {
                    while (!monitorPL.get()) {
                        monitorPL.wait();
                    }
                }
                System.out.println("                        1 - Ejecutando planificador LARGO...");
                Thread.sleep(DELAY);
                planificar();
                System.out.println("                        1 - Fin planificador LARGO");
  
                synchronized (monitorPL) {
                    monitorPL.set(false);
                    monitorPL.notify();
                }
            }
        } catch (Exception e) {
            System.out.println("Algo salio mal en PL: " + e.toString());
        }
    }
    
    /**
     * Toma los procesos que llegan en el tiempo actual y planifica si puede entrar
     * el proceso al planificador corto.
     * Ojo que este metodo tampoco es seguro para multihilos
     */
    private void planificar(){    
        this.obtenerNuevosProcesos();      
        while ( ! this.listaProcesosPL.isEmpty() && this.planificadorCorto.getCantProcesosRestantes() > 0 ) {
            IProceso proceso = this.listaProcesosPL.removeFirst();
            proceso.setPrioridad(3);
            this.planificadorCorto.ingresarProceso(proceso);        // Proceso X en la lista 3    (Van del 1 al 5)
        }      
    }
    
    /**
     * Obtiene todos los procesos que llegaron en el tiempo actual y los inserta en una
     * lista para trabajarlos.
     */
    private void obtenerNuevosProcesos(){        
        boolean hayProcesos = true; //ojo que esto no es seguro si llegamos a crear varios hilos de planificador largo
        while ( hayProcesos ) {
            IProceso proceso = this.fuenteDeDatosProcesos.getPrimerProcYEliminar( this.reloj.getTiempoActual() / this.reloj.getIntervaloDeTiempo() );
            if ( proceso != null ) {
                this.listaProcesosPL.addLast(proceso);
            }
            else {
                hayProcesos = false; 
            }
        }
    }
    // End Methods ****************************************
}
