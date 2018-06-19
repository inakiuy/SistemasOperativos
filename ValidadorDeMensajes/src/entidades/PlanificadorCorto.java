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
public class PlanificadorCorto implements IPlanificadorCorto, Runnable {

    /**
     * Atributes *****************************************************
     */
    private AtomicBoolean monitorPC;
    private LinkedList[] colas;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */
    /**
     * Constructor basico
     *
     * @param pmonitorPC
     */
    public PlanificadorCorto(AtomicBoolean pmonitorPC) {
        this.monitorPC = pmonitorPC;
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
}
