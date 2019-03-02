package server;

/**
 * Classe figlia di Biglietto
 * @author Giovanni Calà
**/

public class Ridotto extends Biglietto {

    private static final float PREZZO_RIDOTTO = 3.50f;
    
    public Ridotto(String myPoltrona) {
        super(myPoltrona);
    }

    @Override
    public float getPrezzo() {
        return PREZZO_RIDOTTO;
    }

    @Override
    public int getId() {
        return super.getId();
    }
    
}
