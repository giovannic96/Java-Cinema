package client;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * @author Giovanni Calà
**/

public class Caronte {
    
    private static final String BLANK = "";
    private final static String END_TASK = "*--------------------------------*";

    final int PORT = 7000;
    
    private static Scanner scan;
    Socket s;
    InputStream in;
    OutputStream out;
    BufferedReader reader;
    PrintWriter writer;
    
    
    public Caronte() throws InterruptedException, IOException {
        
        connetti();
    }
    
    private void connetti() throws IOException, InterruptedException {
        
        boolean collegato = false;
        
        while(!collegato) {
           
            try {
                System.out.println("In attesa di connessione\n");
                scan = new Scanner(System.in);
                s = new Socket("localhost", PORT);
                in = s.getInputStream();
                out = s.getOutputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                writer = new PrintWriter(out); 
                collegato = true;
                
            } catch(ConnectException ex) {
                System.out.println("In attesa del server...\n");
                sleep(4000);
            }
            

        }
        
    }
    
    
    public void invia(String comando) {
        
        System.out.print("\nSending: \n" + comando);
        writer.print(comando);
        writer.flush();
        
    }
    
    
    public String riceviPrimaRiga() throws IOException, InterruptedException {
        
        try {
            System.out.println("\nReceiving: \n");
        
            String text;
            String str = BLANK;

            text = reader.readLine();
            System.out.println(text);

            while(!str.equals(END_TASK)) {

                str = reader.readLine();
                System.out.println(str);
            }
            return text;
        } catch(SocketException ex)  {
            
            ripulisci();
        }
        return null;
    }
    
    
    public String riceviStringaConCaratteriSpeciali() throws IOException, InterruptedException {
        
        try {
            System.out.println("\nReceiving: ");
        
            String str = BLANK;
            String finalString = BLANK;

            while (!str.equals(END_TASK)) {

                str = reader.readLine();
                finalString += str + "\n";
            }
            return finalString;
        } catch(SocketException ex)  {
            
            ripulisci();
        }
        return null;
    }
    
  
    public void ricevi() throws IOException, InterruptedException {
        
        try {
            System.out.println("\nReceiving: \n");

            String str = BLANK;

            while(!str.equals(END_TASK)) {

                str = reader.readLine();
                System.out.println(str);
            }   
        } catch(SocketException ex)  {
            
            ripulisci();
        }
    }
    

    public void riceviSenzaCaratteriSpeciali() throws IOException, InterruptedException {
        
        try {
            System.out.println("\nReceiving: ");
        
            String str = BLANK;
        
            while (!str.equals(END_TASK)) {

                str = reader.readLine();
                String finalString = str.replaceAll("[,\\[\\]]", "");
                System.out.println(finalString);
            }
        } catch(SocketException ex)  {
            
            ripulisci();
        }
    }
    
  
    public String riceviStringa() throws IOException, InterruptedException {
    
        try {
            System.out.println("\nReceiving: \n");

            String text = "";
            String str = BLANK;

            text = reader.readLine().replaceAll("[,\\ \\[\\]]", "");
            System.out.println(text);

            while(!str.equals(END_TASK)) {

                str = reader.readLine();
                System.out.println(str);
            }
            return text;        
        
        } catch(SocketException ex)  {
            
            ripulisci();
        }
        return null;
        
    }
    
    /**
     * Gestisce SocketException
     * @throws IOException
     * @throws InterruptedException 
     */
    private void ripulisci() throws IOException, InterruptedException {
        
        JOptionPane.showMessageDialog(null, "Server non trovato. Rimango in attesa di una connessione", "Errore", JOptionPane.ERROR_MESSAGE);
        s.close();
        sleep(2000);
        for(Frame frame : Frame.getFrames())
            if(!frame.equals("LOGS CLIENT") || !frame.equals("Scegli film"))
                frame.dispose();
        MainClient newClient = new MainClient(); //apri nuovo client
    }
}
