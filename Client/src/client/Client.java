package client;

import java.io.IOException;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Giovanni Calà
**/

public class Client {

    private static final String SEPARATORE = "*";
    public static final String CARD_NOT_FOUND = "Tessera non trovata";
    public static final String NO_OMAGGIO = "Nessun omaggio disponibile. Non hai abbastanza punti";
    public static final String OMAGGIO_NOT_FOUND = "L'omaggio che hai scelto non è presente nella lista";
    
    Caronte caronte;
    Scanner scan;
    
    int detrazionePunti;
    String codiceFidelity;    
    String idSpettTmp;
    
    /*************************** DATI SCONTRINO TEMPORANEO ****************/
    String filmTmp;
    String dataTmp;
    int salaTmp;
    String oraTmp;
    int numRidottiTmp;
    int nBigliettiTmp;
    List<String> poltroneTmp;
    /*********************************************************************/
    
    public Client() throws InterruptedException, IOException {
        
        caronte = new Caronte();
        scan = new Scanner(System.in);
        detrazionePunti = 0;
        codiceFidelity = "";
    }

    //METODI GETTERS

    
    public String getFilmTmp() {
        return filmTmp;
    }


    public String getDataTmp() {
        return dataTmp;
    }

 
    public int getSalaTmp() {
        return salaTmp;
    }

   
    public String getOraTmp() {
        return oraTmp;
    }

   
    public int getNumRidottiTmp() {
        return numRidottiTmp;
    }

 
    public int getnBigliettiTmp() {
        return nBigliettiTmp;
    }

  
    public List<String> getPoltroneTmp() {
        return poltroneTmp;
    }
    

    public int getDetrazionePunti() {
        return this.detrazionePunti;
    }
    
 
    public String getCodiceFidelity() {
        return this.codiceFidelity;
    }
    
    /**
     * Invia tramite Caronte la stringa tokenizzata per la creazione dello scontrino nel server in base
     * ai dati inseriti
     * @return stringa contenente tutti i dati dello scontrino
     * @throws IOException
     * @throws InterruptedException
     */
    public String inviaScontrinoTemporaneo() throws IOException, InterruptedException {
        
        String command = "INVIA SCONTRINO TEMP" + SEPARATORE + filmTmp + SEPARATORE + dataTmp + SEPARATORE + 
                        String.valueOf(salaTmp) + SEPARATORE + oraTmp + SEPARATORE + String.valueOf(numRidottiTmp) + 
                        SEPARATORE + String.valueOf(nBigliettiTmp) + SEPARATORE + poltroneTmp.toString() + SEPARATORE + "\n";
        
        caronte.invia(command);
        return caronte.riceviStringaConCaratteriSpeciali();  
    }

    /**
     * Invia tramite Caronte la stringa tokenizzata per controllare se i posti scelti dall'utente sono ancora liberi 
     * nel database
     * @return vero o falso
     * @throws IOException
     * @throws InterruptedException
     */
    public String controllaPosti() throws IOException, InterruptedException {
        
        String poltroneSelezionateTokenized =  Arrays.toString(poltroneTmp.toArray(new String[poltroneTmp.size()])).replaceAll("[,\\[\\]]", "").replaceAll(" ", SEPARATORE);
        String command = "CONTROLLA POSTI" + SEPARATORE + idSpettTmp + SEPARATORE + poltroneSelezionateTokenized + SEPARATORE + "\n";
        
        caronte.invia(command);
        return caronte.riceviPrimaRiga();
    }
    
    /**
     * Invia tramite Caronte la stringa tokenizzata per aggiornare i punti della Fidelity
     * @param quantita
     * @param codice
     * @return una stringa con i nuovi punti della tessera
     * @throws IOException
     * @throws InterruptedException
     */
    public String aggiornaPuntiFidelity(int quantita, String codice) throws IOException, InterruptedException {
        
        String command;
        
        if(!codice.equals(""))
            command = "AGGIORNA PUNTI" + SEPARATORE + String.valueOf(quantita) + SEPARATORE + codice + SEPARATORE + "\n";
        else 
            return "0";
        
        caronte.invia(command);
        return caronte.riceviPrimaRiga();
    }
    
    /**
     * Invia tramite Caronte la stringa tokenizzata per la visualizzazione degli omaggi per quella tessera
     * @param theCode il codice della tessera
     * @return una stringa degli omaggi
     * @throws IOException
     * @throws InterruptedException
     */
    public String visualizzaOmaggi(String theCode) throws IOException, InterruptedException {
        
        String command = "VISUALIZZA OMAGGI" + SEPARATORE + theCode + SEPARATORE + "\n";
        
        codiceFidelity = theCode;
        
        caronte.invia(command);
        return caronte.riceviStringaConCaratteriSpeciali();
    }
    
