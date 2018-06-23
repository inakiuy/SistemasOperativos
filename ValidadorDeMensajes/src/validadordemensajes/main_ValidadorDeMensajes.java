/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validadordemensajes;

import entidades.Reloj;
import datasource.IDatasource;
import datasource.ProcesosDatasource;

/**
 *
 * @author Inaki
 */
public class main_ValidadorDeMensajes {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         * Paramtros *****************************************************
         */
        final int INTERVALO_TIEMPO = 1;
        final String NOMBRE_RELOJ = "RELOJ-1";
        // Fin Paramtros *****************************************************
        
        //Cargamos los datos de prueba en una lista para luego alimentar el sistema
        final IDatasource datos = new ProcesosDatasource();
        
        //Creamos el reloj que sincronizara la simulacion
        Runnable ru_reloj = new Reloj(NOMBRE_RELOJ, INTERVALO_TIEMPO, datos);
        Thread th_reloj = new Thread(ru_reloj);
        
        //Iniciamos la simulacion
        th_reloj.start();
    }
    
}
