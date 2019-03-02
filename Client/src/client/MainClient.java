package client;

import GUI.ScegliFilmGUI;
import java.io.IOException;
import java.text.ParseException;
/**
 * @author Giovanni Calà
**/

public class MainClient 
{
    public MainClient() throws IOException, InterruptedException {

        ScegliFilmGUI spg = new ScegliFilmGUI();
        spg.setVisible(true); 

    }
    
    public static void main(String[] args) throws ParseException, IOException, InterruptedException {
        
        ScegliFilmGUI spg = new ScegliFilmGUI();
        spg.setVisible(true);
    }
}