    /**
     * Invia tramite Caronte la stringa tokenizzata per ottenere il numero di punti dell'omaggio scelto
     * @param omaggio
     * @return i punti dell'omaggio scelto
     * @throws IOException
     * @throws InterruptedException
     */
    public String scegliOmaggio(String omaggio) throws IOException, InterruptedException {
        
        String command = "SCEGLI OMAGGIO" + SEPARATORE + omaggio.toLowerCase() + SEPARATORE + "\n";
        
        caronte.invia(command);
        String puntiOmaggio = caronte.riceviPrimaRiga(); 
        
        if(!puntiOmaggio.equals(OMAGGIO_NOT_FOUND))
            detrazionePunti = Integer.parseInt(puntiOmaggio);
        
        return puntiOmaggio;
    }
    
    /**
     * Invia tramite Caronte la stringa tokenizzata per controllare se esiste la tessera fidelity nel Server
     * @param theCode
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String controllaCarta(String theCode) throws IOException, InterruptedException {
        
        String command = "CONTROLLA FIDELITY" + SEPARATORE + theCode + SEPARATORE + "\n";
        
        caronte.invia(command);        
        return caronte.riceviPrimaRiga();
    }
    
    /**
     * Invia tramite Caronte la stringa tokenizzata per ottenere l'ID dello spettacolo
     * @param data
     * @param sala
     * @param ora
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String ottieniId(String data, String sala, String ora) throws IOException, InterruptedException {
        
        String command = "OTTIENI ID" + SEPARATORE + data + SEPARATORE + sala + SEPARATORE + ora + SEPARATORE + "\n";
        
        caronte.invia(command);
        idSpettTmp = caronte.riceviPrimaRiga();
        
        return idSpettTmp;
    }
    
    /**
     * Conferma i posti scelti nello scontrino temporaneo
     * @param postiScelti
     * @param nRidotti
     * @param idSpett
     * @throws IOException
     */
    public void confermaPosti(List<String> postiScelti, int nRidotti, int idSpett) throws IOException {
        
        //Imposto i dati dello scontrino temporaneo
        poltroneTmp = postiScelti;
        nBigliettiTmp = postiScelti.size();
        numRidottiTmp = nRidotti;        
    }
    
    /**
     * Invia tramite Caronte la stringa tokenizzata per la modifica dei posti nel database
     * @throws IOException
     * @throws InterruptedException
     */
    public void modificaPosti() throws IOException, InterruptedException {
        
        String poltroneSelezionateTokenized =  Arrays.toString(poltroneTmp.toArray(new String[poltroneTmp.size()])).replaceAll("[,\\[\\]]", "").replaceAll(" ", SEPARATORE);
        String command = "MODIFICA POSTI" + SEPARATORE + idSpettTmp + SEPARATORE + poltroneSelezionateTokenized + SEPARATORE + "\n";
        
        caronte.invia(command);
        caronte.ricevi();
    }

    /**
     * Invia tramite Caronte la stringa tokenizzata per la visualizzazione dei posti occupati
     * @param sala
     * @param ora
     * @param data
     * @return lista di stringhe
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public List<String> riceviPostiOccupati(String sala, String ora, String data) throws IOException, ParseException, InterruptedException {
                          
        String command = "VISUALIZZA POSTI OCCUPATI" + SEPARATORE + data + SEPARATORE + sala + SEPARATORE + 
                            ora + SEPARATORE + "\n";
        
        caronte.invia(command);
        String infoPosti = caronte.riceviStringa();
        
        //Imposto i dati dello scontrino temporaneo
        dataTmp = data;
        salaTmp = Integer.parseInt(sala);
        oraTmp = ora;
               
        return getSplittedPostiList(infoPosti);
    }
    
    /**
     * Invia tramite Caronte la stringa tokenizzata per la visualizzazione posti liberi
     * @param sala
     * @param ora
     * @param data
     * @return lista di stringhe
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public List<String> riceviPostiLiberi(String sala, String ora, String data) throws IOException, ParseException, InterruptedException {
                          
        String command = "VISUALIZZA POSTI LIBERI" + SEPARATORE + data + SEPARATORE + sala + SEPARATORE + 
                            ora + SEPARATORE + "\n";
        
        caronte.invia(command);
        String infoPosti = caronte.riceviStringa();
        
        //Imposto i dati dello scontrino temporaneo
        dataTmp = data;
        salaTmp = Integer.parseInt(sala);
        oraTmp = ora;
               
        return getSplittedPostiList(infoPosti);
    }
    
    /**
     * Invia tramite Caronte la stringa tokenizzata per la visualizzazione degli spettacoli
     * @param film
     * @return lista di stringhe
     * @throws IOException
     * @throws InterruptedException
     * @throws SocketException
     */
    public List<SpettacoloTracker> scegliFilm(String film) throws IOException, InterruptedException, SocketException {
        
        String command = "VISUALIZZA SPETTACOLI" + SEPARATORE + film + "\n";

        caronte.invia(command);
        String infoSpettacoli = caronte.riceviStringa();
       
        //Imposto i dati dello scontrino temporaneo
        filmTmp = film;
        
        return getSplittedSpettacoliList(infoSpettacoli);
    }

