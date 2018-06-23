/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasource;

import java.util.LinkedList;
import java.util.Iterator;
import entidades.IProceso;
import entidades.Proceso;

/**
 *
 * @author Inaki
 */
public class ProcesosDatasource implements IDatasource {

    /**
     * Atributes *****************************************************
     */
    private LinkedList<IProceso> listaProcesos;             //Procesos esperando ser recibidos por el Planificador Largo
    // End Atributes *************************************

    /**
     * Constructors ****************************************************
     */
    /**
     * Constructor de la fuente de datos. Al inicializarse se cargan los datos
     * en una lista
     */
    public ProcesosDatasource(String prutaDatos) {
        this.listaProcesos = new LinkedList<>();
        String[] datos_csv = ManejadorArchivosGenerico1.leerArchivo(prutaDatos, true);  //Hay que ignorar la cabeza
        for (String dato : datos_csv) {
            String[] datos_proc = dato.split(";");
            String proc_nombre = datos_proc[0];
            String proc_tipo = datos_proc[1];
            String proc_comportamiento = datos_proc[2];
            Long proc_tiempoDeLlegada = Long.parseLong(datos_proc[3]);
            Proceso proc = new Proceso(proc_nombre, proc_tipo, proc_comportamiento, proc_tiempoDeLlegada);
            this.insertarOrdenado(proc);
        }
    }
    // End Constructors ***********************************

    /**
     * Methods ****************************************************
     */
    
    /**
     * Inserta de manera ordenada por tiempo de llegada
     * @param proc 
     */
    private void insertarOrdenado(Proceso proc) {
        if (!listaProcesos.isEmpty()) {
            if (proc.getTiempoDeLlegada() >= listaProcesos.getLast().getTiempoDeLlegada()) {
                listaProcesos.addLast(proc);
            } else {
                Iterator<IProceso> iter = listaProcesos.iterator();
                for (int i = 0; i < listaProcesos.size(); i++) {
                    if (iter.hasNext()) {                              //Iterador de java
                        Proceso procesoSeleccionado = (Proceso) iter.next();
                        if (proc.getTiempoDeLlegada() < procesoSeleccionado.getTiempoDeLlegada()) {
                            if ( i > 1){
                                listaProcesos.add(i - 1, proc);            //Agrega al anteriro, ya que se paso por uno.
                            } else{
                                listaProcesos.addFirst(proc);            //Agrega al principio
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            listaProcesos.addLast(proc);
        }
    }

    /**
     * Obtiene el proximo proseso en llegar en el tiempo indicado por el
     * parametro.
     * 
     * @param tiempo
     * @return
     */
    @Override
    public IProceso getPrimerProcYEliminar(long tiempo) {
        IProceso proceso = null;
        if ( !this.listaProcesos.isEmpty() && this.listaProcesos.getFirst().getTiempoDeLlegada() == tiempo) {
            proceso = this.listaProcesos.removeFirst();
        }
        return proceso;
    }
    // End Methods ****************************************

    /**
     * Getters and Setters ****************************************************
     */
    public LinkedList<IProceso> getListaProcesos() {
        return listaProcesos;
    }
    // End Getters and Setters **************************** 
}