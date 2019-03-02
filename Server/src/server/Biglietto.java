package server;

/**
 * Classe madre astratta
 * @author Giovanni Calà
**/

public abstract class Biglietto {
    
    private String poltrona;

    protected static int id;
    
    /**
     * Blocco di inizializzazione statico
     */
    static {
        id++;
    }
    
    /**
     * Costruttore di poltrona
     * @param myPoltrona
     */
    public Biglietto(String myPoltrona) {
     
        this.poltrona = myPoltrona;
    }

  
    public String getPoltrona() {
        return poltrona;
    }
    
    
    public int getId() {
        return id;
    }
    
    /**
     * Metodo astratto 
     * @return prezzo del biglietto
     */
    public abstract float getPrezzo();
    
}
