package server;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Giovanni Calà
**/

public class Scontrino {

    private static final String NOME_CINEMA = "Il Nostro Cinema";
    
    private String nomeFilm;
    private Omaggio omaggio;
    private String data;
    private String ora;
    private int sala;
    List<Biglietto> biglietti = new ArrayList<Biglietto>();
    
    public Scontrino() {
        omaggio = new Consumazione("Nessuno");
    }

   
    public void setSala(int sala) {
        this.sala = sala;
    }

    
    public void setData(String data) {
        this.data = data;
    }
  
    public void setOra(String ora) {
        this.ora = ora;
    }

   
    public void setOmaggio(Omaggio myOmaggio) {
        this.omaggio = myOmaggio;
    }
    
    
    public Omaggio getOmaggio() {
        return omaggio;
    }

    
    public List<Biglietto> getBiglietti() {
        return biglietti;
    }

   
    public void setNomeFilm(String nomeFilm) {
        this.nomeFilm = nomeFilm;
    }
    
    /**
     * Stampa i biglietti di un acquisto uno per volta
     * @return tutti i biglietti come stringa
     */
    public String stampaBiglietti() {
        
        String finalString = "";
        
        for(Biglietto b : getBiglietti()) {
             
            finalString += "\n" + stampaBiglietto(b);
        }
        return finalString + "\n\n\nOMAGGIO: " + omaggio.getNome() + "\n\nPREZZO TOTALE: \n" + stampaPrezzoFinale() + "€\n";
    }
    
    private String stampaBiglietto(Biglietto myBiglietto) {
        
        return "\n\n\t\t" + NOME_CINEMA + "\nBiglietto n° " + myBiglietto.getId() + "\nTitolo: " + nomeFilm + 
                "\nImporto: " + String.format("%.2f", myBiglietto.getPrezzo()) + "€" + "\nData: " + data + "\nOra: " +
                ora + "\nSala: " + sala + "\nPoltrona: " + myBiglietto.getPoltrona();
    }
    
    private String stampaPrezzoFinale() {
        
        float prezzoTotale = 0;
        
        for(Biglietto b : getBiglietti()) {
            
            prezzoTotale += b.getPrezzo();
        }
        return String.valueOf(String.format("%.2f", prezzoTotale));
    }
}
