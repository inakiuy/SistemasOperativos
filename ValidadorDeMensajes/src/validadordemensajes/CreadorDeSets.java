/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validadordemensajes;

import datasource.ManejadorArchivosGenerico1;
import java.util.Random;
/**
 *
 * @author Inaki
 */
public class CreadorDeSets {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] procesoA = {"calc_puntaje_mensaje-","A","6,1,2",""};
        String[] procesoB = {"firmar_mensaje-","B","3,6,3",""};
        String[] procesoC = {"calc_nivel_confianza-","C","5,2,5",""};
        String[] procesoD = {"consultar_validez_mensaje-","D","2,2",""};
        String[] procesoE = {"calc_premio-","E","10,5,10,5",""};
        
        String[][] procesos = {procesoA,procesoB, procesoC, procesoD, procesoE};
        Integer tiempoLlegada = 0;
        Integer minrangoTiempo = 0;
        Integer maxrangoTiempo = 4;
        Random randomTiempo = new Random();
        Integer RandomTiempo;
        
        Integer minrangoSeleccion = 0;
        Integer maxrangoSeleccion = 4;
        Random randomSeleccion = new Random();
        Integer RandomSeleccion;
        String[] datos = new String[1000];
        
        for (int i = 0 ; i < 1000; i++){
            RandomTiempo = randomTiempo.nextInt(maxrangoTiempo - minrangoTiempo + 1) + minrangoTiempo;
            tiempoLlegada += RandomTiempo;
            
            RandomSeleccion = randomSeleccion.nextInt(maxrangoSeleccion - minrangoSeleccion + 1) + minrangoSeleccion;
            String[] proceso = procesos[RandomSeleccion].clone();
            proceso[0] = proceso[0] + i;
            proceso[3] = tiempoLlegada.toString();
            
            datos[i] = String.join(";", proceso);
            System.out.println(datos[i]);
        }
        
        ManejadorArchivosGenerico1.escribirArchivo("./src/datos/set_Aleatorio.csv", datos);
    }    
}
