/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasource;

import java.util.LinkedList;
import entidades.IProceso;
import entidades.Proceso;

/**
 *
 * @author Inaki
 */
public class ProcesosDatasource implements IDatasource{
    /**
     * Atributes
     * *****************************************************
     */
    private LinkedList<IProceso> procesos;
    private String RUTA_DATOS = "./src/datos/procesos.txt";
    // End Atributes *************************************

    
    /**
     * Constructors
     * ****************************************************
     */
    
    /**
     * Constructor de la fuente de datos. Al inicializarse se cargan los datos
     * en una lista
     */
    public ProcesosDatasource(){
        this.procesos = new LinkedList<>();
        String[] datos_csv = ManejadorArchivosGenerico1.leerArchivo(RUTA_DATOS, true);
        for (String dato : datos_csv){
            String[] datos_proc = dato.split(";");
            String proc_nombre = datos_proc[0];
            String proc_tipo = datos_proc[1];
            String proc_comportamiento = datos_proc[2];
            Proceso proc = new Proceso(proc_nombre, proc_tipo, proc_comportamiento);
            procesos.addLast(proc);
        }
    }
    // End Constructors ***********************************

    
    /**
     * Methods
     * ****************************************************
     */
    // End Methods ****************************************

    
    /**
     * Getters and Setters
     * ****************************************************
     */
    
     /**
      * 
      * @return 
      */
    public IProceso getPrimerProc(){
        return this.procesos.getFirst();
    }
    // End Getters and Setters **************************** 
}
