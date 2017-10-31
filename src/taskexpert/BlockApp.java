/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static taskexpert.CreateTasksPanel.createImageIcon;

/**
 *
 * @author TVY
 */
public class BlockApp extends TimerTask {
    ArrayList<String> store;
    String appName;
    long timeBlock;
    
    BlockApp(String appName,long durBlock){
        this.appName = appName;
        //this.timeBlock = timeBlock;
        TimerTask timerTask = this;
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 1);
        //JOptionPane.showMessageDialog(null, this.timeBlock);
        //JOptionPane.showMessageDialog(null, "durBlock is" + durBlock);
        try {
            Thread.sleep(durBlock);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        timer.cancel();
    }
    
    @Override
    public void run() {   
        try {
            String line;
            Process p = Runtime.getRuntime().exec
                             (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            BufferedReader input =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
            for(int i=0;i<5;i++){
                line = input.readLine();
            }
            store = new ArrayList();
            while((line = input.readLine())!=null){
                String[] st = line.split((" "));
                store.add(st[0]);
            }
    
//            for(int i=0;i<store.size();i++){
//                System.out.print(store.get(i) + "\t");
//            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
            }
        
        //////////////////////////////////////
        //Input app that you want to block
        //String appBlock = "notepad.exe";
        //////////////////////////////////////
        
        for(int i=0;i<store.size();i++){
            if(store.get(i).equals(appName)){
                try {
                    
            Runtime.getRuntime().exec("taskkill /F /IM " + store.get(i));
            //JOptionPane.showMessageDialog(null, store.get(i) + " is blocked " + store.size());
            String message1 = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>" + store.get(i) + "<br><br>អ្នកបានកំណត់មិនឲ្យកម្មវិធីនេះបើកបាន នៅក្នុងការងាររបស់អ្នក</FONT></HTML>";
            Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>ខ្ញុំទទួលបាន</font></html"};
            ImageIcon logoIcon = createImageIcon("images/logo60x60.png");
        
            /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
            JOptionPane.showOptionDialog(null, message1, "", 0, 0, logoIcon, options, options[0]);
        } catch (IOException ex) {
            Logger.getLogger(BlockApp.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
        }
        
        
    }    
//    public static void main(String[] args){
//        TimerTask timerTask = new BlockApp();
//        //running timer task as daemon thread
//        Timer timer = new Timer(true);
//        timer.scheduleAtFixedRate(timerTask, 0, 1);
//        
//        try {
//            Thread.sleep(1200000000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//            new BlockApp();
//    }
}
