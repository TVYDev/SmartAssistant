/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;

import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author TVY
 */
public class WelcomePanel extends javax.swing.JFrame {

    /**
     * Creates new form WelcomePanel
     */
    Timer timer;
    
    public WelcomePanel() {
        initComponents();
        setLocationRelativeTo(null);
        setPreferredSize(new Dimension(400,320));
        
        pgbWelcome.setForeground(Color.BLUE);
        timer = new Timer(15,new Progress());
        timer.start();
    }

    class Progress implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            int n = pgbWelcome.getValue();
            
            if(n<100){
                n++;
                pgbWelcome.setValue(n);
            }else{
                timer.stop();
            }
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        pgbWelcome = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/welcome screen.png"))); // NOI18N
        getContentPane().add(jLabel1, java.awt.BorderLayout.CENTER);

        pgbWelcome.setValue(0);
        pgbWelcome.setRequestFocusEnabled(false);
        pgbWelcome.setString("");
        pgbWelcome.setStringPainted(true);
        getContentPane().add(pgbWelcome, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(WelcomePanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WelcomePanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WelcomePanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WelcomePanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WelcomePanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar pgbWelcome;
    // End of variables declaration//GEN-END:variables
}
