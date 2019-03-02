package generatori;

import server.Film;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.FidelityCard;

/**
 * @author Giovanni Calà
**/

public class CaricaDaFile 
{
    private static final String PATH = "C://Users/giova/Desktop/ProgettoJava2017/";
    
    public static void main(String[] args) {
        
        CaricaDaFile caricatore = new CaricaDaFile();
        
        Film harryPotter = caricatore.caricaFilm(PATH + "harryPotter");
        Film avatar = caricatore.caricaFilm(PATH + "avatar");
        Film theRing = caricatore.caricaFilm(PATH + "theRing");
        
        List<String> titoli = (ArrayList)caricatore.caricaLista(PATH + "titoli");
        
        try {
            
            System.out.println(harryPotter.toString());
            System.out.println(avatar.toString());
            System.out.println(theRing.toString());
            System.out.println(titoli.toString());
            
        } catch (NullPointerException e) {
    
            System.out.println("Non posso leggere nessun film, probabilmente il file non esiste o è stato modificato");
        }
        
    }
    
    public Film caricaFilm(String filePath) {

        ObjectInputStream ois;
        Film theFilm = null;

        try {

            ois = new ObjectInputStream(new FileInputStream(filePath));
            theFilm = (Film)ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File non trovato");
            
        } catch (ClassNotFoundException e) {
            
            System.out.println("Errore, il percorso in cui si trova la classe è danneggiato");
            
        } catch (IOException e) {

            System.out.println("Errore durante la lettura del file");
            
        }

        return theFilm;
    }
    
    public FidelityCard caricaTessera(String filePath) {

        ObjectInputStream ois;
        FidelityCard theCard = null;

        try {

            ois = new ObjectInputStream(new FileInputStream(filePath));
            theCard = (FidelityCard)ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File non trovato");
            
        } catch (ClassNotFoundException e) {
            
            System.out.println("Errore, il percorso in cui si trova la classe è danneggiato");
            
        } catch (IOException e) {
            
            e.printStackTrace();
            System.out.println("Errore durante la lettura del file");
            
        }

        return theCard;
    }
    
    public Map<Integer, String> caricaMappa(String filePath) {
        
        ObjectInputStream ois;
        Map<Integer, String> theMap = null;

        try {

            ois = new ObjectInputStream(new FileInputStream(filePath));
            theMap = (HashMap)ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File non trovato");
            
        } catch (ClassNotFoundException e) {
            
            System.out.println("Errore, il percorso in cui si trova la classe è danneggiato");
            
        } catch (IOException e) {
            
            e.printStackTrace();
            System.out.println("Errore durante la lettura del file");
        }

        return theMap;
    }
    
    public List<String> caricaLista(String filePath) {
        
        ObjectInputStream ois;
        List<String> theList = null;

        try {

            ois = new ObjectInputStream(new FileInputStream(filePath));
            theList = (ArrayList)ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File non trovato");
            
        } catch (ClassNotFoundException e) {
            
            System.out.println("Errore, il percorso in cui si trova la classe è danneggiato");
            
        } catch (IOException e) {

            System.out.println("Errore durante la lettura del file");
            
        }

        return theList;
    }

}
