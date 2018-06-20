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
}
