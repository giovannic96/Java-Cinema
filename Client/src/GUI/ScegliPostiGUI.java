package GUI;

import client.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ScegliPostiGUI extends JFrame {

    private static final int WIDTH_FRAME = 520;       
    private static final int HEIGHT_FRAME = 300; 
    private static final int TOTAL_SEATS = 9; 

    private Client client;

    private List<String> postiLiberiTrackerList = new ArrayList<String>();
    private List<String> postiOccupatiTrackerList = new ArrayList<String>();
    private List<String> tuttiIPosti = new ArrayList<String>();
    
    private String[] poltrone;
    private int numRidotti;
    private int idSpettacolo;
    
    private JButton confirmBtn = new JButton("Conferma");
    private JLabel ridottiLabel = new JLabel("Quanti ridotti?");
    private JTextField numRidottiTextField = new JTextField(2);
    private JLabel fidelityLabel = new JLabel("Hai una tessera fidelity?");
    private JButton yesBtn = new JButton("SI");
    private JButton noBtn = new JButton("NO");
    private List<String> postiScelti = new ArrayList<String>();
    
    private JButton[] poltroneBtns = new JButton[TOTAL_SEATS];
    //private JLabel seatNumberLabel = new JLabel("Seat Number");
    //private JLabel confirmLabel = new JLabel("Confirmed"); 
    private int userSelectedSeat; // the seat that user selects
    private ButtonListener buttonListener = new ButtonListener(); 
    
    public ScegliPostiGUI(Client client, String sala, String ora, String data) throws IOException, ParseException, InterruptedException {
      
        this.client = client;
        setTitle("Scegli posti");
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setLayout(new FlowLayout());
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        idSpettacolo = Integer.parseInt(client.ottieniId(data, sala, ora));
        postiLiberiTrackerList = client.riceviPostiLiberi(sala, ora, data);
        postiOccupatiTrackerList = client.riceviPostiOccupati(sala, ora, data);
        
        if(!postiLiberiTrackerList.get(0).equals("")) //se ci sono posti liberi
            for(int i = 0; i < postiLiberiTrackerList.size(); i++)
                tuttiIPosti.add(postiLiberiTrackerList.get(i));
        
        else  //non ci sono posti liberi
            JOptionPane.showMessageDialog(this.getContentPane(), "Non ci sono posti liberi per questa sala", "Attenzione", JOptionPane.INFORMATION_MESSAGE);    
        
        if(!postiOccupatiTrackerList.get(0).equals("")) //se ci sono posti occupati
            for(int i = 0; i < postiOccupatiTrackerList.size(); i++)          
                tuttiIPosti.add(postiOccupatiTrackerList.get(i));
                
        Collections.sort(tuttiIPosti); //ordina la lista
       
        poltrone = tuttiIPosti.toArray(new String[tuttiIPosti.size()]);
        
        createContents();
    }
        
    private void createContents() {
      
        setLayout(new FlowLayout());

        for(int i = 0; i < tuttiIPosti.size(); i++) {

          poltroneBtns[i] = new JButton(tuttiIPosti.get(i));
          add(poltroneBtns[i]);
          poltroneBtns[i].addActionListener(buttonListener);
        } 

        for (int i = 0; i < postiOccupatiTrackerList.size() ; i++) {

            for(int j = 0; j < tuttiIPosti.size(); j++) {

                if(postiOccupatiTrackerList.get(i).equals(tuttiIPosti.get(j))) {
                    poltroneBtns[j].setForeground(Color.RED);
                    poltroneBtns[j].removeActionListener(buttonListener);
                }
            }
        } 

        confirmBtn.setEnabled(false);
        confirmBtn.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    confirmBtnActionPerformed(evt);
                }
        });
        yesBtn.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    yesBtnActionPerformed(evt);
                }
        });
        noBtn.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        noBtnActionPerformed(evt);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ScegliPostiGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        });
               
        addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            
                for(Frame frame : Frame.getFrames())
                    if(frame.getTitle().equals("Seleziona spettacolo"))
                        frame.setEnabled(true);
                dispose();
        }
        });
        
        //add(seatNumberLabel).setVisible(true);
        //add(confirmLabel).setVisible(true);   
        add(confirmBtn).setVisible(true);
        add(ridottiLabel).setVisible(false);
        add(numRidottiTextField).setVisible(false);
        add(fidelityLabel).setVisible(false);
        add(yesBtn).setVisible(false);
        add(noBtn).setVisible(false);
  } 
    
    private void confirmBtnActionPerformed(ActionEvent evt) {
        
        ridottiLabel.setVisible(true);
        numRidottiTextField.setVisible(true);
        numRidottiTextField.setText("0");
        numRidottiTextField.addActionListener(new TextListener()); 
        
        for(JButton b : poltroneBtns) {
            b.removeActionListener(buttonListener);
        }
    }
  
    private void yesBtnActionPerformed(ActionEvent evt) {
    
        Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
        activeWindow.setVisible(false);
        new FidelityGUI(client).setVisible(true);
        
    }
    
    private void noBtnActionPerformed(ActionEvent evt) throws InterruptedException {
        
        Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
        activeWindow.setVisible(false);
       
        try {
            new ScontrinoGUI(client).setVisible(true);            
            
        } catch (IOException ex) {
            Logger.getLogger(ScegliPostiGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void display() {
      
        System.out.println("\nPosti scelti:\n");
        System.out.println(postiScelti);   
  }    

  private class TextListener implements ActionListener {
      
    @Override
    public void actionPerformed(ActionEvent e) {
        
        boolean canContinue = false;
       
        try {
            numRidotti = Integer.parseInt(numRidottiTextField.getText());
            
            if(numRidotti < 0) 
                throw new MyNegativeNumberException("Inserire un numero positivo");
            
            else if(numRidotti > postiScelti.size())
                throw new MyOutOfRangeException("Inserire un numero inferiore al numero di poltrone scelte");
            
            else 
                canContinue = true;
            
        } catch(MyNegativeNumberException ex) {
            JOptionPane.showMessageDialog(null, "Inserire un numero positivo", "Errore inserimento dati", JOptionPane.ERROR_MESSAGE);
            
        } catch(MyOutOfRangeException ex) {
            JOptionPane.showMessageDialog(null, "Inserire un numero inferiore al numero di poltrone scelte", "Errore inserimento dati", JOptionPane.ERROR_MESSAGE);

        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Inserire un numero", "Errore inserimento dati", JOptionPane.ERROR_MESSAGE);

        }
        
        if(canContinue) {

            numRidottiTextField.setEditable(false);

            try {
                client.confermaPosti(postiScelti, numRidotti, idSpettacolo);
            
            } catch (IOException ex) {
                Logger.getLogger(ScegliPostiGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            fidelityLabel.setVisible(true);
            yesBtn.setVisible(true);
            noBtn.setVisible(true);
        }
    }
  }
  
  private class ButtonListener implements ActionListener {
      
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(!confirmBtn.isEnabled()) 
            confirmBtn.setEnabled(true);
        
        int i = 0;
        JButton seat = (JButton)e.getSource();

        while(!poltroneBtns[i].getText().equals(seat.getText()))
        i++;

        userSelectedSeat = i;
        JTextField newSeat = new JTextField(2);
        add(newSeat).setVisible(true);
        newSeat.setText(seat.getText());
        newSeat.setEditable(false);
        validate(); //refresh panel
        poltroneBtns[userSelectedSeat].setEnabled(false);
        postiScelti.add(seat.getText());
        display();        
    } 
  }   
}

class MyNegativeNumberException extends Exception {
    
    public MyNegativeNumberException(String message) {
        super(message);
    }
}

class MyOutOfRangeException extends Exception {
    
    public MyOutOfRangeException(String message) {
        super(message);
    }
}
