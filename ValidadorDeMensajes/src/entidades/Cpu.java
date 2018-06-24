package entidades;

import datasource.NuestroLogger;

/**
 *
 * @author
 */
public class Cpu implements ICpu {

    /**
     * Atributes *****************************************************
     */
    private final String nombre;
    private final Reloj reloj;
    private IProceso procesoCorriendo;
    private final MonitorCPU monitorCPU;
    private final IPlanificadorCorto planificadorCorto;
    private final Integer DELAY = 100;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */
    /**
     *
     * @param pnombre
     * @param monitorCPU
     * @param preloj
     * @param pPlanificadorCorto
     */
    public Cpu(String pnombre, MonitorCPU monitorCPU, Reloj preloj, IPlanificadorCorto pPlanificadorCorto) {
        this.nombre = pnombre;
        this.monitorCPU = monitorCPU;
        this.reloj = preloj;
        this.planificadorCorto = pPlanificadorCorto;
        this.procesoCorriendo = null;
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
                synchronized (monitorCPU) {
                    while (!monitorCPU.getContinuar()) {
                        monitorCPU.wait();
                    }
                }

                System.out.println("                            3 - Ejecutando ciclo de " + this.getNombre());
                NuestroLogger.logConsola(this.reloj.getTiempoActual() + " [" + this.getNombre() + "] Comienzo ciclo de " + this.getNombre());
                Thread.sleep(DELAY);
                if (this.getProcesoCorriendo() != null) {
                    System.out.println("                            3 - Proceso corriendo en " + this.getNombre() +  ":"  + this.getProcesoCorriendo().getNombre() + " - " + this.getProcesoCorriendo().getTipo());
                    NuestroLogger.logConsola(this.reloj.getTiempoActual() + " [" + this.getNombre() + "] Proceso corriendo en " + this.getNombre() +  ":"  + this.getProcesoCorriendo().getNombre() + " - " + this.getProcesoCorriendo().getTipo());
                    Thread.sleep(DELAY);
                    this.trabajar();

                    // ACA IRIA HACER QUE EL PROCESO PUBLIQUE O LO QUE SEA...    ??????
                } else {
                    System.out.println("                            3 - " + this.getNombre() + " vacio");
                NuestroLogger.logConsola(this.reloj.getTiempoActual() + " [" + this.getNombre() + "] CPU vacio");
                    Thread.sleep(DELAY);
                }
                System.out.println("                            3 - Fin ciclo de " + this.getNombre());
                NuestroLogger.logConsola(this.reloj.getTiempoActual() + " [" + this.getNombre() + "] Fin ciclo de " + this.getNombre());
                
                synchronized (monitorCPU){
                    monitorCPU.setContinuar(Boolean.FALSE);
                }                
                reloj.getMonitoresCPUs().continuar();
            }
        } catch (Exception e) {
            System.out.println("Algo salio mal en CPU + " + this.getNombre() + ": " + e.toString());
        }
    }

    /**
     * Realiza trabajo sobre el proceso cargado
     */
    private void trabajar() {
        Integer comportamientoProceso = this.procesoCorriendo.getComportamiento().getFirst();
        if (comportamientoProceso > 1) {                                                   // Disminuye en uno el numero del primero.
            this.procesoCorriendo.getComportamiento().set(0, comportamientoProceso - 1);
         
            this.procesoCorriendo.sumarUnoCiclosEjecutando();
                if ( procesoCorriendo.getCantCiclosEjecutando() % this.planificadorCorto.getTAMANIO_QUANTUM() != 0){
                int prioridadLlego = this.procesoCorriendo.getPrioridad();
                int prioridadNueva = this.planificadorCorto.formulaRecalcularPrioridad(procesoCorriendo);

                // IÑAKI TIENE QUE GUARDAR INFORMACION PARA SU QUERIDO LOG
                if ( prioridadLlego != prioridadNueva ) {
                    procesoCorriendo.setPrioridad(prioridadNueva);
                    procesoCorriendo.setCantCiclosEjecutando(0);
                    this.planificadorCorto.ingresarProceso(procesoCorriendo);
                    this.procesoCorriendo = null;
                }
            } else {
                System.out.println("                            3 - Se le termino el quantum al proceso " + procesoCorriendo.getNombre());
                this.planificadorCorto.ingresarProcesoRetirado(procesoCorriendo);
                this.procesoCorriendo = null;
            }
        } else {
            this.procesoCorriendo.getComportamiento().removeFirst();      // Elimina el primero numero y cambia al estado de E/S.
            if ( this.procesoCorriendo.getComportamiento().size() > 0 ) { 
                this.procesoCorriendo.aumentarDosPrioridades();
                this.procesoCorriendo.getComportamiento().set(0, this.procesoCorriendo.getComportamiento().getFirst() + 1);  // Se le suma uno, ya que la lista de bloqueados se ejecuta 1 vez por adelantado.
                this.planificadorCorto.ingresarProcesoListaBloqueados(procesoCorriendo);      //Lo pasamos a la lista bloqueado del PC.
                this.procesoCorriendo = null;
            } else {
            this.procesoCorriendo.logEstadisticas();
            this.procesoCorriendo = null;
            }
        }
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public boolean hayProceso() {
        return this.procesoCorriendo != null;
    }
    // End Methods ****************************************
    
    /**
     * Getters and Setters ****************************************************
     */
    /**
     * @return the nombre
     */
    @Override
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the procesoCorriendo
     */
    @Override
    public IProceso getProcesoCorriendo() {
        return procesoCorriendo;
    }

    /**
     * @param procesoCorriendo the procesoCorriendo to set
     */
    @Override
    public void setProcesoCorriendo(IProceso procesoCorriendo) {
        this.procesoCorriendo = procesoCorriendo;
    }

    @Override
    public void borrarProcesoCorriendo() {
        this.procesoCorriendo = null;
    }

    // End Getters and Setters ****************************   
}
