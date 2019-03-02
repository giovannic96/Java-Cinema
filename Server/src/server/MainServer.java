package server;

import database.SimpleDataSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * @author Giovanni Calà
**/

public class MainServer 
{
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ParseException {

        if(args.length == 0) {
            System.out.println("Missing properties file in arguments");
            return;           
            
        } else             
            SimpleDataSource.init(args[0]); //richiamo il metodo init di SimpleDataSource per leggere il file di configurazione
        
        final int PORT = 7000;
        ServerSocket server = new ServerSocket(PORT);

        while(true) {
            
            System.out.println("Waiting for clients to connect...");
            Socket s = server.accept(); 
            Connect connection = new Connect(s); //thread
        }   
                  
    }

}
