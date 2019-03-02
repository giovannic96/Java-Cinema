package GUI;

import client.Client;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Giovanni Calà
 */
public class ScontrinoGUI extends javax.swing.JFrame {

    private static final String POSTI_LIBERI = "Posti liberi";

    Client client;
    String scontrino;
    
    public ScontrinoGUI(Client client) throws IOException, InterruptedException {
        
        this.client = client;
        scontrino = client.inviaScontrinoTemporaneo();
        initComponents();
        for(Frame frame : Frame.getFrames())
            if(frame.getTitle().equals("Scegli film") && frame.isShowing()) { //chiude la finestra dello scontrino se è insieme alla finestra della scelta film
                WindowEvent winClosingEvent = new WindowEvent(this.getOwner(), WindowEvent.WINDOW_CLOSING );
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent( winClosingEvent );   
            }
    }

    private ScontrinoGUI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Scontrino");
        setResizable(false);

        jButton1.setText("Conferma acquisto");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText(scontrino);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(104, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        try {
            String controlloPosti = client.controllaPosti();
            
            if(controlloPosti.equals(POSTI_LIBERI)) {
                
                String nuoviPunti = client.aggiornaPuntiFidelity(client.getDetrazionePunti(), client.getCodiceFidelity());
                client.modificaPosti();

                if(nuoviPunti.equals("0"))
                    JOptionPane.showMessageDialog(this.getContentPane(), "Biglietti acquistati con successo", "Transazione", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(this.getContentPane(), "Biglietti acquistati con successo\nNuovi punti tessera: " + nuoviPunti, "Transazione", JOptionPane.INFORMATION_MESSAGE);

                System.exit(0);
            }
            else {
                JOptionPane.showMessageDialog(this.getContentPane(), "Posti già occupati. Riprovare", "Impossibile prenotare posti", JOptionPane.INFORMATION_MESSAGE);
                for(Frame frame : Frame.getFrames()) {
                    if(frame.getTitle().equals("Seleziona spettacolo")) {
                        frame.setEnabled(true);
                        this.dispose();
                    }
                }
            }                
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ScontrinoGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScontrinoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScontrinoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScontrinoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScontrinoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScontrinoGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