    /**
     *Invia tramite Caronte la stringa tokenizzata per la visualizzazione di tutti i film
     * @return una lista di stringhe
     * @throws IOException
     * @throws InterruptedException
     * @throws SocketException
     */
    public List<FilmTracker> visualizzaTuttiIFilm() throws IOException, InterruptedException, SocketException {
        
        String command = "INFO" + SEPARATORE + "film\n";
        
        caronte.invia(command);   
        
        String infoFilm = caronte.riceviStringa();
        
        return getSplittedList(infoFilm);
        
    }
    
    private List<String> getSplittedPostiList(String s) {
        
        String[] campi = s.split("\\%");

        List<String> list = Arrays.asList(campi);
        
        return list;
    }
    
    private List<FilmTracker> getSplittedList(String s) {
        
        List<FilmTracker> list = new ArrayList<FilmTracker>();
        
        FilmTracker film1 = null;
        FilmTracker film2 = null;
        FilmTracker film3 = null;
        
        String[] campi = s.split("\\%");
        String[] campiFilm1 = { campi[0], campi[1], campi[2], campi[3], campi[4], campi[5] };
        String[] campiFilm2 = { campi[6], campi[7], campi[8], campi[9], campi[10], campi[11] };
        String[] campiFilm3 = { campi[12], campi[13], campi[14], campi[15], campi[16], campi[17] };

        for(int i = 0; i < campi.length; i = i+5) {
            
            if(i < 6)
                film1 = new FilmTracker(campiFilm1[0], campiFilm1[1], campiFilm1[2], campiFilm1[3], campiFilm1[4], campiFilm1[5]);
            else if(i > 5 && i < 12) 
                film2 = new FilmTracker(campiFilm2[0], campiFilm2[1], campiFilm2[2], campiFilm2[3], campiFilm2[4], campiFilm2[5]);
            else
                film3 = new FilmTracker(campiFilm3[0], campiFilm3[1], campiFilm3[2], campiFilm3[3], campiFilm3[4], campiFilm3[5]);
        }
        
        list.add(film1);
        list.add(film2);
        list.add(film3);
        
        return list;
    }
    
    private List<SpettacoloTracker> getSplittedSpettacoliList(String s) {
        
        List<SpettacoloTracker> list = new ArrayList<SpettacoloTracker>();
        
        SpettacoloTracker spettacolo1 = null;
        SpettacoloTracker spettacolo2 = null;
        SpettacoloTracker spettacolo3 = null;
        SpettacoloTracker spettacolo4 = null;
        SpettacoloTracker spettacolo5 = null;
        SpettacoloTracker spettacolo6 = null;
        
        String[] campi = s.split("\\%");
        String[] campiSpettacolo1 = { campi[0], campi[1], campi[2] };
        String[] campiSpettacolo2 = { campi[3], campi[4], campi[5] };
        String[] campiSpettacolo3 = { campi[6], campi[7], campi[8] };
        String[] campiSpettacolo4 = { campi[9], campi[10], campi[11] };
        String[] campiSpettacolo5 = { campi[12], campi[13], campi[14] };
        String[] campiSpettacolo6 = { campi[15], campi[16], campi[17] };

        for(int i = 0; i < campi.length; i = i+2) {
            
            if(i < 3)
                spettacolo1 = new SpettacoloTracker(campiSpettacolo1[0], campiSpettacolo1[1], campiSpettacolo1[2]);
            else if(i > 2 && i < 6) 
                spettacolo2 = new SpettacoloTracker(campiSpettacolo2[0], campiSpettacolo2[1], campiSpettacolo2[2]);
            else if(i > 5 && i < 9)
                spettacolo3 = new SpettacoloTracker(campiSpettacolo3[0], campiSpettacolo3[1], campiSpettacolo3[2] );
            else if(i > 8 && i < 12)
                spettacolo4 = new SpettacoloTracker(campiSpettacolo4[0], campiSpettacolo4[1], campiSpettacolo4[2] );
            else if(i > 11 && i < 15)
                spettacolo5 = new SpettacoloTracker(campiSpettacolo5[0], campiSpettacolo5[1], campiSpettacolo5[2] );
            else
                spettacolo6 = new SpettacoloTracker(campiSpettacolo6[0], campiSpettacolo6[1], campiSpettacolo6[2] );
        }
        
        list.add(spettacolo1);
        list.add(spettacolo2);
        list.add(spettacolo3);
        list.add(spettacolo4);
        list.add(spettacolo5);
        list.add(spettacolo6);
        
        return list;
    }
    
}