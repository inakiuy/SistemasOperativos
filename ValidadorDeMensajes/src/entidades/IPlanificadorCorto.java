/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Inaki
 */
public interface IPlanificadorCorto extends Runnable{
    public void ingresarProceso(IProceso pproceso);
    public int getCantProcesosRestantes();
    public void setCantProcesosRestantes(int cantProcesosRestantes);
    public AtomicBoolean getMonitorPC();
    public void setMonitorPC(AtomicBoolean monitorPC);
    public LinkedList[] getPilaListas();
    public void setPilaListas(LinkedList[] pilaListas);
    
}
