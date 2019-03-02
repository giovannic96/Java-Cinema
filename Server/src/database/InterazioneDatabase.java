package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import server.Cinema;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Giovanni Calà
**/

public class InterazioneDatabase 
{
    public static final String POSTI_LIBERI_COLUMN = "posti_liberi";
    public static final String POSTI_OCCUPATI_COLUMN = "posti_occupati";
    public static final String POSTI_GIA_OCCUPATI = "Posti già occupati";
    public static final String POSTI_LIBERI = "Posti liberi";
    
    public String leggiSpettacoli(String theFilm) throws SQLException, com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException {
        
        Connection conn = SimpleDataSource.getConnection(); //ottengo la connessione al database
        String finalString = "";

        try {
            
            String query = "SELECT sala, ora, data FROM spettacoli WHERE titolo = '" + theFilm + "'"; //creo una query
            PreparedStatement stat = conn.prepareStatement(query); //la preparo
            ResultSet result = stat.executeQuery(); //ottengo la tabella dei risultati una volta eseguita la query
            
            while(result.next()) { //finchè ci sono altri record
                
                finalString += (String)result.getString(1) + Cinema.DELIMITATORE_CAMPI + (String)result.getString(2) + 
                                Cinema.DELIMITATORE_CAMPI + (String)result.getString(3) + Cinema.DELIMITATORE_CAMPI;
            }
            
        } catch (SQLException e) {
            
            System.out.println("Violazione vincolo primary key: impossibile modificare il database");            
            
        } finally {
            
            conn.close(); //NON DIMENTICHIAMO DI CHIUDERE LA CONNESSIONE QUANDO ABBIAMO FINITO
        }
        
        return finalString;
    
    }
    
    /* OVERLOAD */
    public int leggiSpettacoli(String theData, int theSala, String theOra) throws SQLException, ParseException {
        
        Connection conn = SimpleDataSource.getConnection(); //ottengo la connessione al database
        
        int resultId = 0;
        Date theDataFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(theData);
        java.sql.Date sqlDate = new java.sql.Date(theDataFormatted.getTime()); //convert data to sqlData
        Date time = new SimpleDateFormat("HH:mm").parse(theOra);
        java.sql.Time sqlTime = new java.sql.Time(time.getTime()); //convert time to sqlTime
        
        try {
            
            String query = "SELECT id FROM spettacoli WHERE data = '" + sqlDate + "' AND sala = " + 
                        theSala + " AND ora = '" + sqlTime + "'"; //creo una query
            System.out.println("Query: " + query);
            PreparedStatement stat = conn.prepareStatement(query); //la preparo
            ResultSet result = stat.executeQuery(); //ottengo la tabella dei risultati una volta eseguita la query
            
            if(result.next()) { //se è stata trovata la riga 
                resultId = result.getInt(1);
            } else 
                resultId = 0;
            
        } catch (SQLException e) {

            e.printStackTrace();
            
        } finally {
            
            conn.close(); //NON DIMENTICHIAMO DI CHIUDERE LA CONNESSIONE QUANDO ABBIAMO FINITO
        }
        
        return resultId;            
    }
    
    public String getId(String theData, int theSala, String theOra) throws SQLException, ParseException {
        
        Connection conn = SimpleDataSource.getConnection(); //ottengo la connessione al database
        
        Date theDataFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(theData);
        java.sql.Date sqlDate = new java.sql.Date(theDataFormatted.getTime()); //convert data to sqlData
        Date time = new SimpleDateFormat("HH:mm").parse(theOra);
        java.sql.Time sqlTime = new java.sql.Time(time.getTime()); //convert time to sqlTime    

        try {
            
            String query = "SELECT id FROM spettacoli WHERE data = '" + sqlDate + "' AND sala = " + 
                        theSala + " AND ora = '" + sqlTime + "'"; //creo una query
            System.out.println("Query: " + query);
            PreparedStatement stat = conn.prepareStatement(query); //la preparo
            ResultSet result = stat.executeQuery(); //ottengo la tabella dei risultati una volta eseguita la query
   
            if(result.next()) { //se è stata trovata la riga 
                
                return result.getString(1);
            }             
        } catch (SQLException e) {

            e.printStackTrace();
            
        } finally {
            
            conn.close(); //NON DIMENTICHIAMO DI CHIUDERE LA CONNESSIONE QUANDO ABBIAMO FINITO
        }            
        return "0";
    }
    
    public String controllaPosti(String[] thePoltrone, int theId) throws SQLException {
        
        Connection conn = SimpleDataSource.getConnection();

        try {
            
            String postiOccupati = getPostiOccupati(theId, POSTI_OCCUPATI_COLUMN);
            
            for(String poltrona : thePoltrone) {
                if(postiOccupati.toLowerCase().indexOf(poltrona.toLowerCase()) != -1 ) //è già occupato
                    return POSTI_GIA_OCCUPATI;
            }
            
        } catch (SQLException e) {
            
            e.printStackTrace();            
        } finally {
            
            conn.close(); //NON DIMENTICHIAMO DI CHIUDERE LA CONNESSIONE QUANDO ABBIAMO FINITO
        }    
        return POSTI_LIBERI;
    }
    
