package server;

import java.io.Serializable;

/**
 * Classe astratta che ha come figlie Ingresso e Consumazione
 * @author Giovanni Calà
**/

public abstract class Omaggio implements Serializable {
   
    public Omaggio() {
        
    }
    
    /**
     * Metoodo astratto 
     * @return il nome dell'omaggio
     */
    public abstract String getNome();
}
