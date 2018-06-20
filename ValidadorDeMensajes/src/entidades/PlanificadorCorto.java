/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

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
    private LinkedList[] pilaListas;
    private final int tamanioQuantum;
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
        this.pilaListas = new LinkedList[5];    // Array de 5, se usaran solo 4, del 1 al 5.
        
        for ( int i = 1 ; i <= 5 ; i++) {               
            pilaListas[i] = new LinkedList<Proceso>();
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

    @Override
    public void ingresarProceso(IProceso pproceso) {
        this.pilaListas[3].addLast(pproceso);        //Esto simula la pila 3
        this.cantProcesosRestantes -= 1;
    }
    
    public int getCantProcesosRestantes() {
        return cantProcesosRestantes;
    }

    public void setCantProcesosRestantes(int cantProcesosRestantes) {
        this.cantProcesosRestantes -= cantProcesosRestantes;
    }

    public AtomicBoolean getMonitorPC() {
        return monitorPC;
    }

    public void setMonitorPC(AtomicBoolean monitorPC) {
        this.monitorPC = monitorPC;
    }

    public LinkedList[] getPilaListas() {
        return pilaListas;
    }

    public void setPilaListas(LinkedList[] pilaListas) {
        this.pilaListas = pilaListas;
    }
}
