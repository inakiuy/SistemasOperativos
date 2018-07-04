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
        //final String RUTA_DATOS = "./src/datos/procesos.csv";
        //final String RUTA_DATOS = "./src/datos/set_Aleatorio.csv";
        final String RUTA_DATOS = "./src/datos/set_MuchosCortos_PocosLargos.csv";
        //final String RUTA_DATOS = "./src/datos/set_MuchosLargos_PocosCortos.csv";
        //final String RUTA_DATOS = "./src/datos/set_rafagas_ProcesosCortos.csv";
        //final String RUTA_DATOS = "./src/datos/set_rafagas_ProcesosLargos.csv";
        
        // Fin Paramtros *****************************************************
        
        //Cargamos los datos de prueba en una lista para luego alimentar el sistema
        final IDatasource datos = new ProcesosDatasource(RUTA_DATOS);
        
        //Creamos el reloj que sincronizara la simulacion
        Runnable ru_reloj = new Reloj(NOMBRE_RELOJ, INTERVALO_TIEMPO, datos);
        Thread th_reloj = new Thread(ru_reloj);
        
        //Iniciamos la simulacion
        th_reloj.start();
    }
    
}
