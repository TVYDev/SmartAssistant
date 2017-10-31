/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;


import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.EmptyBorder;
import static taskexpert.CreateTasksPanel.*;

/**
 *
 * @author TVY
 */
public class ViewTasksPanel extends javax.swing.JPanel{
    /**
     * Creates new form CreateTasksPanel
     */
    
    Statement st;
    ResultSet rs;
    TasksDetail tasks;
    ArrayList<TasksDetail> arraylist = new ArrayList<>();
          
    static ImageIcon edit = createImageIcon("images/edit.png");
    static JLabel lblEditTask = new JLabel(edit);
    
    static ImageIcon delete = createImageIcon("images/delete1.png");
    static JLabel lblDeleteTask = new JLabel(delete);
   
    int i=0;
    
    static JPanel scrollPanePanel;
    static JScrollPane scrollPane;
    
    public ViewTasksPanel() {
        
        initComponents();
        setSize(930, 480);        
        lblEditTask.setOpaque(false);
        lblEditTask.setToolTipText("<html><font face=\"Khmer OS Content\" Size=3>កែប្រែ</font></html>"); 
        
        lblDeleteTask.setOpaque(false);
        lblDeleteTask.setToolTipText("<html><font face=\"Khmer OS Content\" Size=3>លុប</font></html>");   
        
       pnTasksDetail.setBackground(MainForm.selectedColor);

       arraylist.clear();
       pnTasksDetail.removeAll();
       
       scrollPanePanel = new JPanel();
       scrollPane = new JScrollPane(scrollPanePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       
//       String sql = "SELECT * FROM tbTasks WHERE doneStatus=false";
            Date current1 = new Date();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            //SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm aa");
            String day = currentDateFormat.format(current1);  //  is now the new date
            String sql = "SELECT * FROM tbTasks WHERE doneStatus=false";
                   // + " AND Format(taskDate, 'Short Date')>='"+day+"'";//' AND times<'"+currentTimeFormat.format(current1)+"'");
        try {
            st = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(sql);
            while(rs.next()){
                int id=rs.getInt(1);
                String createDate=rs.getString(2);
                String title=rs.getString(3);
                String note=rs.getString(4);
                String category=rs.getString(5);
                String sound=rs.getString(6);
                String taskDate=rs.getString(7);
                String time=rs.getString(8);
                String appToOpen=rs.getString(9);
                String appToBlock=rs.getString(10);
                int timeBlock = rs.getInt(11);
                boolean doneStatus = rs.getBoolean("doneStatus"); 
                
                lblEditTask = new JLabel(edit);
                lblEditTask.setToolTipText("<html><font face=\"Khmer OS Content\" Size=3>កែប្រែ</font></html>");
                lblDeleteTask = new JLabel(delete);       
                lblDeleteTask.setToolTipText("<html><font face=\"Khmer OS Content\" Size=3>លុប</font></html>");
                 
                tasks = new TasksDetail(id,createDate,title,note,category,sound,taskDate, time,
                        appToOpen, appToBlock, timeBlock, doneStatus);
                tasks.setLayout(new BorderLayout());                
                
                JLabel lblTaskDateBottom = new JLabel(taskDate);
                lblTaskDateBottom.setFont(new Font("Comic Sans MS",Font.PLAIN,15));
               
                JLabel lblTaskTimeBottom = new JLabel(time);
                lblTaskTimeBottom.setFont(new Font("Comic Sans MS",Font.PLAIN,15));               
            
                JPanel pnBottom = new JPanel();
                pnBottom.setLayout(new BorderLayout());
                Color color = tasks.getBackground();
                pnBottom.setBackground(new Color(color.getRed(),color.getGreen()-50,color.getBlue()));
                pnBottom.add(lblEditTask,BorderLayout.WEST);
                pnBottom.add(lblDeleteTask,BorderLayout.EAST);
                
                JPanel pnTop = new JPanel();
                pnTop.setBackground(new Color(205,215,200));
                pnTop.setLayout(new BorderLayout());
                
                
                pnTop.add(lblTaskDateBottom, BorderLayout.WEST);
                pnTop.add(lblTaskTimeBottom, BorderLayout.EAST);
                
                JLabel lblTaskID = new JLabel(id + "");
                tasks.add(lblTaskID,BorderLayout.NORTH);
                lblTaskID.setVisible(false);
                
                JLabel lblTaskTitle = new JLabel("<html>" + title + "</html>");
                lblTaskTitle.setFont(new Font("Khmer OS Content",Font.PLAIN,16));
                setBorder(new EmptyBorder(5,5,5,5));   
                
                ///////CURRENT DATE
                Date currentDate = new Date();
                SimpleDateFormat forDate = new SimpleDateFormat("dd/MM/yy");
                String curDate = forDate.format(currentDate);
                Date cDate = new Date();
                try {
                    cDate = forDate.parse(curDate);
                } catch (ParseException ex) {
                    Logger.getLogger(CreateTasksPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                ///////CURRENT TIME
                SimpleDateFormat forTime = new SimpleDateFormat("hh:mm aa");
                String curTime = forTime.format(currentDate);
                Date cTime = new Date();
                try {
                    cTime = forTime.parse(curTime);
                } catch (ParseException ex) {
                    Logger.getLogger(CreateTasksPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                ///////TASK DATE
                Date dT = forDate.parse(taskDate);
                //////TASK TIME
                Date tT = forTime.parse(time);
                
                ImageIcon w = createImageIcon("images/warning20x20.png");
                if(dT.before(cDate)){
                    lblTaskTitle.setIcon(w);
                }else if(dT.equals(cDate)){ 
                    if(tT.before(cTime)){
                        lblTaskTitle.setIcon(w);
                    }else{
                        lblTaskTitle.setIcon(null);
                    }
                }else{
                    lblTaskTitle.setIcon(null);
                }
                
                
                if(lblTaskTitle.getIcon()!=null){
                    lblTaskTitle.setToolTipText("<html><font face=\"Khmer OS Content\" Size=3>កាងារនេះត្រូវបានអាក់ខានមិនបានរំលឹក</font></html>");
                }
                
                JPanel pnFarLeftGap = new JPanel();
                pnFarLeftGap.setBackground(tasks.getBackground());
                pnFarLeftGap.setPreferredSize(new Dimension(5,160));
                pnFarLeftGap.setMinimumSize(pnFarLeftGap.getPreferredSize());
                pnFarLeftGap.setMaximumSize(pnFarLeftGap.getPreferredSize());
            
                tasks.add(pnFarLeftGap,BorderLayout.WEST);
                
                tasks.add(lblTaskTitle,BorderLayout.CENTER);
                tasks.add(pnTop,BorderLayout.NORTH);
                tasks.add(pnBottom, BorderLayout.SOUTH);               
             
                arraylist.add(tasks); 
                scrollPanePanel.setLayout(new FlowLayout());
                scrollPanePanel.setPreferredSize(new Dimension(888,300+(arraylist.size()/4)*140));
                //double d = arraylist.size()/4;
                //d = Math.ceil(d);
                //int hh = (int)d;
                //scrollPanePanel.setPreferredSize(new Dimension(888,200*hh));
                scrollPanePanel.setBorder(null);
                scrollPanePanel.setBackground(MainForm.displayColor);
                
                scrollPanePanel.add(arraylist.get(i)); 
                JPanel panel = new JPanel();
//                panel.setBackground(MainForm.displayColor);
//                panel.setPreferredSize(new Dimension());
                pnTasksDetail.add(scrollPane, BorderLayout.CENTER);
                i++;
            }
                //rs.close();
                //st.close();
        } catch (Exception ex) {
             //System.out.println("Error View");
             ex.printStackTrace();
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

        pnTasksDetail = new javax.swing.JPanel();

        setBackground(new java.awt.Color(230, 255, 255));
        setLayout(null);

        pnTasksDetail.setBackground(new java.awt.Color(230, 255, 255));
        pnTasksDetail.setLayout(new java.awt.BorderLayout());
        add(pnTasksDetail);
        pnTasksDetail.setBounds(0, 20, 920, 430);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel pnTasksDetail;
    // End of variables declaration//GEN-END:variables

}



