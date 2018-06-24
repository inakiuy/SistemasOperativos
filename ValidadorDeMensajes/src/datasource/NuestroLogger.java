/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasource;

/**
 *
 * @author Inaki
 */
public class NuestroLogger {
   
    public static void logEstadisticas(String[] datos){
        
        String wt = datos[1];
        String st = datos[2];
        
        Integer tt = Integer.parseInt(st) + Integer.parseInt(wt);
        
        datos[3] = tt.toString();
        
        String rutaArchivoLog = "./src/datos/procesos.log";
        String[] CSV = {String.join(";", datos)};
        ManejadorArchivosGenerico1.escribirArchivo(rutaArchivoLog, CSV);
    }
    
    public static void logConsola(String salidaConsola){
        String rutaArchivoLog = "./src/datos/consola.log";
        String[] datos = {salidaConsola};
        ManejadorArchivosGenerico1.escribirArchivo(rutaArchivoLog, datos);
    }
}
