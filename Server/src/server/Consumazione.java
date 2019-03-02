package server;

/**
 * Classe figlia di Omaggio
 * @author Giovanni Calà
**/

public class Consumazione extends Omaggio {

    private String nome;
    
    public Consumazione(String nomeOmaggio) {
        
        this.nome = nomeOmaggio;
    }
    
    @Override
    public String getNome() {
        return nome;
    }

}
