package database;

import com.mysql.jdbc.Connection;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Giovanni Calà
**/

public class SimpleDataSource {

    private static String url;
    private static String username;
    private static String password;
    
    public static void init(String filename) throws IOException, ClassNotFoundException {
        
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(filename);
        props.load(in); //carico il file .properties
        
        String driver = props.getProperty("jdbc.driver"); //getProperty mi restituisce il valore della chiave passata come parametro
        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        if(username == null) username = "";
        password = props.getProperty("jdbc.password");
        if(password == null) password = "";
        
        if(driver != null) 
            //è necessario forzare il caricamento della classe che rappresenta il driver opportuno tramite il metodo "forName" della classe "Class"
            Class.forName(driver); 
    }
    
    public static Connection getConnection() throws SQLException {
        
        //stabilisco la connessione tra l'applicazione Java e il database tramite "getConnection()" della classe "DriverManager"
        return (Connection) DriverManager.getConnection(url, username, password);
    }
}
