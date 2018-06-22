/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Inaki
 */
public class PlanificadorCorto implements IPlanificadorCorto {

    /**
     * Atributes *****************************************************
     */
    private Integer cantProcesosRestantes;
    private final AtomicBoolean monitorPC;
    private LinkedList<IProceso>[] pilaListas;
    private LinkedList<IProceso> listaBloqueados;
    private final int tamanioQuantum;
    private final ICpu[] cpus;
    private final Integer CANTIDAD_DE_COLAS = 5;
    private Boolean cargarCPUs = true;
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
    public PlanificadorCorto(AtomicBoolean pmonitorPC, ICpu[] pcpus) {
        this.monitorPC = pmonitorPC;
        this.tamanioQuantum = 4;
        this.cpus = pcpus;
        this.cantProcesosRestantes = 500;
        this.pilaListas = new LinkedList[CANTIDAD_DE_COLAS];    // Array de 5, se usaran solo 4, del 1 al 5.
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
                try {
                    if (this.cargarCPUs) {
                        //PRIMERO
                        System.out.println("    2 - Ejecutando planificador CORTO...");
                        this.asignarProcesosCpusVacios();
                        System.out.println("    2 - Fin planificador CORTO.");
                    } else {
                        System.out.println("    4 - Ejecutando planificador CORTO...");
                        this.planificar();
                        System.out.println("    4 - Fin planificador CORTO.");
                    }
                    Thread.sleep(200);
                    synchronized (monitorPC) {
                        monitorPC.set(false);
                        monitorPC.notify();
                    }
                } catch (Exception e) {
                    System.out.println("Algo salio mal en PC: " + e.toString());
                }finally {
                    this.cargarCPUs = !this.cargarCPUs;
                }
            }
        }catch (Exception e) {
            System.out.println("Algo salio mal en PC: " + e.toString());
        }
    }

    /**
     *
     */
    private void planificar() {
        System.out.println("    4 - Estoy planificando.");
        //ULTIMO
        
        this.actualizarComportamientoProcesoBloqueado();
    }

    // FALTA HACER CHEQUEOS EN TODOS LOS QUE SAQUEMOS removeFirst, QUE NO SEA EL ULTIMO ELEMENTO --------------------------------- En algunos.
    /**
     *
     */
    private void actualizarComportamientoProcesoBloqueado() {
        if ( this.listaBloqueados.size() != 0 ) {
            Iterator<IProceso> iter = listaBloqueados.iterator();
            while(iter.hasNext()) {
                IProceso procesoSeleccionado = (IProceso) iter.next();
                    if ( procesoSeleccionado.getComportamiento().getFirst() == 1 ) {                //Si fue su ultima espera, lo pasa a la Cola nuevamente.
                        procesoSeleccionado.getComportamiento().removeFirst();                      //  Remuevo el primer valor que es un 1, en su ultimo ciclo.
                        if (procesoSeleccionado.getComportamiento().size() != 0) {                      // Si aun tiene ciclos por hacer...
                            this.ingresarProceso(procesoSeleccionado, procesoSeleccionado.getPrioridad());         // Lo agrega a la cola.
                            iter.remove();
                        } else {
                            iter.remove(); 
                        }
                    } else {
                        Integer nuevoValor = procesoSeleccionado.getComportamiento().getFirst() - 1;
                        procesoSeleccionado.getComportamiento().set(0, nuevoValor);
                    }
                }
        }
    }
    
    /*
     {
        
        Iterator<IProceso> iter = listaBloqueados.iterator();
        for (int i = 0; i < listaBloqueados.size(); i++) {
            if (iter.hasNext()) {                              //Iterador de java
                Proceso procesoSeleccionado = (Proceso) iter.next();
                if ( procesoSeleccionado.getComportamiento().getFirst() == 1 ) {                //Si fue su ultima espera, lo pasa a la Cola nuevamente.
                    procesoSeleccionado.getComportamiento().removeFirst();                      //  Remuevo el primer valor que es un 1, en su ultimo ciclo.
                    if (procesoSeleccionado.getComportamiento() != null) {                      // Si aun tiene ciclos por hacer...
                        IProceso p = this.listaBloqueados.remove(i);  //  Lo remuve de la lista bloqueada.
                        this.ingresarProceso(p, p.getPrioridad());         // Lo agrega a la cola.
                    } else {
                        this.listaBloqueados.remove(i); 
                    }
                } else {
                    Integer nuevoValor = procesoSeleccionado.getComportamiento().getFirst() - 1;
                    procesoSeleccionado.getComportamiento().set(i, nuevoValor);
                }
            }
        }
    }
     */
    
    
     /*int prioridad = 3 - (p.getEnvejecimiento() % 5) + (p.getFeedback() % 2);
                        if (p.getEntradaSalida()) {
                            prioridad = prioridad + 2;
                        }
                        if (prioridad == 0) {
                            p.setPrioridad(1);
                        } else {
                            p.setPrioridad(prioridad);
                        }
    
    
    */
                       
    
    
    
    /**
     *
     */
    private void asignarProcesosCpusVacios() {
        System.out.println("      3 - Estoy asignando procesos a CPU`s vacios.");
        for (ICpu cpu : this.cpus) {
            // Chequear Cpu por Cpu cual esta vacio para pasarle un proceso.
            if (!cpu.hayProceso()) {
                for (int i = 1; i < this.pilaListas.length; i++) {
                    // Buscar el primer proceso con mayor prioridad.
                    if (!this.pilaListas[i].isEmpty()) {
                        IProceso primero = this.pilaListas[i].removeFirst();
                        cpu.setProcesoCorriendo(primero);
                        break;
                    }
                }
            }
        }
    }

    /**
     *
     * @param pproceso
     * @param lista
     */
    @Override
    public void ingresarProceso(IProceso pproceso, int lista) {
        this.restarUnoProcesosRestantes();
        this.pilaListas[lista].addLast(pproceso);        //Esto simula la pila X, por defecto deberia ser la 3
        this.restarUnoProcesosRestantes();
    }

    /**
     *
     * @param proceso
     */
    @Override
    public synchronized void ingresarProcesoListaBloqueados(IProceso proceso) {
        this.sumarUnoProcesosRestantes();
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
    // End Methods ****************************************

    /**
     *
     * @return
     */
    @Override
    public int getCantProcesosRestantes() {
        return cantProcesosRestantes;
    }

    /**
     *
     * @param cantProcesosRestantes
     */
    @Override
    public void setCantProcesosRestantes(int cantProcesosRestantes) {
        this.cantProcesosRestantes -= cantProcesosRestantes;
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
    public void restarUnoProcesosRestantes(){
        this.cantProcesosRestantes -= 1;
    }
 
    @Override
    public void sumarUnoProcesosRestantes(){
        this.cantProcesosRestantes += 1;
    }

    @Override
    public int getTamanioQuantum() {
        return this.tamanioQuantum;
    }
    
    public void formulaRecalcularPrioridad(IProceso pproceso){
    
        //  5 quantums = 20 ciclos
        int prioridadBase = pproceso.getPrioridadInicial();
        int envejecimiento = (pproceso.getCantCiclosEsperando() / this.tamanioQuantum) % 5;
        int feedback = (pproceso.getCantCiclosEjecutando() / this.tamanioQuantum) % 2;

        int prioridadNueva = prioridadBase - envejecimiento + feedback;
        
        if ( prioridadNueva <= 0 )
            pproceso.setPrioridad(1);
        else if (prioridadNueva >= this.CANTIDAD_DE_COLAS)
            pproceso.setPrioridad(this.CANTIDAD_DE_COLAS);
        else
            pproceso.setPrioridad(prioridadNueva);
    }
    
      /*          int prioridad = 3 - (p.getEnvejecimiento() % 5) + (p.getFeedback() % 2);
                        if (p.getEntradaSalida()) {
                            prioridad = prioridad + 2;
                        }
                        if (prioridad == 0) {
                            p.setPrioridad(1);
                        } else {
                            p.setPrioridad(prioridad);
                        }                                                   */
                    
    
}