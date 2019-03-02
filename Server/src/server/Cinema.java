package server;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;
import database.InterazioneDatabase;
import generatori.CaricaDaFile;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Giovanni Calà
**/

public class Cinema extends Thread {

    private static final String CARD_NOT_FOUND = "Tessera non trovata";
    private static final String NO_OMAGGIO = "Nessun omaggio disponibile. Non hai abbastanza punti";
    private static final String OMAGGIO_NOT_FOUND = "L'omaggio che hai scelto non è presente nella lista";
    
   
    public static final String DELIMITATORE_CAMPI = "%";

    
    public static final String PATH = "C://Users/giova/Desktop/ProgettoJava2017/";

    CaricaDaFile caricatore;
    InterazioneDatabase myDb;
    
    Scontrino scontrino;
    
    /**
     * Costruttore di Cinema
     */
    public Cinema() {
        
        caricatore = new CaricaDaFile();
        myDb = new InterazioneDatabase();
        scontrino = new Scontrino();
    }
    
    /**
     * Metodo che carica i titoli dei film in una lista
     * @return lista di oggetti Film
     */
    public List<Film> caricaTitoli() {
        
        List<String> titoli = (ArrayList)caricatore.caricaLista(PATH + "titoli");
        List<Film> listaTmpFilm = new ArrayList<Film>();
        
        Iterator<String> iter = titoli.listIterator();
        
        while(iter.hasNext()) {
            
            Film tmpFilm = caricatore.caricaFilm(PATH + iter.next());
            listaTmpFilm.add(tmpFilm);
        }

        return listaTmpFilm;
    }
    
    /**
     * Metodo che carica una mappa di punti e oggetti omaggio
     * @return mappa con chiave i punti e valore gli omaggi
     */
    public Map<Integer, Omaggio> caricaListinoOmaggi() {
        
        Map<Integer, Omaggio> listino = (HashMap)caricatore.caricaMappa(PATH + "listinoOmaggi");
        
        return listino;
    }
    
    /**
     * Metodo che carica una tessera
     * @param code
     * @return oggetto tessera con quel code
     */
    public FidelityCard caricaTessera(String code) {
        
        List<String> tessere = (ArrayList)caricatore.caricaLista(PATH + "tessere");
        
        Iterator<String> iter = tessere.listIterator();
        String card;
        
        while(iter.hasNext()) {
            
            card = iter.next();
            if(card.equals(code)) {
                
                FidelityCard tmpCard = caricatore.caricaTessera(PATH + card); 
                return tmpCard;
            }
        }
    
        return null;
    }
    
    /**
     * Metodo che confronta il codice di una tessera e ne ritorna i punti
     * @param code
     * @return i punti presenti su quella tessera
     */
    public String compareCode(String code) {
        
        List<String> tessere = (ArrayList)caricatore.caricaLista(PATH + "tessere");
        
        Iterator<String> iter = tessere.listIterator();
        String card;
        
        while(iter.hasNext()) {
            
            card = iter.next();
            
            if(card.equals(code)) {
                FidelityCard tmpCard = caricatore.caricaTessera(PATH + card);
                return String.valueOf(tmpCard.getPunti());
            }
        }
    
        return CARD_NOT_FOUND;
    }
    
    /**
     * Metodo che invia lo scontrino per un acquisto 
     * @param film
     * @param data
     * @param sala
     * @param ora
     * @param nRidotti
     * @param nBiglietti
     * @param poltrone
     * @return una stringa che contiene tutti i biglietti
     */
    public String inviaScontrino(String film, String data, String sala, String ora, 
                                String nRidotti, String nBiglietti, String poltrone) {
        
        String poltroneSenzaQuadra = poltrone.replaceAll("[\\[\\ \\]]", "");
        String[] poltroneBiglietti = poltroneSenzaQuadra.split(",");
                  
        scontrino.setNomeFilm(film);
        scontrino.setData(data);
        scontrino.setSala(Integer.parseInt(sala));
        scontrino.setOra(ora);
        
        int numIngressiGratuiti = checkIngressiGratuiti(Integer.parseInt(nBiglietti));
        
        aggiungiBiglietti(numIngressiGratuiti, poltroneBiglietti, Integer.parseInt(nBiglietti), Integer.parseInt(nRidotti));
        
        return scontrino.stampaBiglietti();
    }
    
    private void aggiungiBiglietti(int ingressiGratuiti, String[] poltrone, int numBiglietti, int numRidotti) {
        
        //Stampa i primi N biglietti gratuiti (con N numero di ingressi gratuiti)
        int i = 0;
        
        while(i < ingressiGratuiti) {
            
            scontrino.getBiglietti().add(new Gratuito(poltrone[i]));
            i++;
        }
        
        //Stampa i rimanenti biglietti
        int nInteri = numBiglietti - numRidotti;
        
        for(int j = ingressiGratuiti; j < numBiglietti; j++) {
            
            if(j < nInteri) 
                scontrino.getBiglietti().add(new Intero(poltrone[j]));
            else
                scontrino.getBiglietti().add(new Ridotto(poltrone[j]));
        }
    }
    
    private int checkIngressiGratuiti(int numBiglietti) {
        
        int ingressiGratuiti = 0;
        
        //Ottengo il numero di ingressi gratuiti
        if((scontrino.getOmaggio()) instanceof Ingresso)
            ingressiGratuiti = ((Ingresso)scontrino.getOmaggio()).getIngressi();
        
        //Controllo che il numero di ingressi sia inferiore al numero di biglietti
        if(ingressiGratuiti > numBiglietti)
            ingressiGratuiti = numBiglietti;
        
        return ingressiGratuiti;
    }
    
