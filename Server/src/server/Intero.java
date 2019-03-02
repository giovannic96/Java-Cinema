package server;

/**
 * Classe figlia di biglietto
 * @author Giovanni Calà
**/

public class Intero extends Biglietto {

    private static final float PREZZO_INTERO = 7.50f;
    
    public Intero(String myPoltrona) {
        super(myPoltrona);
    }

    @Override
    public float getPrezzo() {
        return PREZZO_INTERO;
    }
    
    @Override
    public int getId() {
        return super.getId();
    }

}
