package server;

/**
 * Classe figlia di Biglietto
 * @author Giovanni Calà
**/

public class Gratuito extends Biglietto {

    private static final float PREZZO_GRATUITO = 0.00f;
    
    public Gratuito(String myPoltrona) {
        super(myPoltrona);
    }

    @Override
    public float getPrezzo() {
        return PREZZO_GRATUITO;
    }
    
    @Override
    public int getId() {
        return super.getId();
    }
}
