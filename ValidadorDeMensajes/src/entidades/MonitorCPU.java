package entidades;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 *
 * @author Inaki
 */
public class MonitorCPU {
    /**
     * Atributes *****************************************************
     */
    private final AtomicBoolean continuar;
    // End Atributes *************************************************

    /**
     * Constructors **************************************************
     */    
    /**
     * COnstructor de la bandera 
     */
    public MonitorCPU(){
        this.continuar = new AtomicBoolean();
    }
    // End Constructors ***********************************
    
    public Boolean getContinuar(){
        return continuar.get();
    }
    
    public void setContinuar(Boolean pcontinuar){
        this.continuar.set(pcontinuar);
    }
}
