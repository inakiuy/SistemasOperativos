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
    private final int tamanioQuantum;       // Lo deberia de saber el Proceso, o el proceso saber en si cuantos cuantums va el?
    private final ICpu[] cpus;              //FINAL??
    private final Integer CANTIDAD_DE_COLAS = 5;
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
        for ( int i = 1 ; i < this.pilaListas.length ; i++) {
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
                System.out.println("    2 - Ejecutando planificador CORTO...");
                
                planificar();
                
                System.out.println("    2 - Fin planificador CORTO.");
                synchronized (monitorPC) {
                    monitorPC.set(false);
                    monitorPC.notify();
                }
            }
        } catch (Exception e) {
            System.out.println("Algo salio mal en PC: " + e.toString());
        }
    }

    /**
     * 
     */
    private void planificar(){
        //PRIMERO
        this.asignarProcesosCpusVacios();
    
        // Habria que chequear luego de que los CPU trabajen, si alguno quedo vacio, para asignarle uno nuevamente COMO HABIAMOS HABLADO.
        
        
        
        //ULTIMO
        this.actualizarComportamientoProcesoBloqueado();
     }
   
    
    // FALTA HACER CHEQUEOS EN TODOS LOS QUE SAQUEMOS removeFirst, QUE NO SEA EL ULTIMO ELEMENTO --------------------------------- En algunos.

    /**
     * 
     */
    private void actualizarComportamientoProcesoBloqueado() {        
        Iterator <IProceso> iter = listaBloqueados.iterator();            
        for(int i = 0; i < listaBloqueados.size(); i++) {
            if ( iter.hasNext() ){                              //Iterador de java
                Proceso procesoSeleccionado = (Proceso) iter.next();
                if ( (Integer) procesoSeleccionado.getComportamiento().getFirst() == 1){                //Si fue su ultima espera, lo pasa a la Cola nuevamente.
                    procesoSeleccionado.getComportamiento().removeFirst();
                    // Aca iria algo asi como If procesoSeleccionado.getComportamiento() !0 null
                    if ( procesoSeleccionado.getComportamiento() != null ) {
                        this.ingresarProceso(procesoSeleccionado,procesoSeleccionado.getPrioridad());
                    }
                    else {}         // DETRUIR EL OBJETO de ESE proceso.
                }
                else {
                    int nuevoValor = (Integer) procesoSeleccionado.getComportamiento().getFirst() - 1;
                    procesoSeleccionado.getComportamiento().set(i,nuevoValor);
                }
            }
        }
    }
    
   /**
    * 
    */
    private void asignarProcesosCpusVacios() {            
        for (ICpu cpu : this.cpus) {
            // Chequear Cpu por Cpu cual esta vacio para pasarle un proceso.
            if (!cpu.hayProceso()) {
                for (int i = 1; i <= this.pilaListas.length; i++) {
                    // Buscar el primer proceso con mayor prioridad.
                    if (! this.pilaListas[i].isEmpty()) {
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
        this.pilaListas[lista].addLast(pproceso);        //Esto simula la pila 3
        this.cantProcesosRestantes -= 1;
    }
    
    /**
     * 
     * @param proceso 
     */
    @Override
    public void ingresarProcesoListaBloqueados(IProceso proceso) {
        Iterator <IProceso> iter = listaBloqueados.iterator();            
        for(int i = 0; i < listaBloqueados.size(); i++) {
            if ( iter.hasNext() ){                              //Iterador de java
                Proceso procesoSeleccionado = (Proceso) iter.next();
                if ( (Integer) procesoSeleccionado.getComportamiento().getFirst() > proceso.getComportamiento().getFirst() ){
                    listaBloqueados.add(i-1,proceso);            //Agrega al anteriro, ya que se paso por uno.
                    break;
                }
            }
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
}
