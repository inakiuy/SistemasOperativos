/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Inaki
 */
public class MonitoresCPUs {
    /**
     * Atributes *****************************************************
     */
    private final AtomicBoolean monitorReloj;
    private final MonitorCPU[] monitoresCPUs;
    private AtomicInteger finalizados;
    // End Atributes **************************************    

    /**
     * Constructors ****************************************************
     */
    
    public MonitoresCPUs(Integer pcantidad_cpus){
        this.monitorReloj = new AtomicBoolean();
        this.monitoresCPUs = new MonitorCPU[pcantidad_cpus];
        this.finalizados = new AtomicInteger();
    }
    // End Constructors ***********************************


    /**
     * Methods ****************************************************
     */
    public void notificarATodos(){
        for (MonitorCPU monitor : monitoresCPUs){
            synchronized (monitor){
                monitor.setContinuar(Boolean.TRUE);
                monitor.notify();
            }
        }
    }
    
    public synchronized void continuar(){
        Integer cantidad_finalizados = finalizados.addAndGet(1);
        if (cantidad_finalizados == monitoresCPUs.length){
            finalizados.set(0);
            synchronized (monitorReloj){
                monitorReloj.set(false);
                monitorReloj.notify();
            }
        }
    }
    // End Methods ****************************************

    /**
     * Getters and Setters ****************************************************
     */
    /**
     * @return the monitorReloj
     */
    public AtomicBoolean getMonitorReloj() {
        return monitorReloj;
    }

    /**
     * @return the monitoresCPUs
     */
    public MonitorCPU[] getMonitoresCPUs() {
        return monitoresCPUs;
    }
    // End Getters and Setters ****************************
}
