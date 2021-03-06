/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.FileFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author TVY
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form mainFrame
     */
    static CreateTasksPanel pnCreateTasks;
    static CalendarPanel pnCalendar;
    public static ViewTasksPanel pnViewTasks;
    static HistoryPanel pnHistory;
    public static MainForm mainForm;
    static Connection con;
    
    public static ArrayList<Task> listTask = new ArrayList<>();
    
    public MainForm() {
        initComponents();
        ImageIcon icon = pnCreateTasks.createImageIcon("images/logo.png");
            Image imag = icon.getImage();
        setIconImage(imag);

        stateSelected = 0;
        setSize(930,640);
        setLocationRelativeTo(null);
        
        
        
        /////////////////////////////////////////
        //CONNECTION TO DATABASE
        try {     
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            final String dir = System.getProperty("user.dir");
            System.out.println("dir =" + dir);
            
            con = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)}"
                    + ";DBQ=" + dir + "\\TaskExpert.accdb;"); 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        ///////////////////////////
        listTaskUpdate();
        ///////////////////////////
        
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        setTitle(dateFormat.format(cal.getTime()));
        
        pnCreateTasks = new CreateTasksPanel();
        pnCreateTasks.setBackground(displayColor);
        pnDisplay.setLayout(new BorderLayout());
      
        pnCalendar = new CalendarPanel();
        pnCalendar.setBackground(displayColor);
        
        pnViewTasks = new ViewTasksPanel();
        pnViewTasks.setBackground(displayColor);
        
        pnHistory = new HistoryPanel();
        pnHistory.setBackground(displayColor);

        pnDisplay.removeAll();
        
        stateSelected = 1;
        lblViewTasks.setBackground(selectedColor);
        lblCreateTask.setBackground(unselectedColor);
        lblHistory.setBackground(unselectedColor);
        lblCalendar.setBackground(unselectedColor);
        
        pnDisplay.add(pnViewTasks, BorderLayout.CENTER);
        pnDisplay.revalidate();
        pnDisplay.repaint();
        
    }

    static Color enteredColor = new Color(205,215,200);
    static Color exitedColor = new Color(102,204,255);
    static Color selectedColor = new Color(230,255,255);
    static Color unselectedColor = new Color(102,204,255);
    static Color displayColor = new Color(230,255,255);
    
    //Homepage=0    viewTask=1  createTask=2    history=3   calendar=4
    int stateSelected = 0;
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnOptions = new javax.swing.JPanel();
        lblViewTasks = new javax.swing.JLabel();
        lblHistory = new javax.swing.JLabel();
        lblCalendar = new javax.swing.JLabel();
        lblCreateTask = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblHelp = new javax.swing.JLabel();
        lblAbout = new javax.swing.JLabel();
        lblAbout1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pnDisplay = new javax.swing.JPanel();

        setResizable(false);
        getContentPane().setLayout(null);

        pnOptions.setBackground(new java.awt.Color(102, 204, 255));
        pnOptions.setLayout(null);

        lblViewTasks.setBackground(new java.awt.Color(102, 204, 255));
        lblViewTasks.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        lblViewTasks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblViewTasks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/viewMyTask.png"))); // NOI18N
        lblViewTasks.setText("  បង្ហាញការងារ");
        lblViewTasks.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblViewTasks.setName(""); // NOI18N
        lblViewTasks.setOpaque(true);
        lblViewTasks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblViewTasksMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblViewTasksMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblViewTasksMouseExited(evt);
            }
        });
        pnOptions.add(lblViewTasks);
        lblViewTasks.setBounds(0, 0, 250, 50);

        lblHistory.setBackground(new java.awt.Color(102, 204, 255));
        lblHistory.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        lblHistory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/history1.png"))); // NOI18N
        lblHistory.setText("  បញ្ជីការងារចាស់");
        lblHistory.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblHistory.setOpaque(true);
        lblHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHistoryMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHistoryMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHistoryMouseExited(evt);
            }
        });
        pnOptions.add(lblHistory);
        lblHistory.setBounds(490, 0, 220, 50);

        lblCalendar.setBackground(new java.awt.Color(102, 204, 255));
        lblCalendar.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        lblCalendar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/calendar.png"))); // NOI18N
        lblCalendar.setText("  ប្រតិទិន");
        lblCalendar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblCalendar.setOpaque(true);
        lblCalendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCalendarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCalendarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCalendarMouseExited(evt);
            }
        });
        pnOptions.add(lblCalendar);
        lblCalendar.setBounds(710, 0, 220, 50);

        lblCreateTask.setBackground(new java.awt.Color(102, 204, 255));
        lblCreateTask.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        lblCreateTask.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCreateTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/createTask.png"))); // NOI18N
        lblCreateTask.setText("  បង្កើតការងារ");
        lblCreateTask.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblCreateTask.setOpaque(true);
        lblCreateTask.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCreateTaskMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCreateTaskMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCreateTaskMouseExited(evt);
            }
        });
        pnOptions.add(lblCreateTask);
        lblCreateTask.setBounds(250, 0, 240, 50);

        getContentPane().add(pnOptions);
        pnOptions.setBounds(0, 110, 930, 50);

        jPanel2.setBackground(new java.awt.Color(0, 204, 153));
        jPanel2.setLayout(null);

        lblHelp.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        lblHelp.setText("ជំនួយ");
        lblHelp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHelpMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblHelpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblHelpMouseExited(evt);
            }
        });
        jPanel2.add(lblHelp);
        lblHelp.setBounds(869, 72, 51, 27);

        lblAbout.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        lblAbout.setText("អំពីយើង");
        lblAbout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAboutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblAboutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblAboutMouseExited(evt);
            }
        });
        jPanel2.add(lblAbout);
        lblAbout.setBounds(785, 72, 60, 27);

        lblAbout1.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        lblAbout1.setText("|");
        jPanel2.add(lblAbout1);
        lblAbout1.setBounds(851, 74, 12, 21);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/panellogo.png"))); // NOI18N
        jPanel2.add(jLabel1);
        jLabel1.setBounds(0, 0, 930, 110);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(0, 0, 930, 110);

        pnDisplay.setBackground(new java.awt.Color(204, 204, 255));
        pnDisplay.setLayout(null);
        getContentPane().add(pnDisplay);
        pnDisplay.setBounds(0, 160, 930, 480);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void listTaskUpdate(){
        Statement stmt;
        ResultSet rs;
        try{
            stmt = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT * FROM tbTasks WHERE doneStatus=false");
            listTask.clear();
            while(rs.next()){
                listTask.add(new Task(Integer.parseInt(rs.getString("taskID")), rs.getString("createDate"),
                        rs.getString("title"), rs.getString("notes"), rs.getString("category"),
                        rs.getString("sounds"), rs.getString("taskDate"), rs.getString("times"),
                        rs.getString("appToOpen"), rs.getString("appToBlock"),
                        Integer.parseInt(rs.getString("timeForBlock")), Boolean.parseBoolean(rs.getString("doneStatus"))));
            }
            rs.close();
            stmt.close();
        }catch(Exception exc){
            System.out.println(exc.getMessage());
        }
    }
    private void lblCreateTaskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCreateTaskMouseClicked
        stateSelected = 2;
        lblViewTasks.setBackground(unselectedColor);
        lblCreateTask.setBackground(selectedColor);
        lblHistory.setBackground(unselectedColor);
        lblCalendar.setBackground(unselectedColor);
        
        if(!pnCreateTasks.equals(null)){
            pnCreateTasks = new CreateTasksPanel();
            pnCreateTasks.setBackground(displayColor);
        }
        
        
        pnDisplay.removeAll();
        pnDisplay.add(pnCreateTasks, BorderLayout.CENTER);
        pnDisplay.revalidate();
        pnDisplay.repaint();
    }//GEN-LAST:event_lblCreateTaskMouseClicked

    private void lblCreateTaskMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCreateTaskMouseEntered
        lblCreateTask.setBackground(enteredColor);
    }//GEN-LAST:event_lblCreateTaskMouseEntered

    private void lblCreateTaskMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCreateTaskMouseExited
        if(stateSelected != 2){
            lblCreateTask.setBackground(exitedColor);
        }else{
            lblCreateTask.setBackground(selectedColor);
        }
    }//GEN-LAST:event_lblCreateTaskMouseExited

    private void lblViewTasksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewTasksMouseClicked
        stateSelected = 1;
        lblViewTasks.setBackground(selectedColor);
        lblCreateTask.setBackground(unselectedColor);
        lblHistory.setBackground(unselectedColor);
        lblCalendar.setBackground(unselectedColor);
        
        if(!pnViewTasks.equals(null)){
            pnViewTasks = new ViewTasksPanel();
        }
       
        if(CreateTasksPanel.threadSound.isAlive()){
            CreateTasksPanel.threadSound.stop();
            System.out.println("dkfjsdkfjdslkfd");
        }
        
        pnDisplay.removeAll();
        pnDisplay.add(pnViewTasks, BorderLayout.CENTER);
        pnDisplay.revalidate();
        pnDisplay.repaint();
    }//GEN-LAST:event_lblViewTasksMouseClicked

    private void lblViewTasksMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewTasksMouseEntered
        lblViewTasks.setBackground(enteredColor);
    }//GEN-LAST:event_lblViewTasksMouseEntered

    private void lblViewTasksMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblViewTasksMouseExited
        if(stateSelected != 1){
            lblViewTasks.setBackground(exitedColor);
        }else{
            lblViewTasks.setBackground(selectedColor);
        }
    }//GEN-LAST:event_lblViewTasksMouseExited

    private void lblHistoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHistoryMouseClicked
        stateSelected = 3;
        lblViewTasks.setBackground(unselectedColor);
        lblCreateTask.setBackground(unselectedColor);
        lblHistory.setBackground(selectedColor);
        lblCalendar.setBackground(unselectedColor);
        
        if(!pnHistory.equals(null)){
            pnHistory = new HistoryPanel();
        }
        
        if(CreateTasksPanel.threadSound.isAlive()){
            CreateTasksPanel.threadSound.stop();
        }
        
        pnDisplay.removeAll();
        pnDisplay.add(pnHistory, BorderLayout.CENTER);
        pnDisplay.revalidate();
        pnDisplay.repaint();
    }//GEN-LAST:event_lblHistoryMouseClicked

    private void lblHistoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHistoryMouseEntered
        lblHistory.setBackground(enteredColor);
    }//GEN-LAST:event_lblHistoryMouseEntered

    private void lblHistoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHistoryMouseExited
        if(stateSelected != 3){
            lblHistory.setBackground(exitedColor);
        }else{
            lblHistory.setBackground(selectedColor);
        }
    }//GEN-LAST:event_lblHistoryMouseExited

    private void lblHelpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHelpMouseEntered
        lblHelp.setForeground(new Color(255,255,255));
        lblHelp.setFont(new Font("Khmer OS Content",Font.ITALIC,16));
    }//GEN-LAST:event_lblHelpMouseEntered

    private void lblHelpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHelpMouseExited
        lblHelp.setForeground(new Color(0,0,0));
        lblHelp.setFont(new Font("Khmer OS Content",Font.PLAIN,14));
    }//GEN-LAST:event_lblHelpMouseExited

    private void lblAboutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAboutMouseEntered
        lblAbout.setForeground(new Color(255,255,255));
        lblAbout.setFont(new Font("Khmer OS Content",Font.ITALIC,16));
    }//GEN-LAST:event_lblAboutMouseEntered

    private void lblAboutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAboutMouseExited
        lblAbout.setForeground(new Color(0,0,0));
        lblAbout.setFont(new Font("Khmer OS Content",Font.PLAIN,14));
    }//GEN-LAST:event_lblAboutMouseExited

    private void lblAboutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAboutMouseClicked
        lblAbout.setForeground(new Color(255,255,255));
        lblAbout.setFont(new Font("Khmer OS Content",Font.ITALIC,14));
        
        DialogAbout dialogAbout = new DialogAbout(this,true);
        dialogAbout.setVisible(true);
    }//GEN-LAST:event_lblAboutMouseClicked

    private void lblHelpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHelpMouseClicked
        lblHelp.setForeground(new Color(255,255,255));
        lblHelp.setFont(new Font("Khmer OS Content",Font.ITALIC,14));
        final String dir = System.getProperty("user.dir");
        try{
            Desktop dt = Desktop.getDesktop();
            dt.open(new File(dir + "/Help doc.pdf"));
        }catch(Exception e){
            e.printStackTrace();
        } 
    }//GEN-LAST:event_lblHelpMouseClicked

    private void lblCalendarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCalendarMouseClicked
        stateSelected = 4;
        lblViewTasks.setBackground(unselectedColor);
        lblCreateTask.setBackground(unselectedColor);
        lblHistory.setBackground(unselectedColor);
        lblCalendar.setBackground(selectedColor);
        
        if(CreateTasksPanel.threadSound.isAlive()){
            CreateTasksPanel.threadSound.stop();
        }
        
        pnCalendar = new CalendarPanel();
        pnCalendar.setBackground(displayColor);
        
        pnDisplay.removeAll();
        pnDisplay.add(pnCalendar, BorderLayout.CENTER);
        pnDisplay.revalidate();
        pnDisplay.repaint();
    }//GEN-LAST:event_lblCalendarMouseClicked

    private void lblCalendarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCalendarMouseEntered
        lblCalendar.setBackground(enteredColor);
    }//GEN-LAST:event_lblCalendarMouseEntered

    private void lblCalendarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCalendarMouseExited
        if(stateSelected != 4){
            lblCalendar.setBackground(exitedColor);
        }else{
            lblCalendar.setBackground(selectedColor);
        }
    }//GEN-LAST:event_lblCalendarMouseExited

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
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        
        mainForm = new MainForm();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainForm.setVisible(false);
                
            }
        });
        WelcomePanel welcome = new WelcomePanel();
        welcome.setVisible(true);
        try{
            Thread.sleep(2200L);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        mainForm.setVisible(true);
        welcome.setVisible(false);
        
        SystemTrayUtils.createSystemTrayIcon();

        try{
            Thread.sleep(5000L);
        }catch(Exception e){
            e.printStackTrace();
        }


        Thread th = new Thread(new Runnable(){
            public void run(){
                new ThreadCheck();
            }
        });
        th.start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAbout;
    private javax.swing.JLabel lblAbout1;
    private javax.swing.JLabel lblCalendar;
    static javax.swing.JLabel lblCreateTask;
    private javax.swing.JLabel lblHelp;
    private javax.swing.JLabel lblHistory;
    static javax.swing.JLabel lblViewTasks;
    static javax.swing.JPanel pnDisplay;
    private javax.swing.JPanel pnOptions;
    // End of variables declaration//GEN-END:variables
}


class OnlyExt implements FileFilter{
    String ext;
    public OnlyExt(String ext){
        this.ext = "." + ext;
    }
    @Override
    public boolean accept(File file) {
        return file.getAbsolutePath().endsWith(ext);
    }
    
}