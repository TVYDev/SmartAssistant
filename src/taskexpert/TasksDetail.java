/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static taskexpert.ViewTasksPanel.*;
import static taskexpert.MainForm.*;
import static taskexpert.CreateTasksPanel.*;
import java.sql.*;


/**
 *
 * @author SSK
 */

public class TasksDetail extends javax.swing.JPanel {
    /**
     * Creates new form TasksDetail
     */
    boolean click = false;

    
    int id; 
    String createDate, title, note, category, sound, taskDate;
    String time; 
    String appToOpen,  appToBlock;
    int timeBlock;
    boolean doneStatus; 
    
    public TasksDetail(int i,String cd,String t,String n,String c,String so,String td,
                       String ti, String appO, String appB, int tB, boolean dS) 
    {       
        initComponents();            
        id = i; createDate=cd; title=t; note=n; category=c; sound=so;
        taskDate=td;  time=ti; 
        appToOpen = appO; appToBlock = appB;       
        timeBlock = tB;     doneStatus = dS;
       
        
        for(int k=0;k<CreateTasksPanel.arrColorCate.size();k++){
            if(category.equals(arrColorCate.get(k).stCategory)){
                setBackground(arrColorCate.get(k).colorCate);
            }
        }       
        
        lblDeleteTask.addMouseListener(new MouseAdapter(){
//            @Override
//            public void mousePressed(MouseEvent me){
//                    lblDeleteTask.setOpaque(true);
//                    lblDeleteTask.setBackground(Color.WHITE);    
//            }
//            
//            @Override
//            public void mouseReleased(MouseEvent me){
//                lblDeleteTask.setOpaque(false);
//            }
            
            @Override
            public void mouseClicked(MouseEvent me){
                    setBorder(BorderFactory.createEmptyBorder());
                    String message = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4><p>តើអ្នកពិតជាចង់លុបការងារនេះមែនឬទេ?<br><br>" + title + "<br><br>បើអ្នកចុច <b>បាទ ឬចាស</b> នោះការងារនេះនឹងត្រូវបានលុបចេញពីកម្មវិធី។</p></FONT></HTML>";
                    Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>បាទ​​ ឬចាស</font></html","<html><font face=\"Khmer OS\" size=3>មិនលុបទេ</font></html"};
                    ImageIcon warningIcon = createImageIcon("images/warning.png");
        
                    /*Method showOptionDialog(Component parentComponent, 
                                        Object message, 
                                        String title, 
                                        int optionType, 
                                        int messageType,
                                        Icon icon, 
                                        Object[] options, 
                                        Object selectedValue)*/
                       
                    int choice = JOptionPane.showOptionDialog(null, message, "", 0, 0, warningIcon, options, options[0]);                     
                    
                    if(choice == 0){
                        Statement stmt;
                    
                        String sqlInsertToDelete = "Insert into tbDeletedTasks(taskID,createDate,title,notes,category,sounds,taskDate,times,appToOpen,appToBlock,timeForBlock) "
                                + "values(" + id + ",'" + createDate + "','" + title + "','" + note 
                                + "','" + category + "','" + sound 
                                + "','" + taskDate + "','" + time 
                                + "','" + appToOpen + "','" + appToBlock 
                                + "'," + timeBlock + ");";
                    
                        try{
                            stmt = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            stmt.executeUpdate(sqlInsertToDelete);
                            stmt.close();
                        }catch(Exception e){
                            //JOptionPane.showMessageDialog(null, "From insert to delete query" + e.getMessage());
                            e.printStackTrace();
                        }
                    
                    
                        String sqlDelele = "DELETE FROM tbTasks WHERE taskID=" + id;
                        try{
                            stmt = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            stmt.executeUpdate(sqlDelele);
                            stmt.close();
                        }catch(Exception e){
                            //JOptionPane.showMessageDialog(null, "From delete query" + e.getMessage());
                            e.printStackTrace();
                        }
                        
                        ViewTasksPanel pnVTask = new ViewTasksPanel();
                        pnVTask.setBackground(new Color(230,255,255));
                        MainForm.pnDisplay.removeAll();
                        MainForm.pnDisplay.add(pnVTask, BorderLayout.CENTER);
                        MainForm.pnDisplay.revalidate();
                        MainForm.pnDisplay.repaint();
                    }
            }
        });

        lblEditTask.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me){
              pnCreateTasks.ExchangeInfo(id, createDate,title, note, category, 
                        sound, taskDate, time, appToOpen, appToBlock, 
                        timeBlock, doneStatus);        
                
                pnCreateTasks.setBackground(selectedColor);
                pnDisplay.removeAll();                
                pnDisplay.add(pnCreateTasks, BorderLayout.CENTER);
                pnDisplay.revalidate();
                pnDisplay.repaint();
                lblViewTasks.setBackground(unselectedColor);
                lblCreateTask.setBackground(selectedColor);                
                               
                pnCreate.setVisible(true);
                lblClickToCreate.setVisible(false);
                lblCreate.setEnabled(false);
                lblSave.setEnabled(true);
                lblCancel.setEnabled(true);
                pnAppOpenBlock.setVisible(true);
                
                mainForm.stateSelected = 2;
            }
        });   
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
//    @SuppressWarnings("unchecked");
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 204));
        setPreferredSize(new java.awt.Dimension(190, 160));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