    public void modificaPosti(String[] thePoltrone, int theId) throws SQLException {
        
        Connection conn = SimpleDataSource.getConnection();
        
        try {

            /* Ottengo la nuova stringa di posti liberi */
            String postiLiberi = getPostiLiberi(theId, POSTI_LIBERI_COLUMN);

            String nuoviPostiLiberi = getNuoviPostiLiberi(postiLiberi, thePoltrone);

            /* Ottengo la nuova stringa di posti occupati */
            String postiOccupati = getPostiOccupati(theId, POSTI_OCCUPATI_COLUMN);

            String nuoviPostiOccupati = getNuoviPostiOccupati(postiOccupati, thePoltrone);

            System.out.println("Nuovi posti liberi: \n" + nuoviPostiLiberi);
            System.out.println("Nuovi posti occupati: \n" + nuoviPostiOccupati);

            /* Aggiorno il database con le nuove stringhe sopra ottenute */
            aggiornaPosti(conn, nuoviPostiLiberi, POSTI_LIBERI_COLUMN, theId);
            aggiornaPosti(conn, nuoviPostiOccupati, POSTI_OCCUPATI_COLUMN, theId);
           
            System.out.println("Aggiornamento concluso");
        
        } catch (SQLException e) {
            
            e.printStackTrace();            
        } finally {
            
            conn.close(); //NON DIMENTICHIAMO DI CHIUDERE LA CONNESSIONE QUANDO ABBIAMO FINITO
        }    
    }
    
    public synchronized void aggiornaPosti(Connection myConn, String nuoviPosti, String columnName, int id) throws SQLException {
        
        try {       
            String query = "UPDATE spettacoli SET " + columnName + " = '" + nuoviPosti + "' WHERE id = " + id;
            PreparedStatement stat = myConn.prepareStatement(query);
            stat.executeUpdate(query);
            
        } catch(SQLException e) {
            
            e.printStackTrace();
        }        
    }

    public String getPostiLiberi(int id, String columnName) throws SQLException{
        
        Connection conn = SimpleDataSource.getConnection();

        String postiIniziali = "";
        
        try {
            String query = "SELECT " + columnName + " FROM spettacoli WHERE id = " + id;
            PreparedStatement stat = conn.prepareStatement(query);
            ResultSet result = stat.executeQuery();
        
            if(result.next()) {
                postiIniziali = result.getString(1);
            }
            
        } catch(SQLException ex) {
            
            ex.printStackTrace();  
        } finally {
            
            conn.close();
        }
        
        return postiIniziali;
    }
    
    public String getPostiOccupati(int id, String columnName) throws SQLException{
        
        Connection conn = SimpleDataSource.getConnection();

        String postiIniziali = "";
        
        try {
            String query = "SELECT " + columnName + " FROM spettacoli WHERE id = " + id;
            PreparedStatement stat = conn.prepareStatement(query);
            ResultSet result = stat.executeQuery();
        
            if(result.next()) {
                postiIniziali = result.getString(1);
            }
            
        } catch(SQLException ex) {
            
            ex.printStackTrace();  
        } finally {
            
            conn.close();
        }
        
        return postiIniziali;
    }
    
    private String getNuoviPostiLiberi(String thePostiLiberi, String[] thePoltrone) throws SQLException {
        
        List<String> postiLiberiTokenized = new ArrayList<String>();
        StringTokenizer tokenizer;
            
        tokenizer = new StringTokenizer(thePostiLiberi, Cinema.DELIMITATORE_CAMPI);

        while(tokenizer.hasMoreTokens())
            postiLiberiTokenized.add(tokenizer.nextToken()); //tokenizzo i posti liberi iniziali
        
        for(String poltrona : thePoltrone) {
                
            for(int i = 0; i < postiLiberiTokenized.toArray().length; i++) {

                if(postiLiberiTokenized.get(i).equals(poltrona)) 
                    postiLiberiTokenized.remove(i); //rimuovo dalla lista dei posti liberi quelli che l'utente vuole occupare
            }
        }
        
        String nuoviPostiLiberi = "";
        Iterator iter = postiLiberiTokenized.iterator();
        
        while(iter.hasNext()) {
                nuoviPostiLiberi += Cinema.DELIMITATORE_CAMPI + iter.next(); //ricreo la nuova stringa di posti liberi da mettere nel database
            }
        
        return nuoviPostiLiberi.replaceFirst(Cinema.DELIMITATORE_CAMPI, "");
    }
    
    private String getNuoviPostiOccupati(String thePostiOccupati, String[] thePoltrone) throws SQLException {
        
        List<String> postiOccupatiTokenized = new ArrayList<String>();
        StringTokenizer tokenizer;
            
        tokenizer = new StringTokenizer(thePostiOccupati, Cinema.DELIMITATORE_CAMPI);

        while(tokenizer.hasMoreTokens())
            postiOccupatiTokenized.add(tokenizer.nextToken()); //tokenizzo i posti occupati iniziali
        
        for(String poltrona : thePoltrone) {            
            
            postiOccupatiTokenized.add(poltrona); //aggiungo alla lista dei posti occupati quelli che l'uente vuole occupare
        }
        
        String nuoviPostiOccupati = "";
        Iterator iter = postiOccupatiTokenized.iterator();
        
        while(iter.hasNext()) {
            nuoviPostiOccupati += Cinema.DELIMITATORE_CAMPI + iter.next(); //ricreo la nuova stringa di posti occupati da mettere nel database
        }
        
        return nuoviPostiOccupati.replaceFirst(Cinema.DELIMITATORE_CAMPI, "");
    }
    
}
