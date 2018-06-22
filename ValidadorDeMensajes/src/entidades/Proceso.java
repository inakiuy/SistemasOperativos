/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.LinkedList;

/**
 *
 * @author Inaki
 */
public class Proceso implements IProceso {

    /**
     * Atributes *****************************************************
     */
    private String nombre;
    private String tipo;
    private LinkedList<Integer> comportamiento;
    private Long tiempoDeLlegada;
    private int prioridad;
    private int cantQuantums;
    private int feedback;
    private int envejecimiento;
    private Boolean entradaSalida;
    
    // End Atributes *************************************

      
    /**
     * Constructors ****************************************************
     */
    /**
     * Constructor basico
     *
     * @param pnombre
     * @param ptipo
     */
    public Proceso(String pnombre, String ptipo) {
        this.nombre = pnombre;
        this.tipo = ptipo;
    }

    /**
     * Constructor con comportamiento
     *
     * @param pnombre
     * @param ptipo
     * @param pComportamiento
     * @param ptiempoDeLlegada
     */
    public Proceso(String pnombre, String ptipo, String pComportamiento, Long ptiempoDeLlegada) {
        this.nombre = pnombre;
        this.tipo = ptipo;
        this.comportamiento = new LinkedList();
        String[] datos_comportamiento = pComportamiento.split(",");
        for (String dato : datos_comportamiento) {
            comportamiento.add(Integer.parseInt(dato));
        }
        this.tiempoDeLlegada = ptiempoDeLlegada;
        this.prioridad = 3;                     // Prioridad 3 por defecto.
        this.cantQuantums = 0;
        this.feedback = 0;
        this.envejecimiento = 0;
        this.entradaSalida = false;
      }

    // End Constructors ***********************************
    
    
    /**
     * Methods ****************************************************
     */
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
     * @return the tipo
     */
    @Override
    public String getTipo() {
        return tipo;
    }

    /**
     * @return the comportamiento
     */
    @Override
    public LinkedList<Integer> getComportamiento() {
        return comportamiento;
    }

    /**
     * @return the tiempoDeLlegada
     */
    @Override
    public Long getTiempoDeLlegada() {
        return tiempoDeLlegada;
    }
    // End Getters and Setters ****************************    

    @Override
    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public int getCantQuantums() {
        return cantQuantums;
    }

    @Override
    public void setCantQuantums(int cantQuantums) {
        this.cantQuantums = cantQuantums;
    }

    @Override
    public int getFeedback() {
        return feedback;
    }

    @Override
    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    @Override
    public int getEnvejecimiento() {
        return envejecimiento;
    }

    @Override
    public void setEnvejecimiento(int envejecimiento) {
        this.envejecimiento = envejecimiento;
    }

    @Override
    public Boolean getEntradaSalida() {
        return entradaSalida;
    }

    @Override
    public void setEntradaSalida(Boolean entradaSalida) {
        this.entradaSalida = entradaSalida;
    }
    
    
 }