/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.Iterator;
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

    /**
     * @return the tiempoDeLlegada
     */
    public Long getTiempoDeLlegada() {
        return tiempoDeLlegada;
    }
    // End Getters and Setters ****************************    

    Iterator<IProceso> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
