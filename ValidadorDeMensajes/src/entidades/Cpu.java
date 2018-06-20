/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Inaki
 */
public class Cpu implements ICpu {

    /**
     * Atributes *****************************************************
     */
    private String nombre;
    private IProceso procesoCorriendo;
    private AtomicBoolean monitorCPUs;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */
    /**
     *
     * @param pnombre
     */
    public Cpu(String pnombre, AtomicBoolean monitorCPUs) {
        this.nombre = pnombre;
        this.monitorCPUs = monitorCPUs;
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
                    while ( !monitorCPUs.get() ) {
                        monitorCPUs.wait();
                    }
                }
                System.out.println("3 - Ejecutando ciclo de " + this.getNombre());
                
                
                
                
                if (this.getProcesoCorriendo() != null) {
                    System.out.println("3 - Proceso corriendo: " + this.getProcesoCorriendo().getNombre() + " - " + this.getProcesoCorriendo().getTipo());
                } else {
                    System.out.println("3 - " + this.getNombre() + " vacio");
                }
                Thread.sleep(1000);
                
                
                
                
                
                System.out.println("3 - Fin ciclo de " + this.getNombre());

                synchronized (monitorCPUs) {
                    monitorCPUs.set(false);
                    monitorCPUs.notify();
                }
            }
        } catch (Exception e) {
            System.out.println("Algo salio mal en CPU + " + this.getNombre() + ": " + e.toString());
        }
    }
    // End Methods ****************************************

    /**
     * Getters and Setters ****************************************************
     */
    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the procesoCorriendo
     */
    public IProceso getProcesoCorriendo() {
        return procesoCorriendo;
    }

    /**
     * @param procesoCorriendo the procesoCorriendo to set
     */
    public void setProcesoCorriendo(IProceso procesoCorriendo) {
        this.procesoCorriendo = procesoCorriendo;
    }
    
    public void borrarProcesoCorriendo(){
        this.procesoCorriendo = null;
    }
    
    // End Getters and Setters ****************************   
    
    public boolean hayProceso(){
        if ( this.procesoCorriendo != null ){
            return true;
        }
        else return false;
    }


    
}
