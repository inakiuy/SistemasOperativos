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
   
    public static void log(String[] datos){
        String rutaArchivoLog = "./src/datos/procesos.log";
        String[] CSV = {String.join(";", datos)};
        ManejadorArchivosGenerico1.escribirArchivo(rutaArchivoLog, CSV);
    }
}
