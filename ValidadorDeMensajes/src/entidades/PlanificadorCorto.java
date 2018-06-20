/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.ArrayList;
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
    private int cantProcesosRestantes;
    private AtomicBoolean monitorPC;
    private LinkedList<IProceso>[] pilaListas;
    private final int tamanioQuantum;       // Lo deberia de saber el Proceso, o el proceso saber en si cuantos cuantums va el?
    private final ICpu[] cpus;              //FINAL?? 
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */
    /**
     * Constructor basico
     *
     * @param pmonitorPC
     */
    public PlanificadorCorto(AtomicBoolean pmonitorPC, ICpu[] pcpus) {
        this.monitorPC = pmonitorPC;
        this.tamanioQuantum = 4;
        this.cpus = pcpus;
        this.cantProcesosRestantes = 500;
        this.pilaListas = new LinkedList[6];    // Array de 5, se usaran solo 4, del 1 al 5.
        
        for ( int i = 1 ; i < this.pilaListas.length ; i++) {
            pilaListas[i] = new LinkedList<IProceso>();
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
                System.out.println("2 - Ejecutando planificador CORTO...");
                
                
                
                System.out.println("2 - Fin planificador CORTO.");
                synchronized (monitorPC) {
                    monitorPC.set(false);
                    monitorPC.notify();
                }
            }
        } catch (Exception e) {
            System.out.println("Algo salio mal en PC: " + e.toString());
        }
    }
    // End Methods ****************************************

    
    private void planificar(){
        
        this.asignarProcesosCpusVacios();
        
    }
    
    
    
    private void asignarProcesosCpusVacios() {
            
            for (int i =0 ; i < this.cpus.length ; i++ ) {                          // Chequear Cpu por Cpu cual esta vacio para pasarle un proceso.
            if ( ! this.cpus[i].hayProceso() ){
                for ( int j = 1; j <= this.pilaListas.length; j++ ) {           // Buscar el primer proceso con mayor prioridad.
                    if ( ! this.pilaListas[j].isEmpty() ) {
                        IProceso primero = this.pilaListas[j].removeFirst();
                        this.cpus[i].setProcesoCorriendo(primero);
                        break;
                    }  
                } 
                break;      //Es necesario este BREAK para el primer for?  -------------
            }  
        }
    }      
    
    
    
    
    
    
    @Override
    public void ingresarProceso(IProceso pproceso) {
        this.pilaListas[3].addLast(pproceso);        //Esto simula la pila 3
        this.cantProcesosRestantes -= 1;
    }
    
    @Override
    public int getCantProcesosRestantes() {
        return cantProcesosRestantes;
    }

    @Override
    public void setCantProcesosRestantes(int cantProcesosRestantes) {
        this.cantProcesosRestantes -= cantProcesosRestantes;
    }

    @Override
    public AtomicBoolean getMonitorPC() {
        return monitorPC;
    }

    @Override
    public void setMonitorPC(AtomicBoolean monitorPC) {
        this.monitorPC = monitorPC;
    }

    @Override
    public LinkedList[] getPilaListas() {
        return pilaListas;
    }

    @Override
    public void setPilaListas(LinkedList[] pilaListas) {
        this.pilaListas = pilaListas;
    }
}
