package client;

/**
 * @author Giovanni Calà
**/

public class SpettacoloTracker {

    private String sala;
    private String ora;
    private String data;
    
    public SpettacoloTracker(String sala, String ora, String data) {
        
        this.sala = sala;
        this.ora = ora;
        this.data = data;
    }

    public String getSala() {
        return sala;
    }

    public String getOra() {
        return ora;
    }

    public String getData() {
        return data;
    }
    
    
}
