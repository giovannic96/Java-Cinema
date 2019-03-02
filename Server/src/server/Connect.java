package server;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Giovanni Calà
**/
public class Connect extends Thread {

    Socket s;
    Cinema c;
    
    /**
     * Costruttore di Connect
     * Instaura una connessione
     * @param mySocket
     * @throws IOException
     * @throws SQLException
     * @throws ParseException
     */
    public Connect(Socket mySocket) throws IOException, SQLException, ParseException {
        
        this.s = mySocket;
        this.c = new Cinema();
        this.start();
    }
    
    @Override
    public void run() {
        
        Service service = new Service(s, c);
        
        try {
            service.doService();
            
        } catch (IOException | SQLException | ParseException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
}
