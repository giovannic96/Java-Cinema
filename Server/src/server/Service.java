package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 * @author Giovanni Calà
**/
public class Service {

    public final static String END_TASK = "*--------------------------------*";
    private static final String BLANK = "";
    
    private Socket s;
    private Cinema cinema;
    
    /**
       Constructs a service object that processes commands
       from a socket for a cinema.
       @param aSocket the socket
     * @param aCinema the cinema
    */
    public Service(Socket aSocket, Cinema aCinema)
    {
       s = aSocket;
       cinema = aCinema;
    }
 
    /**
       Executes all commands until the QUIT command or the
       end of input.
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
    */      
    public void doService() throws IOException, SQLException, ParseException
    {
       BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
       PrintWriter out = new PrintWriter(s.getOutputStream());
       
       while (true)
       {  
            String line = in.readLine();
            
            System.out.println("Received: " + line + "\n");
            if (line == null || line.equals("QUIT")) 
               break;

            String response = "";
            try {
                response = executeCommand(line);

            } catch(com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException e) {
                System.out.println("\nDatabase non connesso\n\n");
                JOptionPane.showMessageDialog(null, "Database non connesso. Riprovare più tardi", "Errore database", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            System.out.println("\nSending: \n" + response);
            out.println(response);
            out.println(END_TASK);
            out.flush(); 
       }
    }
 
    /**
       Executes a single command.
       @param line the command
       @return the reply to send to the client.
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     * @throws java.io.FileNotFoundException
    */
    public String executeCommand(String line) throws SQLException, ParseException, FileNotFoundException
    {
        StringTokenizer tokenizer = new StringTokenizer(line, "*");
        
        String command = tokenizer.nextToken();
        
        switch (command) {
            
            case "INFO":
            {
                List<Film> listaFilmTmp = (ArrayList)cinema.caricaTitoli();
                
                return listaFilmTmp.toString() + "\n";
            }
            case "VISUALIZZA SPETTACOLI":
            {
                String nomeFilm = tokenizer.nextToken();
                String infoSpettacoli = BLANK;
                
                infoSpettacoli = cinema.leggiSpettacoliFilm(nomeFilm);
                    
                return infoSpettacoli;
            }
            case "VISUALIZZA POSTI LIBERI":
            {
                String dataToken = tokenizer.nextToken();
                String salaToken = tokenizer.nextToken();
                String oraToken = tokenizer.nextToken();
                
                String infoPoltrone = BLANK;
                
                int id = Integer.parseInt(cinema.getIdSpettacolo(dataToken, Integer.parseInt(salaToken), oraToken));
                infoPoltrone = cinema.visualizzaPoltroneLibere(id);
                                    
                return infoPoltrone;
            }
            case "VISUALIZZA POSTI OCCUPATI":
            {
                String dataToken = tokenizer.nextToken();
                String salaToken = tokenizer.nextToken();
                String oraToken = tokenizer.nextToken();
                
                String infoPoltrone = BLANK;
                
                int id = Integer.parseInt(cinema.getIdSpettacolo(dataToken, Integer.parseInt(salaToken), oraToken));
                infoPoltrone = cinema.visualizzaPoltroneOccupate(id);
                                    
                return infoPoltrone;
            }
            case "CONTROLLA POSTI":
            {
                int numberOfSeats = tokenizer.countTokens();
                String[] postiDaControllare = new String[numberOfSeats-1]; //perchè il primo token mi da l id dello spettacolo non i posti (quindi non lo considero)
                int idSpettacolo = Integer.parseInt(tokenizer.nextToken());
                int i = 0;
                
                while(i < numberOfSeats-1) {
                    postiDaControllare[i] = tokenizer.nextToken();
                    i++;
                }
                
                return cinema.controllaPosti(postiDaControllare, idSpettacolo);
            }
            case "MODIFICA POSTI":
            {
                int numberOfSeats = tokenizer.countTokens();
                String[] postiDaOccupare = new String[numberOfSeats-1]; //perchè il primo token mi da l id dello spettacolo non i posti (quindi non lo considero)
                int idSpettacolo = Integer.parseInt(tokenizer.nextToken());
                int i = 0;
                
                while(i < numberOfSeats-1) {
                    postiDaOccupare[i] = tokenizer.nextToken();
                    i++;
                }
                
                cinema.assegnaPoltrone(postiDaOccupare, idSpettacolo);
                return "Posti assegnati con successo\n";
            }   
            case "OTTIENI ID":
            {  
                String dataToken = tokenizer.nextToken();
                String salaToken = tokenizer.nextToken();
                String oraToken = tokenizer.nextToken();
                
                String idSpettacolo = cinema.getIdSpettacolo(dataToken, Integer.parseInt(salaToken), oraToken);
                return idSpettacolo;
            }
            case "CONTROLLA FIDELITY":
            {  
                String cardCode = tokenizer.nextToken();
                String comparison = cinema.compareCode(cardCode);
                
                return comparison;
            }
            case "VISUALIZZA OMAGGI":
            {
                String cardCode = tokenizer.nextToken();
                return cinema.visualizzaOmaggi(cardCode);             
            }
            case "SCEGLI OMAGGIO":
            {
                String omaggio = tokenizer.nextToken();
                
                return cinema.scegliOmaggio(omaggio);
            }
            case "AGGIORNA PUNTI":
            {
                String quantita = tokenizer.nextToken();
                String codice = tokenizer.nextToken();
                
                return cinema.aggiornaPuntiFidelity(Integer.parseInt(quantita), codice);

            }
            case "INVIA SCONTRINO TEMP":
            {
                String film = tokenizer.nextToken();
                String data = tokenizer.nextToken();
                String sala = tokenizer.nextToken();
                String ora = tokenizer.nextToken();
                String nRidotti = tokenizer.nextToken();
                String nBiglietti = tokenizer.nextToken();
                String poltrone = tokenizer.nextToken();
                           
                return cinema.inviaScontrino(film, data, sala, ora, nRidotti, nBiglietti, poltrone);
            }
            default:
                break;
        
        }
        return command;
    }

}
