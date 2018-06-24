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
public interface IProceso {
    public String getNombre();
    public String getTipo();
    public LinkedList<Integer> getComportamiento();
    public Long getTiempoDeLlegada();
    public Integer getPrioridad();
    public Integer getCantCiclosEjecutando();
    public Integer getCantCiclosEsperando();
    public Integer getPrioridadInicial();
    public Integer[] getVectorDeInformacion();
    public void setPrioridad(int prioridad);
    public void setCantCiclosEjecutando(int cantCiclosEjecutando);
    public void setCantCiclosEsperando(int cantCiclosEsperando);
    public void sumarUnoCiclosEsperando();
    public void sumarUnoCiclosEjecutando();
    public void cambiarPrioridad(int cantidad);
    public void logEstadisticas();
    public void aumentarDosPrioridades();
}