    /**
     * Metodo che legge gli spettacoli per un film dal database
     * @param nomeFilm
     * @return una stringa contenente tutti gli spettacoli per quel film
     * @throws SQLException
     * @throws MySQLNonTransientConnectionException
     */
    public String leggiSpettacoliFilm(String nomeFilm) throws SQLException, com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException {
        
        return myDb.leggiSpettacoli(nomeFilm);
    }
    
    /**
     * Metodo che permette di scegliere un omaggio e inserirlo nello scontrino
     * @param omaggioScelto
     * @return i punti dell'omaggio scelto come stringa
     */
    public String scegliOmaggio(String omaggioScelto) {
                
        Map<Integer, Omaggio> listino = (HashMap)caricaListinoOmaggi();
        
        for(Map.Entry<Integer, Omaggio> entry : listino.entrySet()) {

            Integer key = entry.getKey();
            Omaggio value = entry.getValue();

            if(value.getNome().toLowerCase().equals(omaggioScelto)) {
                
                scontrino.setOmaggio(value);
                return String.valueOf(key);
            }
        }
        
        return OMAGGIO_NOT_FOUND;
    }
    
    /**
     * Metodo che aggiorna i punti sulla tessera fidelity dopo l'acquisto 
     * @param quantitaPunti
     * @param cardCode
     * @return numero di punti presenti sulla carta al termine dell'acquisto
     * @throws FileNotFoundException
     */
    public String aggiornaPuntiFidelity(int quantitaPunti, String cardCode) throws FileNotFoundException {
        
        FidelityCard tmpCard = caricaTessera(cardCode);
        
        tmpCard.decrementaPunti(quantitaPunti);
        tmpCard.incrementaPunti(scontrino.getBiglietti().size());
        tmpCard.aggiorna();
        return String.valueOf(tmpCard.getPunti());
    }
    
    /**
     * Meteodo che visualizza gli omaggi riscattabili con i punti presenti sulla carta
     * @param cardCode
     * @return una stringa contenente gli omaggi disponibili
     */
    public String visualizzaOmaggi(String cardCode) {
        
        FidelityCard tmpCard = caricaTessera(cardCode);

        Map<Integer, Omaggio> listino = (HashMap)caricaListinoOmaggi();
        Map<Integer, Omaggio> omaggi;
        
        if(tmpCard != null) {
            
            omaggi = (HashMap)tmpCard.controllaOmaggio(listino, tmpCard.getPunti());
            
            if(omaggi.isEmpty()) 
                
                return NO_OMAGGIO;
            else
                
                return tmpCard.visualizzaOmaggi(omaggi); 
        }
        else 
            return CARD_NOT_FOUND;              
    }
    
    /**
     * Metodo che visualizza i posti liberi per uno spettacolo
     * @param idSpettacolo
     * @return una stringa con i posti liberi presi dal database
     * @throws SQLException
     * @throws ParseException
     */
    public String visualizzaPoltroneLibere(int idSpettacolo) throws SQLException, ParseException {
                
        if(String.valueOf(idSpettacolo).equals("0"))
            return "Non ci sono spettacoli con queste info";
        
        System.out.println("L'id dello spettacolo trovato è: " + idSpettacolo);
        
        /* Leggo i posti liberi e i posti occupati in base all'id dello spettacolo */
        String postiLiberi = myDb.getPostiLiberi(idSpettacolo, InterazioneDatabase.POSTI_LIBERI_COLUMN); 
        String finalString = "";
        
        if(postiLiberi.equals("0")) {
            
            System.out.println("Non è stata trovata nessuna riga corrispondente");
        } 
        
        finalString = DELIMITATORE_CAMPI + postiLiberi;
        return finalString.replaceFirst(DELIMITATORE_CAMPI, "");
    }
    
   
    public String visualizzaPoltroneOccupate(int idSpettacolo) throws SQLException, ParseException {
                
        if(String.valueOf(idSpettacolo).equals("0"))
            return "Non ci sono spettacoli con queste info";
        
        System.out.println("L'id dello spettacolo trovato è: " + idSpettacolo);
        
        /* Leggo i posti liberi e i posti occupati in base all'id dello spettacolo */
        String postiOccupati = myDb.getPostiOccupati(idSpettacolo, InterazioneDatabase.POSTI_OCCUPATI_COLUMN); 
        String finalString = "";
        
        if(postiOccupati.equals("0")) {
            
            System.out.println("Non è stata trovata nessuna riga corrispondente");
        } 
        
        finalString = DELIMITATORE_CAMPI + postiOccupati;
        return finalString.replaceFirst(DELIMITATORE_CAMPI, "");
    }
    

    public String getIdSpettacolo(String data, int sala, String ora) throws SQLException, ParseException {
        
        String id = myDb.getId(data, sala, ora);
        
        if(id.equals("0"))
            System.out.println("Non è stata trovata nessuna riga corrispondente");
        
        return id;
    }
    
    /**
     * Metodo che modifica le poltrone nel database
     * @param poltrone
     * @param idSpettacolo
     * @throws SQLException
     */
    public void assegnaPoltrone(String[] poltrone, int idSpettacolo) throws SQLException {
        
        myDb.modificaPosti(poltrone, idSpettacolo);
    }
    
  
    public String controllaPosti(String[] poltrone, int idSpettacolo) throws SQLException {
        
        return myDb.controllaPosti(poltrone, idSpettacolo);
    }
    
}
