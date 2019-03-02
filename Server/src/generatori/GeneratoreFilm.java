package generatori;

import server.Film;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * @author Giovanni Calà
**/

public class GeneratoreFilm {
    
    private static final String PATH = "C://Users/giova/Desktop/ProgettoJava2017/";
    Scanner in = null;
    
    public GeneratoreFilm() {
        
        in = new Scanner(System.in);
    }
    
    public static void main(String[] args) {

        GeneratoreFilm generatore = new GeneratoreFilm();
        
        /* GENERATE FILM */
        Film thor = new Film("Thor", "Fantasy", "Kenneth Branagh", "2011", "USA", "114 min");
        Film avatar = new Film("Avatar", "Avventura", "James Cameron", "2009", "Regno Unito", "161 min");
        Film insidious = new Film("Insidious", "Horror", "James Wan", "2010", "USA", "102 min");

        /* SAVE FILM */
        generatore.salvaFilm(PATH + "Thor", thor);
        generatore.salvaFilm(PATH + "Avatar", avatar);
        generatore.salvaFilm(PATH + "Insidious", insidious);

        /* GENERATE LIST */
        List<String> titoli = new ArrayList<String>();
        titoli.add("Thor");
        titoli.add("Avatar");
        titoli.add("Insidious");
        
        /* SAVE LIST */
        generatore.salvaLista(PATH + "titoli", titoli);
    }
    
    private void salvaFilm(String filePath, Film theFilm) {
        
        ObjectOutputStream oss;
        
        try {
            
            oss = new ObjectOutputStream(new FileOutputStream(filePath));
            oss.writeObject(theFilm);
            oss.close();
            
        } catch (FileNotFoundException e) {
          
            System.out.println("Problemi nell'apertura del file");
            
        } catch (IOException e) {
            
            System.out.println("Problemi nella scrittura del file");
        }
    }
    
    private void salvaLista(String filePath, List<String> myList) {
        
        ObjectOutputStream oss;
        
        try {
            
            oss = new ObjectOutputStream(new FileOutputStream(filePath));
            oss.writeObject((ArrayList)myList);
            oss.close();
            
        } catch (FileNotFoundException e) {
          
            System.out.println("Problemi nell'apertura del file");
            
        } catch (IOException e) {
            
            System.out.println("Problemi nella scrittura del file");
        }
    }

}