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
    private final String nombre;
    private final String tipo;
    private LinkedList<Integer> comportamiento;
    private Long tiempoDeLlegada;
    private int prioridadInicial;
    private int prioridad;
    private int cantCiclosEjecutando;
    private int cantCiclosEsperando;
    
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
        this.prioridadInicial = 3;
        this.nombre = pnombre;
        this.tipo = ptipo;
        this.comportamiento = new LinkedList();
        String[] datos_comportamiento = pComportamiento.split(",");
        for (String dato : datos_comportamiento) {
            comportamiento.add(Integer.parseInt(dato));
        }
        this.tiempoDeLlegada = ptiempoDeLlegada;
        this.prioridad = 3;                     // Prioridad 3 por defecto.
        this.cantCiclosEjecutando = 0;
        this.cantCiclosEsperando = 0;
    }

    // End Constructors ***********************************
    
    
    /**
     * Methods ****************************************************
     */
    /**
     * Aumenta la variable de ciclos esperados
     */
    @Override
    public void sumarUnoCiclosEsperando() {
        this.cantCiclosEsperando += 1;
    }
    /**
     * Aumenta la variable de ciclos ejecutados
     */
    @Override
    public void sumarUnoCiclosEjecutando() {
        this.cantCiclosEjecutando += 1;
    }
    
    /**
     * Cambia la prioridad del proceso
     * @param cantidad 
     */
    public void cambiarPrioridad(int cantidad) {
        int nuevaPrioridad = this.prioridad + cantidad;
        if ( nuevaPrioridad <= 0 ){
            nuevaPrioridad = 1;
        }
        else if (nuevaPrioridad >= 5  ){
            nuevaPrioridad = 5;
        }
        this.setPrioridad(nuevaPrioridad);
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

    @Override
    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public int getCantCiclosEjecutando() {
        return cantCiclosEjecutando;
    }

    @Override
    public void setCantCiclosEjecutando(int cantCiclosEjecutando) {
        this.cantCiclosEjecutando = cantCiclosEjecutando;
    }

    @Override
    public int getCantCiclosEsperando() {
        return cantCiclosEsperando;
    }

    @Override
    public void setCantCiclosEsperando(int cantCiclosEsperando) {
        this.cantCiclosEsperando = cantCiclosEsperando;
    }

    @Override
    public int getPrioridadInicial() {
        return this.prioridadInicial;
    }
    // End Getters and Setters ****************************    
 }