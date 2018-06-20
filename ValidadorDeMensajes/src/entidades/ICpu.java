/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author Inaki
 */
interface ICpu extends Runnable{

    public String getNombre();
    public IProceso getProcesoCorriendo();
    public void setProcesoCorriendo(IProceso procesoCorriendo);
    public void borrarProcesoCorriendo();
    public boolean hayProceso();
    
}
