package server;

/**
 * Classe figlia di Omaggio
 * @author Giovanni Calà
**/

public class Ingresso extends Omaggio {

    private String nome;
    private int numIngressi;
    
   
    public Ingresso(String nomeOmaggio, int nIngressi) {
        
        this.nome = nomeOmaggio;
        this.numIngressi = nIngressi;
    }
    
   
    @Override
    public String getNome() {
        return nome;
    }
    
    
    public int getIngressi() {
        return numIngressi;
    }
}
