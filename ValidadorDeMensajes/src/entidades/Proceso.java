/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import datasource.NuestroLogger;
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
    private final Integer prioridadInicial = 3;
    private Integer prioridad;
    private Integer cantCiclosEjecutando;
    private Integer cantCiclosEsperando;
    private final Integer[] vectorDeInformacion = {-1,0,0};     // RT  WT  ST  	
    
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
        this.cantCiclosEjecutando = 0;
        this.cantCiclosEsperando = 0;
        st();
    }

    // End Constructors ***********************************
    
    
    /**
     * Methods ****************************************************
     */

    private void st(){
        for ( int i = 0 ; i < this.getComportamiento().size(); i++ ){                // ST
            this.getVectorDeInformacion()[2] += this.getComportamiento().get(i);
        }
    }
    
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
    @Override
    public void cambiarPrioridad(int cantidad) {
        int nuevaPrioridad = this.prioridad + cantidad;
        if ( nuevaPrioridad <= 0 ){
            nuevaPrioridad = 1;
        }
        else if (nuevaPrioridad >= 5){
            nuevaPrioridad = 5;
        }
        this.setPrioridad(nuevaPrioridad);
    }
    
    /**
     * Obtiene las estadisticas del procesos para ser logueadas al terminar.
     * @return 
     */
    private String[] obtenerEstadisticas(){    // RT  WT  ST  TT 
        String[] estadisticas = {this.getVectorDeInformacion()[0].toString(), this.getVectorDeInformacion()[1].toString(), this.getVectorDeInformacion()[2].toString(), "0", this.getNombre()};
        return estadisticas;
    }
    
    /**
     *
     * @param ptiempoActual
     */
    @Override
    public void logEstadisticas(){
        String[] estadisticas = this.obtenerEstadisticas();
        NuestroLogger.logEstadisticas(estadisticas);
    }
    
    @Override
    public Integer[] getVectorDeInformacion(){
        return this.vectorDeInformacion;
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
    public Integer getPrioridad() {
        return prioridad;
    }

    @Override
    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public Integer getCantCiclosEjecutando() {
        return cantCiclosEjecutando;
    }

    @Override
    public void setCantCiclosEjecutando(int cantCiclosEjecutando) {
        this.cantCiclosEjecutando = cantCiclosEjecutando;
    }

    @Override
    public Integer getCantCiclosEsperando() {
        return cantCiclosEsperando;
    }

    @Override
    public void setCantCiclosEsperando(int cantCiclosEsperando) {
        this.cantCiclosEsperando = cantCiclosEsperando;
    }

    @Override
    public Integer getPrioridadInicial() {
        return this.prioridadInicial;
    }
    // End Getters and Setters ****************************    

    @Override
    public void aumentarDosPrioridades() {
        int prioriNueva = this.prioridad - 2;
        if ( prioriNueva <= 1){
            this.setPrioridad(1);
        }
        else this.setPrioridad(prioriNueva);
    }
 }