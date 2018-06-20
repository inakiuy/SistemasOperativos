/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import datasource.IDatasource;
import datasource.ProcesosDatasource;
import java.util.Iterator;
import java.util.LinkedList;
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
    private LinkedList<IProceso> listaProcesosPL;   
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

    public PlanificadorLargo(AtomicBoolean pmonitorPL, Reloj preloj, ProcesosDatasource pFuenteDeDatosProcesos, IPlanificadorCorto pplanificadorCorto) {
        this.monitorPL = pmonitorPL;
        this.reloj = preloj; 
        this.fuenteDeDatosProcesos = pFuenteDeDatosProcesos;
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
                
                this.planificar();
                
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
    
        this.obtenerNuevosProcesos();      
        while ( this.listaProcesosPL.size() != 0 || this.planificadorCorto.getCantProcesosRestantes() != 0 ) {
            if ( this.listaProcesosPL.size() != 0 ) {
            IProceso x = this.listaProcesosPL.removeFirst();
            this.planificadorCorto.ingresarProceso(x);
            }
        }      
    }
    
    private void obtenerNuevosProcesos(){
        
        boolean hayProcesos = true;
        
        while ( hayProcesos ) {
            IProceso x = this.fuenteDeDatosProcesos.getPrimerProcYEliminar( this.reloj.getTiempoActual() );
            if ( x != null ) {
                this.listaProcesosPL.addLast(x);
            }
            else { hayProcesos = false; }
        }
    }
    // End Methods ****************************************
}
