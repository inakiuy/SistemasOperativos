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
public class Proceso implements IProceso{
    /**
     * Atributes
     * *****************************************************
     */
    private String nombre;
    private String tipo;
    private LinkedList<Integer> comportamiento;
    // End Atributes *************************************


    /**
     * Constructors
     * ****************************************************
     */

     /**
      * Constructor basico
      * 
      * @param pnombre
      * @param ptipo 
      */
    public Proceso(String pnombre, String ptipo){
        this.nombre = pnombre;
        this.tipo = ptipo;       
    }

    /**
      * Constructor con comportamiento
      * 
      * @param pnombre
      * @param ptipo 
      */
    public Proceso(String pnombre, String ptipo, String pComportamiento){
        this.nombre = pnombre;
        this.tipo = ptipo;
        this.comportamiento = new LinkedList();
        String[] datos_comportamiento = pComportamiento.split(",");
        for (String dato : datos_comportamiento){
            comportamiento.add(Integer.parseInt(dato));
        }
    }
    

    // End Constructors ***********************************
    
    
    /**
     * Methods
     * ****************************************************
     */
    // End Methods ****************************************    

    
    /**
     * Getters and Setters
     * ****************************************************
     */
    
    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return the comportamiento
     */
    public LinkedList getComportamiento() {
        return comportamiento;
    }
    // End Getters and Setters ****************************    
}
