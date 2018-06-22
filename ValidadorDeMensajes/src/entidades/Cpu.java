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
    private final String nombre;
    private IProceso procesoCorriendo;
    private AtomicBoolean monitorCPUs;
    private final IPlanificadorCorto planificadorCorto;
    // End Atributes **************************************

    /**
     * Constructors ****************************************************
     */
    /**
     *
     * @param pnombre
     * @param monitorCPUs
     * @param pPlanificadorCorto
     */
    public Cpu(String pnombre, AtomicBoolean monitorCPUs, IPlanificadorCorto pPlanificadorCorto) {
        this.nombre = pnombre;
        this.monitorCPUs = monitorCPUs;
        this.planificadorCorto = pPlanificadorCorto;
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
                    while (!monitorCPUs.get()) {
                        monitorCPUs.wait();
                    }
                }

                System.out.println("      5 - Ejecutando ciclo de " + this.getNombre());

                if (this.getProcesoCorriendo() != null) {
                    System.out.println("      5 - Proceso corriendo: " + this.getProcesoCorriendo().getNombre() + " - " + this.getProcesoCorriendo().getTipo());

                    this.trabajar();

                    // ACA IRIA HACER QUE EL PROCESO PUBLIQUE O LO QUE SEA...    ??????
                } else {
                    System.out.println("      5 - " + this.getNombre() + " vacio");
                }
                Thread.sleep(200);

                System.out.println("      5 - Fin ciclo de " + this.getNombre());

                synchronized (monitorCPUs) {
                    monitorCPUs.set(false);
                    monitorCPUs.notify();
                }
            }
        } catch (Exception e) {
            System.out.println("Algo salio mal en CPU + " + this.getNombre() + ": " + e.toString());
        }
    }

    /**
     * Realiza trabajo sobre el proceso cargado
     */
    /*    private void trabajar() {
        
        int x = this.procesoCorriendo.getComportamiento().getFirst();
        
        int y =this.planificadorCorto.getTamanioQuantum();
        int z = this.procesoCorriendo.getCantQuantums();
        
        if ((x-y)>1){
            //llega al quantum
            if (z >= 2) {
                this.procesoCorriendo.setFeedback(this.procesoCorriendo.getFeedback()+2);
            }
            this.procesoCorriendo.setCantQuantums(this.procesoCorriendo.getCantQuantums()+1);
            this.planificadorCorto.ingresarProcesoListaBloqueados(procesoCorriendo);      //Lo pasamos a la lista bloqueado del PC.
            this.procesoCorriendo = null;                                                   // Se declara que no hay proceso en el CPU.
        } else {
            if (x != 1) {                                                   // Disminuye en uno el numero del primero.
                this.procesoCorriendo.getComportamiento().set(0, x - 1);
            } else {
                this.procesoCorriendo.getComportamiento().removeFirst();      // Elimina el primero numero y cambia al estado de E/S.
                this.procesoCorriendo.setFeedback(this.procesoCorriendo.getFeedback()-1);
                this.procesoCorriendo.setEntradaSalida(true);
                this.planificadorCorto.ingresarProcesoListaBloqueados(procesoCorriendo);      //Lo pasamos a la lista bloqueado del PC.
                this.procesoCorriendo = null;                                                   // Se declara que no hay proceso en el CPU.
            }
        }
    }*/
    private void trabajar() {

        int comportamientoProceso = this.procesoCorriendo.getComportamiento().getFirst();

        if (comportamientoProceso != 1) {                                                   // Disminuye en uno el numero del primero.
            this.procesoCorriendo.getComportamiento().set(0, comportamientoProceso - 1);
        } else {
            this.procesoCorriendo.getComportamiento().removeFirst();      // Elimina el primero numero y cambia al estado de E/S.
           
            if ( this.procesoCorriendo.getComportamiento().size() != 0 ) { 
                this.planificadorCorto.ingresarProcesoListaBloqueados(procesoCorriendo);      //Lo pasamos a la lista bloqueado del PC.
            }
            this.procesoCorriendo = null;    
        }
    }

    // End Methods ****************************************
    /**
     * Getters and Setters ****************************************************
     */
    /**
     * @return the nombre
     */
    @Override
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the procesoCorriendo
     */
    @Override
    public IProceso getProcesoCorriendo() {
        return procesoCorriendo;
    }

    /**
     * @param procesoCorriendo the procesoCorriendo to set
     */
    @Override
    public void setProcesoCorriendo(IProceso procesoCorriendo) {
        this.procesoCorriendo = procesoCorriendo;
    }

    @Override
    public void borrarProcesoCorriendo() {
        this.procesoCorriendo = null;
    }

    // End Getters and Setters ****************************   
    @Override
    public boolean hayProceso() {
        return this.procesoCorriendo != null;
    }

}
