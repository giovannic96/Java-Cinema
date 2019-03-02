package server;

import generatori.GeneratoreFidelity;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Giovanni Calà
**/

public class FidelityCard implements Serializable {

    private static final String DEFAULT_CODE = "Unknown";
    private static final int MAX_PUNTI = 300;
    
    private GeneratoreFidelity saver;
    private String code;
    private int punti;
    
   
    public FidelityCard() {
        
        this(DEFAULT_CODE, 0);
    }
    
   
    public FidelityCard(String myCode, int myPunti) {
        
        this.code = myCode;
        this.punti = myPunti;
        this.saver = new GeneratoreFidelity();
    }
    
   
    public String getCode() {
        return code;
    }
    
  
    public void decrementaPunti(int number) {
        punti = punti - number;
    }
   
 
    public void incrementaPunti(int quantita) {
        
        if(punti >= MAX_PUNTI)
            System.out.println("Hai raggiunto il limite massimo di punti");
        else
            punti += quantita;
    }
    
  
    public int getPunti() {
        return punti;
    }
    
    /**
     * Metodo che aggiorna una tessera Fidelity nel file  
     * @throws FileNotFoundException
     */
    public void aggiorna() throws FileNotFoundException {
        
        saver.salvaFidelity(Cinema.PATH + getCode(), this);
    }
    
    /**
     * Metodo che controlla quali omaggi sono disponibili con i punti presenti su una carta
     * @param listino
     * @param puntiCarta
     * @return una mappa di punti (chiave) e oggetti omaggio (valore)
     */
    public Map<Integer, Omaggio> controllaOmaggio(Map<Integer, Omaggio> listino, int puntiCarta) {
        
        Map<Integer, Omaggio> tmpMap = new HashMap<Integer, Omaggio>();
        boolean esisteOmaggio = false;
        
        for(Map.Entry<Integer, Omaggio> entry : listino.entrySet()) {

            Integer key = entry.getKey();
            Omaggio value = entry.getValue();

            if(key <= puntiCarta) {

                tmpMap.put(key, value);
                esisteOmaggio = true;
            }
        }
        return tmpMap;
    }
    
   
    public String visualizzaOmaggi(Map<Integer, Omaggio> myOmaggi) {
        
        String finalString = "";
        
        for(Map.Entry<Integer, Omaggio> entry : myOmaggi.entrySet()) {
            
            finalString += entry.getValue().getNome() + "(" + entry.getKey() + " punti)" + "\n";
        }
        return finalString;
    }
    
    
}
