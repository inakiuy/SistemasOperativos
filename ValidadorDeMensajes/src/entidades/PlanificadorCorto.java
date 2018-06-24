package entidades;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author
 */
public class PlanificadorCorto implements IPlanificadorCorto {

    /**
     * Atributes *****************************************************
     */
    private final AtomicBoolean monitorPC;
    private final Reloj reloj;
    private final LinkedList<IProceso>[] pilaListas;
    private final LinkedList<IProceso> listaBloqueados;
    private final ICpu[] cpus;
    private Boolean cargarCPUs = true;
    private final int TAMANIO_QUANTUM = 4;
    private Integer CANT_PROC_RESTANTES = 500;
    private final Integer CANTIDAD_DE_COLAS = 5;
    private final Integer DELAY = 600;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */
    /**
     * Constructor basico
     *
     * @param pmonitorPC
     * @param pcpus
     */
    public PlanificadorCorto(AtomicBoolean pmonitorPC, Reloj preloj, ICpu[] pcpus) {
        this.monitorPC = pmonitorPC;
        this.reloj = preloj;
        this.cpus = pcpus;
        this.pilaListas = new LinkedList[CANTIDAD_DE_COLAS + 1];    // Array de 5, se usaran solo 4, del 1 al 5.
        this.listaBloqueados = new LinkedList();
        for (int i = 1; i < this.pilaListas.length; i++) {
            pilaListas[i] = new LinkedList<>();
        }
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
                synchronized (monitorPC) {
                    while (!monitorPC.get()) {
                        monitorPC.wait();
                    }
                }
                    if (this.cargarCPUs) {
                        //PRIMERO
                        System.out.println("                          2 - Ejecutando planificador CORTO...");
                        Thread.sleep(DELAY);
                        
                        this.actualizarComportamientoProcesoBloqueado();
                        this.asignarProcesosCpusVacios();
                        
                        System.out.println("                          2 - Fin planificador CORTO.");
                    } else {
                        System.out.println("                          2 - Ejecutando planificador CORTO...");
                        Thread.sleep(DELAY);
                        this.planificar();
                        System.out.println("                          2 - Fin planificador CORTO.");
                    }                    
                    synchronized (monitorPC) {
                        monitorPC.set(false);
                        monitorPC.notify();
                    }
                    this.cargarCPUs = !this.cargarCPUs;                    
            }
        }catch (Exception e) {
            System.out.println("Algo salio mal en PC: " + e.toString());
        }
    }

    /**
     *
     */
    private void planificar() {
        System.out.println("                          2 - Estoy planificando.");
        //ULTIMO        
               
        this.reasignarPrioridadesPila();
        

    }

    /**
     * 
     */
    private void reasignarPrioridadesPila(){
        synchronized (pilaListas){
            for (int i = 1; i < this.pilaListas.length; i++) {
                if (!this.pilaListas[i].isEmpty()) {
                        Iterator<IProceso> iter = this.pilaListas[i].iterator();
                        while(iter.hasNext()) {
                            IProceso proceso = (IProceso) iter.next();
                        
                            proceso.sumarUnoCiclosEsperando();
                            
                            int prioridadLlego = proceso.getPrioridad();
                            int prioridadNueva = this.formulaRecalcularPrioridad(proceso);

                            if ( prioridadLlego != prioridadNueva ) {
                                proceso.setPrioridad(prioridadNueva);
                                //proceso.setCantCiclosEjecutando(0);  // Tal vez no necesita.
                                proceso.setCantCiclosEsperando(0);
                                this.ingresarProceso(proceso);
                                iter.remove();
                        }
                    }
                }
            }
        }   
    }
   
    /**
     * 
     */
    private void actualizarComportamientoProcesoBloqueado() {
        if ( this.listaBloqueados.size() > 0 ) {
            synchronized (listaBloqueados){
                Iterator<IProceso> iter = listaBloqueados.iterator();
                while(iter.hasNext()) {
                    IProceso procesoSeleccionado = iter.next();
                    if ( procesoSeleccionado.getComportamiento().getFirst() == 1 ) {                //Si fue su ultima espera, lo pasa a la Cola nuevamente.
                        procesoSeleccionado.getComportamiento().removeFirst();                      //  Remuevo el primer valor que es un 1, en su ultimo ciclo.
                        if (procesoSeleccionado.getComportamiento().size() != 0) {                      // Si aun tiene ciclos por hacer...
                            procesoSeleccionado.getVectorDeInformacion()[1] += procesoSeleccionado.getCantCiclosEjecutando();
                            procesoSeleccionado.setCantCiclosEjecutando(0);
                            this.ingresarProceso(procesoSeleccionado);         // Lo agrega a la cola.
                            iter.remove();
                        } else {
                            procesoSeleccionado.logEstadisticas();
                            iter.remove(); 
                        }
                    } else {
                        Integer nuevoValor = procesoSeleccionado.getComportamiento().getFirst() - 1;
                        procesoSeleccionado.getComportamiento().set(0, nuevoValor);
                    }
                }
            }

        }
    }
    
    /**
     *
     */
    private void asignarProcesosCpusVacios() {
        System.out.println("                          2 - Estoy asignando procesos a CPU`s vacios.");
        try {
            Thread.sleep(DELAY);
            
            for (ICpu cpu : this.cpus) {
            // Chequear Cpu por Cpu cual esta vacio para pasarle un proceso.
            if (!cpu.hayProceso()) {
                for (int i = 1; i < this.pilaListas.length; i++) {
                    // Buscar el primer proceso con mayor prioridad.
                    if (!this.pilaListas[i].isEmpty()) {
                        IProceso proceso = this.pilaListas[i].removeFirst();

                        if ( proceso.getVectorDeInformacion()[0] < 0) {
                             proceso.getVectorDeInformacion()[0] = proceso.getCantCiclosEsperando();
                        }
                        proceso.setCantCiclosEsperando(0);
                        
                        cpu.setProcesoCorriendo(proceso);
                        break;
                    }
                }
            }
        }
            
        }catch (Exception e) {
            System.out.println("Algo salio mal en PC: " + e.toString());
        }
    }

    /**
     *
     * @param pproceso
     * @param lista
     */
    @Override
    public void ingresarProceso(IProceso pproceso) {
        this.restarUnoProcesosRestantes();
          synchronized (pilaListas){
              this.pilaListas[pproceso.getPrioridad()].addLast(pproceso);
          }   
    }

    /**
     *
     * @param proceso
     */
    @Override
    public synchronized void ingresarProcesoListaBloqueados(IProceso proceso) {
        this.sumarUnoProcesosRestantes();
        synchronized (listaBloqueados){
            if (this.listaBloqueados.size() > 0) {
                Iterator<IProceso> iter = listaBloqueados.iterator();
                for (int i = 0; i < listaBloqueados.size(); i++) {
                    if (iter.hasNext()) {                              //Iterador de java
                        Proceso procesoSeleccionado = (Proceso) iter.next();
                        if (i == (listaBloqueados.size() - 1)) {
                            listaBloqueados.addLast(proceso);               //Agrega al final.
                            break;
                        } else if ( procesoSeleccionado.getComportamiento().getFirst() > proceso.getComportamiento().getFirst()) {
                            if (i == 0) {
                                listaBloqueados.addFirst(proceso);              //Agrega al principio.
                                break;
                            } else {
                                listaBloqueados.add(i - 1, proceso);            //Agrega al anteriro, ya que se paso por uno.
                                break;
                            }
                        }
                    }
                }
            } else {
                listaBloqueados.addLast(proceso);
            }            
        }
    }
  
    /**
     * 
     * @param pproceso
     * @return 
     */
    @Override
    public int formulaRecalcularPrioridad(IProceso pproceso){
        //  5 quantums = 20 ciclos        
        int prioridadBase = pproceso.getPrioridad();
        int envejecimiento = (pproceso.getCantCiclosEsperando() / this.TAMANIO_QUANTUM) / 5;
        int feedback = (pproceso.getCantCiclosEjecutando() / this.TAMANIO_QUANTUM) / 2;

        int prioridadNueva = prioridadBase - envejecimiento + feedback;
        
        if ( prioridadNueva <= 0 ){
            prioridadNueva = 1;
        }
        else if (prioridadNueva >= this.CANTIDAD_DE_COLAS){
            prioridadNueva = this.CANTIDAD_DE_COLAS;
        }
        return prioridadNueva;
    }

    /**
     * 
     */
    @Override
    public void restarUnoProcesosRestantes(){
        this.CANT_PROC_RESTANTES -= 1;
    }
 
    /**
     * 
     */
    @Override
    public void sumarUnoProcesosRestantes(){
        this.CANT_PROC_RESTANTES += 1;
    }
    // End Methods ****************************************

    /**
     * Getters and Setters ****************************************************
     */
    /**
     *
     * @return
     */
    @Override
    public int getCantProcesosRestantes() {
        return CANT_PROC_RESTANTES;
    }

    /**
     *
     * @param cantProcesosRestantes
     */
    @Override
    public void setCantProcesosRestantes(int cantProcesosRestantes) {
        this.CANT_PROC_RESTANTES -= cantProcesosRestantes;
    }

    /**
     *
     * @return
     */
    @Override
    public AtomicBoolean getMonitorPC() {
        return monitorPC;
    }

    /**
     *
     * @return
     */
    @Override
    public LinkedList[] getPilaListas() {
        return pilaListas;
    }

    /**
     *
     * @return
     */
    @Override
    public LinkedList<IProceso> getListaBloqueados() {
        return listaBloqueados;
    }

    @Override
    public int getTAMANIO_QUANTUM() {
        return this.TAMANIO_QUANTUM;
    }
    // End Getters and Setters ****************************************
}