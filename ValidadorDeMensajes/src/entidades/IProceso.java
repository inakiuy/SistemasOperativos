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
    public int getPrioridad();
    public int getFeedback();
    public int getCantCiclosEjecutando();
    public int getCantCiclosEsperando();
    public int getPrioridadInicial();
    public Boolean getEntradaSalida();
    
    public void setPrioridad(int prioridad);
    public void setFeedback(int feedback);
    public void setEntradaSalida(Boolean entradaSalida);
    public void setCantCiclosEjecutando(int cantCiclosEjecutando);
    public void setCantCiclosEsperando(int cantCiclosEsperando);
    
    
    public void sumarUnoFeedback();
    public void sumarUnoCiclosEsperando();
    public void sumarUnoCiclosEjecutando();

    
}
