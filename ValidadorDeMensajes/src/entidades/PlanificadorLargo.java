/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import datasource.IDatasource;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Inaki
 */
public class PlanificadorLargo implements IPlanificadorLargo {

    /**
     * Atributes *****************************************************
     */
    private AtomicBoolean monitorPL;
    private Reloj reloj;
    private IDatasource fuenteDeDatosProcesos;
    private IPlanificadorCorto planificadorCorto;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */
    /**
     * Constructor con objeto sincronizador
     *
     * @param pmonitorPL
     */
    public PlanificadorLargo(AtomicBoolean pmonitorPL) {
        this.monitorPL = pmonitorPL;
    }

    public PlanificadorLargo(AtomicBoolean pmonitorPL, Reloj preloj, IDatasource pfuenteDeDatosProcesos, IPlanificadorCorto pplanificadorCorto) {
        this.monitorPL = pmonitorPL;
        this.reloj = preloj;
        this.fuenteDeDatosProcesos = pfuenteDeDatosProcesos;
        this.planificadorCorto = pplanificadorCorto;
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
                System.out.println("1 - Ejecutando planificador LARGO...");
                
                
                
                System.out.println("1 - Tiempo actual: " + this.reloj.getTiempoActual());
                
                
                
                
                
                System.out.println("1 - Fin planificador LARGO");
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
     * 
     */
    private void planificar(){
        
    }
    // End Methods ****************************************
}
