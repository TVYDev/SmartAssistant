/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javazoom.jl.player.Player;
import static taskexpert.MainForm.listTask;

/**
 *
 * @author TVY
 */
public class ThreadCheck extends TimerTask {
    
    
    
    ThreadCheck(){
        TimerTask timeTask = this;
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timeTask, 0, 1000L); //1second per round
        
        try{
            Thread.sleep(9999999999L);
        }catch(InterruptedException ie){
            ie.printStackTrace();
            System.out.println("from ThreadCheck");
        }
        
    }
     
    static Thread th;
    int c =1;
    String soundToPlay;
    @Override 
    public void run(){        

        Date currentDate = new Date();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm aa");
        
        for(int i=0;i<MainForm.listTask.size();i++){
            if(currentDateFormat.format(currentDate).equals(MainForm.listTask.get(i).taTaskDate) &&
               currentTimeFormat.format(currentDate).equals(MainForm.listTask.get(i).taTime)){
                soundToPlay = MainForm.listTask.get(i).taSound;
                new NotificationForm();
                NotificationForm.lblTaskID.setText(MainForm.listTask.get(i).taID + "");
                NotificationForm.lblTitleNotification.setText(MainForm.listTask.get(i).taTitle);
                NotificationForm.lblTaskOpen.setText(MainForm.listTask.get(i).taOpen);
                NotificationForm.lblTaskBlock.setText(MainForm.listTask.get(i).taBlock);
                NotificationForm.lblTimeBlock.setText(MainForm.listTask.get(i).taTimeBlock + "");
                NotificationForm.lblNote.setText(MainForm.listTask.get(i).taNote);
                NotificationForm.pnNotification.revalidate();
                NotificationForm.pnNotification.repaint();

                Statement stmt1;
                try{                    
                    stmt1 = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    String sql = "UPDATE tbTasks SET doneStatus=true WHERE taskID=" + MainForm.listTask.get(i).taID;
                    stmt1.executeUpdate(sql);
                    stmt1.close();                    
                }catch(Exception e){
                    //JOptionPane.showMessageDialog(null, "From ThreadCheck" + e.getMessage());
                    e.printStackTrace();
                }
                
                
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
                    //JOptionPane.showMessageDialog(null, "From function listTaskUpdate(): " + exc.getMessage());
                    exc.printStackTrace();
                }
                
                
                th = new Thread(new Runnable(){
                    public void run(){
                        try{
                             File mp3;
                             File f = new File(soundToPlay);
                             if(soundToPlay.equals("")){
                                 return;
                             }
                             if(f.exists()){
                                mp3 = new File(soundToPlay);
                             }else{
                                 final String dir = System.getProperty("user.dir");  //get project directory
                                //mp3 = new File(dir + "\\src\\taskexpert\\sounds\\Alarm01.mp3");
                                
                                String dirName = dir +  "\\src\\taskexpert\\sounds";
                                File f1 = new File(dirName);
        
                                //get only the files which have extension .mp3
                                OnlyExt only = new OnlyExt("mp3");  
                                File ff[] = f1.listFiles(only);
                                 
                                mp3 = ff[0];
                             }
                            
                            //File mp3 = new File("E:\\RUPP IT\\302\\Java\\TaskExpert\\src\\taskexpert\\sounds\\Moves Like Jagger.mp3");
                            
                            FileInputStream fis = new FileInputStream(mp3);
                            BufferedInputStream bis = new BufferedInputStream(fis);                           
                            Player player = new Player(bis);                          
                           
                            player.play();
                            //player.close();
       
                        }catch(Exception e){
                            //JOptionPane.showMessageDialog(null, "From ThreadCheck" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
                
                th.start();
                
                break;
            }
        }
    }
    
}
