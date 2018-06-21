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
    public void setPrioridad(int prioridad);
    public int getCantQuantums();
    public void setCantQuantums(int cantQuantums);
    public int getFeedback();
    public void setFeedback(int feedback);
    public int getEnvejecimiento();
    public void setEnvejecimiento(int envejecimiento);
    public Boolean getEntradaSalida();
    public void setEntradaSalida(Boolean entradaSalida);
}
