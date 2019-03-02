package generatori;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import server.Consumazione;
import server.FidelityCard;
import server.Ingresso;
import server.Omaggio;

/**
 * @author Giovanni Calà
**/

public class GeneratoreFidelity implements Serializable {
    
    private static final String PATH = "C://Users/giova/Desktop/ProgettoJava2017/";
    
    transient Scanner in = null;
    
    public GeneratoreFidelity() {
        
        in = new Scanner(System.in);
    }
    
    public static void main(String[] args) {
        
        GeneratoreFidelity generatore = new GeneratoreFidelity();
        
        /* GENERATE FIDELITYCARD */
        FidelityCard tessera1 = new FidelityCard("a231id", 28);
        FidelityCard tessera2 = new FidelityCard("a22224", 108);
        FidelityCard tessera3 = new FidelityCard("bc91fh", 0);
        FidelityCard tessera4 = new FidelityCard("a61ztl", 6);
        
        /* SAVE FILM */
        generatore.salvaFidelity(PATH + "a231id", tessera1);
        generatore.salvaFidelity(PATH + "a22224", tessera2);
        generatore.salvaFidelity(PATH + "bc91fh", tessera3);
        generatore.salvaFidelity(PATH + "a61ztl", tessera4);

        /* GENERATE LIST */
        List<String> tessere = new ArrayList<String>();
        tessere.add("a231id");
        tessere.add("a22224");
        tessere.add("bc91fh");
        tessere.add("a61ztl");
        
        Map<Integer, Omaggio> listinoOmaggi = new HashMap<Integer, Omaggio>();
        listinoOmaggi.put(10, new Consumazione("Pop-corn Mini"));
        listinoOmaggi.put(20, new Ingresso("1 Ingresso Gratuito", 1));
        listinoOmaggi.put(25, new Consumazione("Pop-corn Maxi"));
        listinoOmaggi.put(45, new Ingresso("2 Ingressi Gratuiti", 2));

        /* SAVE MAP */
        generatore.salvaLista(PATH + "tessere", tessere);
        
        generatore.salvaMappaListinoOmaggi(PATH + "listinoOmaggi", listinoOmaggi);
    }
    
    public void salvaFidelity(String filePath, FidelityCard theCard) {
        
        ObjectOutputStream oss;
        
        try {
            
            oss = new ObjectOutputStream(new FileOutputStream(filePath));
            oss.writeObject(theCard);
            oss.close();
            
        } catch (FileNotFoundException e) {
          
            System.out.println("Problemi nell'apertura del file");
            
        } catch (IOException e) {
            e.printStackTrace();
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
    
    private void salvaMappaListinoOmaggi(String filePath, Map<Integer, Omaggio> myMap) {
        
        ObjectOutputStream oss;
        
        try {
            
            oss = new ObjectOutputStream(new FileOutputStream(filePath));
            oss.writeObject((HashMap)myMap);
            oss.close();
            
        } catch (FileNotFoundException e) {
          
            System.out.println("Problemi nell'apertura del file");
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problemi nella scrittura del file");
        }
    }

}
