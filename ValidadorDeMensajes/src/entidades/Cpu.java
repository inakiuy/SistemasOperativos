package entidades;

import java.util.concurrent.atomic.AtomicBoolean;

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
    private final AtomicBoolean monitorCPUs;
    private final IPlanificadorCorto planificadorCorto;
    private final Integer DELAY = 600;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */
    /**
     *
     * @param pnombre
     * @param monitorCPUs
     * @param pPlanificadorCorto
     */
    public Cpu(String pnombre, AtomicBoolean monitorCPUs, Reloj preloj, IPlanificadorCorto pPlanificadorCorto) {
        this.nombre = pnombre;
        this.monitorCPUs = monitorCPUs;
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
                synchronized (monitorCPUs) {
                    while (!monitorCPUs.get()) {
                        monitorCPUs.wait();
                    }
                }

                System.out.println("                            3 - Ejecutando ciclo de " + this.getNombre());
                Thread.sleep(DELAY);
                if (this.getProcesoCorriendo() != null) {
                    System.out.println("                            3 - Proceso corriendo en " + this.getNombre() +  ":"  + this.getProcesoCorriendo().getNombre() + " - " + this.getProcesoCorriendo().getTipo());
                    Thread.sleep(DELAY);
                    this.trabajar();

                    // ACA IRIA HACER QUE EL PROCESO PUBLIQUE O LO QUE SEA...    ??????
                } else {
                    System.out.println("                            3 - " + this.getNombre() + " vacio");
                    Thread.sleep(DELAY);
                }
                System.out.println("                            3 - Fin ciclo de " + this.getNombre());

                synchronized (monitorCPUs) {
                    monitorCPUs.set(false);
                    monitorCPUs.notify();
                }
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
        } else {
            this.procesoCorriendo.getComportamiento().removeFirst();      // Elimina el primero numero y cambia al estado de E/S.
            if ( this.procesoCorriendo.getComportamiento().size() > 0 ) { 
                this.procesoCorriendo.sumarDosPrioridad();
                this.planificadorCorto.ingresarProcesoListaBloqueados(procesoCorriendo);      //Lo pasamos a la lista bloqueado del PC.
                this.procesoCorriendo = null;
            } else {
            this.procesoCorriendo.logEstadisticas(this.reloj.getTiempoActual());
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
