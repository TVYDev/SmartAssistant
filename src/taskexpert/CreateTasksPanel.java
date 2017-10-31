/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;


import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.filechooser.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javazoom.jl.player.Player;

/**
 *
 * @author TVY
 */
public class CreateTasksPanel extends javax.swing.JPanel {

    /**
     * Creates new form CreateTasksPanel
     */
    int countOpen = 0;
    int countBlock = 3;
    PanelAddAppDoc[] pnAdd;
    PanelAppDocName[] pnAppDocName;
    
    static int duration = 0;
    int taskTodayCount = 0;
    
    static ArrayList<ColorCategory> arrColorCate = new ArrayList<>();
    ArrayList<PanelTaskToday> pnTToday = new ArrayList<>();
    static ArrayList<SoundFile> arrSoundFile = new ArrayList<>();
    static ArrayList<String> arrExistSounds = new ArrayList<>();
    JPanel pnScrollPane;
    JScrollPane scrollPane;
    
    static Thread threadSound = new Thread();
    
    public CreateTasksPanel() {
        initComponents();
        setSize(930, 480);
        
//        if(threadSound.isAlive()){
//            threadSound.stop();
//        }
        
        lblID.setVisible(false);
       
        dialogNote.setSize(new Dimension(395,330));
        dialogNote.setLocationRelativeTo(null);
        
        dialogRepeat.setSize(new Dimension(380,370));
        dialogRepeat.setLocationRelativeTo(null);
       
        Date currentDate = new Date();        
        dpkDate.setDate(currentDate);
        
        arrColorCate.add(new ColorCategory(1, "មិនកំណត់", new Color(145,210,255))); //not set--light blue
        arrColorCate.add(new ColorCategory(2, "សំខាន់", new Color(255,200,85))); //exam--light orange
        arrColorCate.add(new ColorCategory(3, "ប្រឡង", new Color(195,190,90))); //important--light brown
        arrColorCate.add(new ColorCategory(4, "កិច្ចការសាលា", new Color(210,255,255))); //assignment--light purple
        arrColorCate.add(new ColorCategory(5, "ការងារ", new Color(205,210,190))); //work--light grey
        arrColorCate.add(new ColorCategory(6, "កិច្ចការផ្ទាល់ខ្លួន", new Color(250,180,190))); //personal task--light pink
        arrColorCate.add(new ColorCategory(7, "ខួបកំណើត", new Color(235,235,135))); //Birthday--light yellow
        arrColorCate.add(new ColorCategory(8, "វិស្សមកាល", new Color(105,155,160))); //Holiday--light grey-blue
        
        ////////////////////////////////////////////////////
        //Get the sound file from folder sounds and add them to cboSounds
        
        //to get the current directory of the project
        //it return E:\RUPP IT\302\Java\TaskExpert
//        try{
        final String dir = System.getProperty("user.dir");  
        //System.out.println("dir = " + dir);
        
        String dirName = dir +  "\\src\\taskexpert\\sounds";
        File f1 = new File(dirName);
        
        //get only the files which have extension .mp3
        OnlyExt only = new OnlyExt("mp3");  
        File f[] = f1.listFiles(only);
        
        String soundsPath, soundsName, soundsNameNoExt;
        
        arrSoundFile.clear();
        cboSound.removeAllItems();
        
        arrExistSounds.clear();
        
        for(int i=0;i<f.length;i++){
            if(only.accept(f[i])){
                soundsPath = f[i].getAbsolutePath();
                soundsName = f[i].getName();
                soundsNameNoExt = f[i].getName().replaceFirst("[.][^.]+$", "");
                arrSoundFile.add(new SoundFile(soundsPath,soundsName));
                cboSound.addItem(soundsNameNoExt);
                arrExistSounds.add(soundsNameNoExt);
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        try{
            FileReader frSound = new FileReader("soundNamePath.txt");
            BufferedReader br = new BufferedReader(frSound);
            
            File fTest;
            
            String line;
            while((line = br.readLine()) != null){
                fTest = new File(line);
                if(fTest.exists()){
                    cboSound.addItem(fTest.getName().replaceFirst("[.][^.]+$", ""));
                }
            }
            br.close();
            frSound.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        cboSound.addItem("(មិនដាក់សម្លេងរោទិ៍)");
        
        
        cboHourBlock.removeAllItems();

        for(int i=0;i<=23;i++){
            cboHourBlock.addItem("" + i);

        }
        cboMinuteBlock.removeAllItems();

        for(int i=1;i<=59;i++){
            String st = "";
            if(i<10){
                st = "0";
            }
            cboMinuteBlock.addItem(st + i);
        }
        cboHour.removeAllItems();
        for(int i=1;i<=12;i++){
            cboHour.addItem("" + i);
        }
        cboMinute.removeAllItems();
        for(int i=0;i<=59;i++){
            String st = "";
            if(i<10){
                st = "0";
            }
            cboMinute.addItem(st + i);
        }
        cboTimeState.removeAllItems();
        cboTimeState.addItem("AM");
        cboTimeState.addItem("PM");
 
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm:aa");
        String dateNow = dateFormat.format(cal.getTime());
        //System.out.println(dateNow);
        String[] st = dateNow.split(":");
        cboHour.setSelectedItem(st[0]);
        cboMinute.setSelectedItem(st[1]);
        cboTimeState.setSelectedItem(st[2]);
        
        pnCreate.setVisible(false);
        lblCreate.setEnabled(true);
        lblSave.setEnabled(false);
        lblCancel.setEnabled(false);
        pnAppOpenBlock.setVisible(false);
        
        pnTasksToday.setLayout(new BorderLayout());
        pnTasksToday.setBackground(new Color(230,255,255));
        pnScrollPane = new JPanel();
        pnScrollPane.setBackground(new Color(230,255,255));
        pnScrollPane.setSize(new Dimension(800,15));
        scrollPane = new JScrollPane(pnScrollPane);
        pnTasksToday.add(scrollPane,BorderLayout.CENTER);
        
        Statement stmt;
        ResultSet rs;
        
        PanelTaskToday pnTT;
        try{
            stmt = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //Date current1 = new Date();
            //SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            //SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm aa");
            //String day = currentDateFormat.format(current1);  //  is now the new date
            rs = stmt.executeQuery("SELECT * FROM tbTasks WHERE createDate=Date() AND doneStatus=false");
                  //  + " AND Format(taskDate, 'Short Date')>='" + day + "'");//' AND times<'"+currentTimeFormat.format(current1)+"'");
            
            
            while(rs.next()){
                int tid = Integer.parseInt(rs.getString("taskID"));
                String tcreatedate = rs.getString("createDate");
                String ttitle = rs.getString("title");
                String tnote = rs.getString("notes");
                String tcategory = rs.getString("category");
                String tsound = rs.getString("sounds");
                String ttaskdate = rs.getString("taskDate");
                
                String ttime = rs.getString("times");
                
                String tapptoopen = rs.getString("appToOpen");
                String tapptoblock = rs.getString("appToBlock");
                int ttimeforblock = Integer.parseInt(rs.getString("timeForBlock"));
                boolean tdonestatus = Boolean.parseBoolean(rs.getString("doneStatus"));
                
                SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d = sdformat.parse(tcreatedate);
                SimpleDateFormat sdformat1 = new SimpleDateFormat("dd/MM/yy");
                
                Color c = new Color(0,0,0);
                for(int i=0;i<arrColorCate.size();i++){
                    if(tcategory.equals(arrColorCate.get(i).stCategory)){
                        c = arrColorCate.get(i).colorCate;
                    }
                }
                pnTT = new PanelTaskToday(ttitle,c,tid,tcreatedate,ttitle,tnote,tcategory,tsound,ttaskdate,ttime,
                                            tapptoopen,tapptoblock,ttimeforblock,tdonestatus);
                pnTToday.add(pnTT);
                
            }
            rs.close();
            stmt.close();
            
        }catch(Exception exc){
            System.out.println("From createTaskPanel" + exc.getMessage());
        }
         
        pnScrollPane.removeAll();
        for(int i=0;i<pnTToday.size();i++){
            
            pnScrollPane.add(pnTToday.get(i));
            scrollPane.revalidate();
                
            lblTotalTasksToday.setText("សរុប = " + (taskTodayCount+1));
            taskTodayCount++;
        }
         
       chbSetTime.setSelected(true);
         
       pnAdd = new PanelAddAppDoc[2];
       pnAppDocName = new PanelAppDocName[6];
       
       for(int i=0;i<6;i++){
           pnAppDocName[i] = new PanelAppDocName();
       }
              
       pnAdd[0] = new PanelAddAppDoc();
       pnAppDocOpen.add(pnAdd[0]);
       pnAdd[1] = new PanelAddAppDoc();
       pnAppDocBlock.add(pnAdd[1]);
       
       pnAdd[0].addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent me){
                    pnAdd[0].setBorder(BorderFactory.createLineBorder(new Color(102,204,255), 1));
                }
                
                @Override
                public void mouseExited(MouseEvent me){
                    pnAdd[0].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
                
                @Override
                public void mouseClicked(MouseEvent me){
                    JFileChooser fc = new JFileChooser();
                    fc.setBackground(new Color(230,255,255));
                    fc.addChoosableFileFilter(new FileNameExtensionFilter("Executable files (.exe)", "exe"));
                    fc.addChoosableFileFilter(new FileNameExtensionFilter("PDF (.pdf)", "pdf"));
                    fc.addChoosableFileFilter(new FileNameExtensionFilter("Text Document (.txt)", "txt"));
                    fc.addChoosableFileFilter(new FileNameExtensionFilter("Word Document (.docx)", "docx"));
                    int returnVal = fc.showOpenDialog(null);
                    if(returnVal == JFileChooser.APPROVE_OPTION){
                        pnAddZeroMouseClicked(fc.getSelectedFile().getAbsolutePath());
                    }
                }
                
            });
       
      pnAppDocName[0].lblDeleteIcon.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me){
                        pnAppDocOpen.remove(pnAppDocName[0]);
                        pnAppDocOpen.revalidate();
                        pnAppDocOpen.repaint();
                        countOpen=0;
                        if(pnAppDocOpen.getComponentCount()<=3){
                            pnAppDocOpen.add(pnAdd[0]);
                        }
                    }
                });
      pnAppDocName[1].lblDeleteIcon.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me){
                        pnAppDocOpen.remove(pnAppDocName[1]);
                        pnAppDocOpen.revalidate();
                        pnAppDocOpen.repaint();
                        countOpen=1;
                        if(pnAppDocOpen.getComponentCount()<=3){
                            pnAppDocOpen.add(pnAdd[0]);
                        }
                    }
                });
      
      pnAppDocName[2].lblDeleteIcon.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me){
                        pnAppDocOpen.remove(pnAppDocName[2]);
                        pnAppDocOpen.revalidate();
                        pnAppDocOpen.repaint();
                        countOpen=2;
                        if(pnAppDocOpen.getComponentCount()<=3){
                            pnAppDocOpen.add(pnAdd[0]);
                        }
                        
                    }
                });
      
      pnAppDocName[0].lblChangeIcon.addMouseListener(new MouseAdapter(){
          @Override
          public void mouseClicked(MouseEvent me){
              if(pnAppDocBlock.getComponentCount()==3
                      && pnAppDocBlock.getComponent(2) != pnAdd[1]){
                            fullAddedAppDoc();
                            return;
                        }
              pnAddOneMouseClicked(pnAppDocName[0].path);
              
              pnAppDocOpen.remove(pnAppDocName[0]);
              pnAppDocOpen.revalidate();
              pnAppDocOpen.repaint();
              countOpen = 0;
              if(pnAppDocOpen.getComponentCount()<=3){
                            pnAppDocOpen.add(pnAdd[0]);
                        }
              
          }
      });
      pnAppDocName[1].lblChangeIcon.addMouseListener(new MouseAdapter(){
          @Override
          public void mouseClicked(MouseEvent me){
              if(pnAppDocBlock.getComponentCount()==3
                      && pnAppDocBlock.getComponent(2) != pnAdd[1]){
                            fullAddedAppDoc();
                            return;
                        }
              pnAddOneMouseClicked(pnAppDocName[1].path);
              
              pnAppDocOpen.remove(pnAppDocName[1]);
              pnAppDocOpen.revalidate();
              pnAppDocOpen.repaint();
              countOpen = 1;
              if(pnAppDocOpen.getComponentCount()<=3){
                            pnAppDocOpen.add(pnAdd[0]);
                        }
              
          }
      });
      pnAppDocName[2].lblChangeIcon.addMouseListener(new MouseAdapter(){
          @Override
          public void mouseClicked(MouseEvent me){
              if(pnAppDocBlock.getComponentCount()==3
                      && pnAppDocBlock.getComponent(2) != pnAdd[1]){
                            fullAddedAppDoc();
                            return;
                        }
              pnAddOneMouseClicked(pnAppDocName[2].path);
              
              pnAppDocOpen.remove(pnAppDocName[2]);
              pnAppDocOpen.revalidate();
              pnAppDocOpen.repaint();
              countOpen = 2;
              if(pnAppDocOpen.getComponentCount()<=3){
                            pnAppDocOpen.add(pnAdd[0]);
                        }
              
          }
      });
      
       pnAdd[1].addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent me){
                    pnAdd[1].setBorder(BorderFactory.createLineBorder(new Color(102,204,255), 1));
                }
                
                @Override
                public void mouseExited(MouseEvent me){
                    pnAdd[1].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
                
                @Override
                public void mouseClicked(MouseEvent me){
                    JFileChooser fc = new JFileChooser();
                    fc.setBackground(new Color(230,255,255));
                    fc.addChoosableFileFilter(new FileNameExtensionFilter("Executable files (.exe)", "exe"));
                    fc.setAcceptAllFileFilterUsed(false);
                    int returnVal = fc.showOpenDialog(null);
                    if(returnVal == JFileChooser.APPROVE_OPTION){
                        pnAddOneMouseClicked(fc.getSelectedFile().getAbsolutePath());
                    }
                }
                
            });
       
       pnAppDocName[3].lblDeleteIcon.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me){
                        pnAppDocBlock.remove(pnAppDocName[3]);
                        pnAppDocBlock.revalidate();
                        pnAppDocBlock.repaint();
                        countBlock=3;
                        if(pnAppDocBlock.getComponentCount()<=3){
                            pnAppDocBlock.add(pnAdd[1]);
                        }
                    }
                });
      pnAppDocName[4].lblDeleteIcon.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me){
                        pnAppDocBlock.remove(pnAppDocName[4]);
                        pnAppDocBlock.revalidate();
                        pnAppDocBlock.repaint();
                        countBlock=4;
                        if(pnAppDocBlock.getComponentCount()<=3){
                            pnAppDocBlock.add(pnAdd[1]);
                        }
                    }
                });
      
      pnAppDocName[5].lblDeleteIcon.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me){
                        pnAppDocBlock.remove(pnAppDocName[5]);
                        pnAppDocBlock.revalidate();
                        pnAppDocBlock.repaint();
                        countBlock=5;
                        if(pnAppDocBlock.getComponentCount()<=3){
                            pnAppDocBlock.add(pnAdd[1]);
                        }
                    }
                });
      pnAppDocName[3].lblChangeIcon.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me){
                        if(pnAppDocOpen.getComponentCount()==3
                                && pnAppDocOpen.getComponent(2) != pnAdd[0]){
                            fullAddedAppDoc();
                            return;
                        }
                        pnAddZeroMouseClicked(pnAppDocName[3].path);
                        pnAppDocBlock.remove(pnAppDocName[3]);
                        pnAppDocBlock.revalidate();
                        pnAppDocBlock.repaint();
                        countBlock=3;
                        if(pnAppDocBlock.getComponentCount()<=3){
                            pnAppDocBlock.add(pnAdd[1]);
                        }
                    }
                });
      pnAppDocName[4].lblChangeIcon.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me){
                        if(pnAppDocOpen.getComponentCount()==3
                                && pnAppDocOpen.getComponent(2) != pnAdd[0]){
                            fullAddedAppDoc();
                            return;
                        }
                        pnAddZeroMouseClicked(pnAppDocName[4].path);
                        pnAppDocBlock.remove(pnAppDocName[4]);
                        pnAppDocBlock.revalidate();
                        pnAppDocBlock.repaint();
                        countBlock=4;
                        if(pnAppDocBlock.getComponentCount()<=3){
                            pnAppDocBlock.add(pnAdd[1]);
                        }
                    }
                });
      pnAppDocName[5].lblChangeIcon.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me){
                        if(pnAppDocOpen.getComponentCount()==3
                                && pnAppDocOpen.getComponent(2) != pnAdd[0]){
                            fullAddedAppDoc();
                            return;
                        }
                        pnAddZeroMouseClicked(pnAppDocName[5].path);
                        pnAppDocBlock.remove(pnAppDocName[5]);
                        pnAppDocBlock.revalidate();
                        pnAppDocBlock.repaint();
                        countBlock=5;
                        if(pnAppDocBlock.getComponentCount()<=3){
                            pnAppDocBlock.add(pnAdd[1]);
                        }
                    }
                });
    }

    void pnAddZeroMouseClicked(String path){
        if(pnAppDocOpen.getComponentCount()==3 
                            && (pnAppDocOpen.getComponent(0) == pnAppDocName[1] ||
                                pnAppDocOpen.getComponent(1) == pnAppDocName[1])){
                        countOpen = 2;
                    }
                    
                    if(pnAppDocOpen.getComponentCount()==3 
                            && (pnAppDocOpen.getComponent(0) == pnAppDocName[2] ||
                                pnAppDocOpen.getComponent(1) == pnAppDocName[2])){
                        countOpen = 1;
                    }
                    if(pnAppDocOpen.getComponentCount()==3
                            && ((pnAppDocOpen.getComponent(0) == pnAppDocName[1] && pnAppDocOpen.getComponent(1) == pnAppDocName[2]) ||
                                (pnAppDocOpen.getComponent(0) == pnAppDocName[2] && pnAppDocOpen.getComponent(1) == pnAppDocName[1]))){
                        countOpen = 0;
                    }
                    if(pnAppDocOpen.getComponentCount()==2
                            && pnAppDocOpen.getComponent(0) == pnAppDocName[1]){
                        countOpen = 0;
                    }
                    if(pnAppDocOpen.getComponentCount()==1){
                        countOpen = 0;
                    }
                    
                    pnAppDocName[countOpen].setPath(path);
                    pnAppDocOpen.add(pnAppDocName[countOpen]);
                    
                    
                    pnAppDocOpen.add(pnAdd[0]);
                    if(pnAppDocOpen.getComponentCount() == 4){
                        pnAppDocOpen.remove(pnAdd[0]);
                    }
                    
                    //To force panel to get the updates immediately
                    //In fact, revalidate() operates automatically, but takes a little long time
                    //That's why, it is manually called in here
                    pnAppDocOpen.revalidate();  
                    pnAppDocOpen.repaint();

                    countOpen++;
    }
    
    void fullAddedAppDoc(){
        String message = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>អ្នកអាចកំណត់បានត្រឹមតែ <b><font color=\"RED\">3</font></b>​ កម្មវិធី ឬឯកសារប៉ុណ្ណោះ</FONT></HTML>";
        Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>យល់ព្រម</font></html"};
        ImageIcon fullIcon = createImageIcon("images/full.png");
        
        /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
        JOptionPane.showOptionDialog(null, message, "", 0, 0, fullIcon, options, options[0]);
    }
    
    void pnAddOneMouseClicked(String path){
        if(pnAppDocBlock.getComponentCount()==3 
                            && (pnAppDocBlock.getComponent(0) == pnAppDocName[4] ||
                                pnAppDocBlock.getComponent(1) == pnAppDocName[4])){
                        countBlock = 5;
                    }
                    
                    if(pnAppDocBlock.getComponentCount()==3 
                            && (pnAppDocBlock.getComponent(0) == pnAppDocName[5] ||
                                pnAppDocBlock.getComponent(1) == pnAppDocName[5])){
                        countBlock = 4;
                    }
                    if(pnAppDocBlock.getComponentCount()==3
                            && ((pnAppDocBlock.getComponent(0) == pnAppDocName[4] && pnAppDocBlock.getComponent(1) == pnAppDocName[5]) ||
                                (pnAppDocBlock.getComponent(0) == pnAppDocName[5] && pnAppDocBlock.getComponent(1) == pnAppDocName[4]))){
                        countBlock = 3;
                    }
                    if(pnAppDocBlock.getComponentCount()==2
                            && pnAppDocBlock.getComponent(0) == pnAppDocName[4]){
                        countBlock = 3;
                    }
                    if(pnAppDocBlock.getComponentCount()==1){
                        countBlock = 3;
                    }
                    
                    pnAppDocName[countBlock].setPath(path);
                    pnAppDocBlock.add(pnAppDocName[countBlock]);
                    
                    
                    pnAppDocBlock.add(pnAdd[1]);
                    if(pnAppDocBlock.getComponentCount() == 4){
                        pnAppDocBlock.remove(pnAdd[1]);
                    }
                    
                    //To force panel to get the updates immediately
                    //In fact, revalidate() operates automatically, but takes a little long time
                    //That's why, it is manually called in here
                    pnAppDocBlock.revalidate();  
                    pnAppDocBlock.repaint();

                    countBlock++;
    }
    
    int vID;
    public void ExchangeInfo(int i, String cd,String t,String n,String c,String so,String td,
                       String ti, String appO, String appB, int tB, boolean dS){
        vID = i;
        lblID.setText(i + "");
        tfTitle.setText(t);
                    stnote = n;
                    
                    SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yy");
                    try{    
                        dpkDate.setDate(sdformat.parse(td));
                    } catch (ParseException ex) {
                        Logger.getLogger(CreateTasksPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    cboCategory.setSelectedItem(c);
                    
                    if(so.equals("")){
                        cboSound.setSelectedItem("(មិនដាក់សម្លេងរោទិ៍)");
                    }else{
                        File fTest = new File(so);
                        cboSound.setSelectedItem(fTest.getName().replaceFirst("[.][^.]+$", ""));
                    }
                    
                    if(ti.equals("")){
                        chbSetTime.setSelected(false);
                        if(!chbSetTime.isSelected()){
                            cboHour.setEnabled(false);
                            cboMinute.setEnabled(false);
                            cboTimeState.setEnabled(false);
                        }else{
                            cboHour.setEnabled(true);
                            cboMinute.setEnabled(true);
                            cboTimeState.setEnabled(true);
                        }
                    }
                    if(!ti.equals("")){
                        chbSetTime.setSelected(true);
                        if(!chbSetTime.isSelected()){
                            cboHour.setEnabled(false);
                            cboMinute.setEnabled(false);
                            cboTimeState.setEnabled(false);
                        }else{
                            cboHour.setEnabled(true);
                            cboMinute.setEnabled(true);
                            cboTimeState.setEnabled(true);
                        }
                        cboHour.setSelectedIndex(Integer.parseInt(ti.substring(0, 2))-1);
                        cboMinute.setSelectedItem(ti.substring(3,5));
                        cboTimeState.setSelectedItem(ti.substring(6,8));
                    }
                    
                    pnAppDocOpen.removeAll();
                    pnAppDocBlock.removeAll();
                    pnAppDocOpen.add(pnAdd[0]);
                    pnAppDocBlock.add(pnAdd[1]);
                    pnAppDocOpen.revalidate();  pnAppDocOpen.repaint();
                    pnAppDocBlock.revalidate(); pnAppDocBlock.repaint();
                    
                    if(!appO.equals("")){
                        String[] stOpen = appO.split("<");    //after split, String "" is auto deleted 
                        for(i=0;i<stOpen.length;i++){
                            pnAddZeroMouseClicked(stOpen[i]);
                        }
                    }
                    if(!appB.equals("")){
                        String[] stBlock = appB.split("<");
                        for(i=0;i<stBlock.length;i++){
                            pnAddOneMouseClicked(stBlock[i]);
                        }
                        cboHourBlock.setSelectedIndex((tB/60));
                        cboMinuteBlock.setSelectedIndex((tB%60)-1);
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

        dialogNote = new javax.swing.JDialog();
        btnCancelNote = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taNote = new javax.swing.JTextArea();
        btnSaveNote = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        dialogRepeat = new javax.swing.JDialog();
        jLabel12 = new javax.swing.JLabel();
        tpRepeat = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        cboRepeatDaily = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cboRepeatWeekly = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        chbSun = new javax.swing.JCheckBox();
        chbMon = new javax.swing.JCheckBox();
        chbTue = new javax.swing.JCheckBox();
        chbWed = new javax.swing.JCheckBox();
        chbThu = new javax.swing.JCheckBox();
        chbFri = new javax.swing.JCheckBox();
        chbSat = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        cboRepeatMonthly = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        cboRepeatYearly = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        btnCancelRepeat = new javax.swing.JButton();
        btnSaveRepeat = new javax.swing.JButton();
        dialogOpenSoundFile = new javax.swing.JDialog();
        jLabel7 = new javax.swing.JLabel();
        fchSound = new javax.swing.JFileChooser();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        lblCreate = new javax.swing.JLabel();
        lblSave = new javax.swing.JLabel();
        lblCancel = new javax.swing.JLabel();
        pnAppOpenBlock = new javax.swing.JPanel();
        pnAppDocBlock = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        pnAppDocOpen = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        cboMinuteBlock = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        cboHourBlock = new javax.swing.JComboBox<>();
        pnClickToCreate = new javax.swing.JPanel();
        lblClickToCreate = new javax.swing.JLabel();
        pnCreate = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        tfTitle = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dpkDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel8 = new javax.swing.JLabel();
        btnAddNote = new javax.swing.JButton();
        cboCategory = new javax.swing.JComboBox<>();
        lblColorCategory = new javax.swing.JLabel();
        chbSetTime = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        cboHour = new javax.swing.JComboBox<>();
        cboMinute = new javax.swing.JComboBox<>();
        cboTimeState = new javax.swing.JComboBox<>();
        btnAddSound = new javax.swing.JButton();
        lblID = new javax.swing.JLabel();
        cboSound = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        btnStop = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        pnTasksToday = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblTotalTasksToday = new javax.swing.JLabel();

        dialogNote.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialogNote.setBackground(new java.awt.Color(230, 255, 255));
        dialogNote.setModal(true);
        dialogNote.setResizable(false);
        dialogNote.getContentPane().setLayout(null);

        btnCancelNote.setFont(new java.awt.Font("Khmer OS Content", 0, 12)); // NOI18N
        btnCancelNote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/cancelNote.png"))); // NOI18N
        btnCancelNote.setText("បោះបង់");
        btnCancelNote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelNoteMouseClicked(evt);
            }
        });
        dialogNote.getContentPane().add(btnCancelNote);
        btnCancelNote.setBounds(280, 260, 100, 33);

        taNote.setColumns(20);
        taNote.setFont(new java.awt.Font("Khmer OS", 0, 14)); // NOI18N
        taNote.setLineWrap(true);
        taNote.setRows(5);
        taNote.setWrapStyleWord(true);
        jScrollPane1.setViewportView(taNote);

        dialogNote.getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 80, 370, 170);

        btnSaveNote.setFont(new java.awt.Font("Khmer OS Content", 0, 12)); // NOI18N
        btnSaveNote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/saveNote.png"))); // NOI18N
        btnSaveNote.setText("រក្សាទុក");
        btnSaveNote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveNoteMouseClicked(evt);
            }
        });
        dialogNote.getContentPane().add(btnSaveNote);
        btnSaveNote.setBounds(170, 260, 100, 33);

        jLabel1.setFont(new java.awt.Font("Khmer OS Content", 0, 15)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/note.png"))); // NOI18N
        jLabel1.setText("បញ្ចូលកំណត់ត្រារបស់អ្នកក្នុងប្រអប់ខាងក្រោមនេះ");
        jLabel1.setIconTextGap(20);
        dialogNote.getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 370, 60);

        dialogRepeat.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialogRepeat.setBackground(new java.awt.Color(230, 255, 255));
        dialogRepeat.setModal(true);
        dialogRepeat.setResizable(false);
        dialogRepeat.getContentPane().setLayout(null);

        jLabel12.setFont(new java.awt.Font("Khmer OS Content", 0, 15)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/repeatSetting.png"))); // NOI18N
        jLabel12.setText("កំណត់ភាពសារឡើងវិញនៃការងាររបស់អ្នក");
        jLabel12.setIconTextGap(20);
        dialogRepeat.getContentPane().add(jLabel12);
        jLabel12.setBounds(0, 10, 400, 60);

        tpRepeat.setFont(new java.awt.Font("Khmer OS Content", 0, 13)); // NOI18N

        jPanel3.setLayout(null);

        cboRepeatDaily.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        cboRepeatDaily.setMaximumRowCount(4);
        cboRepeatDaily.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6" }));
        jPanel3.add(cboRepeatDaily);
        cboRepeatDaily.setBounds(80, 10, 50, 30);

        jLabel13.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel13.setText("ថ្ងៃ");
        jPanel3.add(jLabel13);
        jLabel13.setBounds(140, 10, 14, 30);

        jLabel14.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel14.setText("រៀងរាល់");
        jPanel3.add(jLabel14);
        jLabel14.setBounds(20, 10, 60, 30);

        tpRepeat.addTab("ប្រចាំថ្ងៃ", jPanel3);

        jPanel4.setLayout(null);

        jLabel15.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel15.setText("នៅថ្ងៃ");
        jPanel4.add(jLabel15);
        jLabel15.setBounds(20, 50, 60, 30);

        cboRepeatWeekly.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        cboRepeatWeekly.setMaximumRowCount(4);
        cboRepeatWeekly.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));
        jPanel4.add(cboRepeatWeekly);
        cboRepeatWeekly.setBounds(80, 10, 50, 30);

        jLabel16.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel16.setText("សប្ដាហ៍");
        jPanel4.add(jLabel16);
        jLabel16.setBounds(140, 10, 50, 30);

        jLabel17.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel17.setText("រៀងរាល់");
        jPanel4.add(jLabel17);
        jLabel17.setBounds(20, 10, 60, 30);
        jPanel4.add(jSeparator3);
        jSeparator3.setBounds(20, 80, 310, 20);

        chbSun.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        chbSun.setText("អាទិត្យ");
        jPanel4.add(chbSun);
        chbSun.setBounds(170, 130, 70, 30);

        chbMon.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        chbMon.setText("ចន្ទ");
        jPanel4.add(chbMon);
        chbMon.setBounds(30, 90, 50, 30);

        chbTue.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        chbTue.setText("អង្គារ");
        jPanel4.add(chbTue);
        chbTue.setBounds(100, 90, 50, 30);

        chbWed.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        chbWed.setText("ពុធ");
        jPanel4.add(chbWed);
        chbWed.setBounds(170, 90, 50, 30);

        chbThu.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        chbThu.setText("ព្រហស្បតិ៍");
        jPanel4.add(chbThu);
        chbThu.setBounds(240, 90, 81, 30);

        chbFri.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        chbFri.setText("សុក្រ");
        jPanel4.add(chbFri);
        chbFri.setBounds(30, 130, 50, 30);

        chbSat.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        chbSat.setText("សោរ៍");
        jPanel4.add(chbSat);
        chbSat.setBounds(100, 130, 53, 30);

        tpRepeat.addTab("ប្រចាំសប្ដាហ៍", jPanel4);

        jPanel5.setLayout(null);

        jLabel18.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel18.setText("ពត៌មាន....");
        jPanel5.add(jLabel18);
        jLabel18.setBounds(20, 60, 60, 30);

        cboRepeatMonthly.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        cboRepeatMonthly.setMaximumRowCount(4);
        cboRepeatMonthly.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11" }));
        cboRepeatMonthly.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboRepeatMonthlyItemStateChanged(evt);
            }
        });
        jPanel5.add(cboRepeatMonthly);
        cboRepeatMonthly.setBounds(80, 10, 50, 30);

        jLabel19.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel19.setText("ខែ");
        jPanel5.add(jLabel19);
        jLabel19.setBounds(140, 10, 50, 30);

        jLabel22.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel22.setText("រៀងរាល់");
        jPanel5.add(jLabel22);
        jLabel22.setBounds(20, 10, 60, 30);

        tpRepeat.addTab("ប្រចាំខែ", jPanel5);

        jPanel6.setLayout(null);

        jLabel20.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel20.setText("រៀងរាល់");
        jPanel6.add(jLabel20);
        jLabel20.setBounds(20, 10, 60, 30);

        cboRepeatYearly.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        cboRepeatYearly.setMaximumRowCount(4);
        cboRepeatYearly.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        jPanel6.add(cboRepeatYearly);
        cboRepeatYearly.setBounds(80, 10, 50, 30);

        jLabel21.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel21.setText("ឆ្នាំ");
        jPanel6.add(jLabel21);
        jLabel21.setBounds(140, 10, 50, 30);

        jLabel23.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel23.setText("ពត៌មាន....");
        jPanel6.add(jLabel23);
        jLabel23.setBounds(20, 60, 60, 30);

        tpRepeat.addTab("ប្រចាំឆ្នាំ", jPanel6);

        dialogRepeat.getContentPane().add(tpRepeat);
        tpRepeat.setBounds(10, 80, 350, 210);

        btnCancelRepeat.setFont(new java.awt.Font("Khmer OS Content", 0, 12)); // NOI18N
        btnCancelRepeat.setText("បោះបង់");
        btnCancelRepeat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelRepeatMouseClicked(evt);
            }
        });
        dialogRepeat.getContentPane().add(btnCancelRepeat);
        btnCancelRepeat.setBounds(260, 300, 100, 30);

        btnSaveRepeat.setFont(new java.awt.Font("Khmer OS Content", 0, 12)); // NOI18N
        btnSaveRepeat.setText("រក្សាទុក");
        btnSaveRepeat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveRepeatMouseClicked(evt);
            }
        });
        dialogRepeat.getContentPane().add(btnSaveRepeat);
        btnSaveRepeat.setBounds(150, 300, 100, 30);

        dialogOpenSoundFile.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialogOpenSoundFile.setMinimumSize(new java.awt.Dimension(525, 435));
        dialogOpenSoundFile.setModal(true);
        dialogOpenSoundFile.setResizable(false);
        dialogOpenSoundFile.getContentPane().setLayout(null);

        jLabel7.setFont(new java.awt.Font("Khmer OS Content", 0, 15)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/find.png"))); // NOI18N
        jLabel7.setText("ស្វែងរកឯកសារបទភ្លេងរបស់អ្នក");
        dialogOpenSoundFile.getContentPane().add(jLabel7);
        jLabel7.setBounds(0, 10, 520, 60);
        dialogOpenSoundFile.getContentPane().add(fchSound);
        fchSound.setBounds(2, 80, 520, 320);
        dialogOpenSoundFile.getContentPane().add(jSeparator5);
        jSeparator5.setBounds(20, 70, 490, 70);

        setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 102, 153));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);

        lblCreate.setBackground(new java.awt.Color(255, 102, 153));
        lblCreate.setFont(new java.awt.Font("Khmer OS Content", 0, 13)); // NOI18N
        lblCreate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/addNew.png"))); // NOI18N
        lblCreate.setText("បង្កើតថ្មី");
        lblCreate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCreateMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCreateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCreateMouseExited(evt);
            }
        });
        jPanel1.add(lblCreate);
        lblCreate.setBounds(20, 10, 80, 40);

        lblSave.setBackground(new java.awt.Color(255, 102, 153));
        lblSave.setFont(new java.awt.Font("Khmer OS Content", 0, 13)); // NOI18N
        lblSave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/save.png"))); // NOI18N
        lblSave.setText("រក្សាទុក");
        lblSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSaveMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSaveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSaveMouseExited(evt);
            }
        });
        jPanel1.add(lblSave);
        lblSave.setBounds(110, 10, 80, 40);

        lblCancel.setBackground(new java.awt.Color(255, 102, 153));
        lblCancel.setFont(new java.awt.Font("Khmer OS Content", 0, 13)); // NOI18N
        lblCancel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/cancel.png"))); // NOI18N
        lblCancel.setText("បោះបង់");
        lblCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCancelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCancelMouseExited(evt);
            }
        });
        jPanel1.add(lblCancel);
        lblCancel.setBounds(200, 10, 80, 40);

        add(jPanel1);
        jPanel1.setBounds(0, 0, 300, 60);

        pnAppOpenBlock.setBackground(new java.awt.Color(204, 255, 102));
        pnAppOpenBlock.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 2, true));
        pnAppOpenBlock.setOpaque(false);
        pnAppOpenBlock.setLayout(null);

        pnAppDocBlock.setBackground(new java.awt.Color(230, 255, 255));
        pnAppOpenBlock.add(pnAppDocBlock);
        pnAppDocBlock.setBounds(10, 230, 300, 150);

        jLabel10.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("កម្មវិធី ឬឯកសារដែលនឹងត្រូវបានបើកដោយស្វ័យប្រវត្តិ");
        pnAppOpenBlock.add(jLabel10);
        jLabel10.setBounds(10, 10, 300, 26);

        jLabel9.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("កម្មវិធីដែលនឹងត្រូវបិទមិនឲ្យប្រើប្រាស់");
        pnAppOpenBlock.add(jLabel9);
        jLabel9.setBounds(10, 200, 300, 26);

        pnAppDocOpen.setBackground(new java.awt.Color(230, 255, 255));
        pnAppOpenBlock.add(pnAppDocOpen);
        pnAppDocOpen.setBounds(10, 40, 300, 150);

        jLabel3.setFont(new java.awt.Font("Khmer OS Content", 0, 13)); // NOI18N
        jLabel3.setText("ម៉ោង");
        pnAppOpenBlock.add(jLabel3);
        jLabel3.setBounds(140, 380, 30, 30);
        pnAppOpenBlock.add(jSeparator6);
        jSeparator6.setBounds(10, 200, 300, 190);

        jLabel11.setFont(new java.awt.Font("Khmer OS Content", 0, 13)); // NOI18N
        jLabel11.setText("បិទរយៈពេល");
        pnAppOpenBlock.add(jLabel11);
        jLabel11.setBounds(10, 380, 70, 30);

        cboMinuteBlock.setFont(new java.awt.Font("Comic Sans MS", 0, 13)); // NOI18N
        pnAppOpenBlock.add(cboMinuteBlock);
        cboMinuteBlock.setBounds(170, 380, 60, 30);

        jLabel24.setFont(new java.awt.Font("Khmer OS Content", 0, 13)); // NOI18N
        jLabel24.setText("នាទី");
        pnAppOpenBlock.add(jLabel24);
        jLabel24.setBounds(230, 380, 30, 30);

        cboHourBlock.setFont(new java.awt.Font("Comic Sans MS", 0, 13)); // NOI18N
        pnAppOpenBlock.add(cboHourBlock);
        cboHourBlock.setBounds(80, 380, 60, 30);

        add(pnAppOpenBlock);
        pnAppOpenBlock.setBounds(590, 10, 320, 420);

        pnClickToCreate.setBackground(new java.awt.Color(153, 153, 255));
        pnClickToCreate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 2, true));
        pnClickToCreate.setOpaque(false);
        pnClickToCreate.setLayout(null);

        lblClickToCreate.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        lblClickToCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/click.png"))); // NOI18N
        lblClickToCreate.setText("ចុច \"បង្កើតថ្មី\" ដើម្បីបង្កើតការងារ");
        pnClickToCreate.add(lblClickToCreate);
        lblClickToCreate.setBounds(170, 80, 280, 60);

        pnCreate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 2, true));
        pnCreate.setForeground(new java.awt.Color(102, 102, 102));
        pnCreate.setOpaque(false);
        pnCreate.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        jLabel5.setText("អំពី");
        pnCreate.add(jLabel5);
        jLabel5.setBounds(30, 20, 30, 31);

        tfTitle.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        pnCreate.add(tfTitle);
        tfTitle.setBounds(60, 10, 450, 40);

        jLabel2.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        jLabel2.setText("ប្រភេទ");
        pnCreate.add(jLabel2);
        jLabel2.setBounds(310, 70, 43, 31);

        dpkDate.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        pnCreate.add(dpkDate);
        dpkDate.setBounds(110, 70, 160, 30);
        dpkDate.setFormats("dd/MM/yy");

        jLabel8.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        jLabel8.setText("កាលបរិច្ឆេទ");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        pnCreate.add(jLabel8);
        jLabel8.setBounds(30, 70, 75, 31);

        btnAddNote.setFont(new java.awt.Font("Khmer OS Content", 0, 14)); // NOI18N
        btnAddNote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/noteSmall.png"))); // NOI18N
        btnAddNote.setToolTipText("<html><font face=\"Khmer OS Content\">បញ្ចូលកំណត់ត្រា</font></html>");
        btnAddNote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddNoteMouseClicked(evt);
            }
        });
        btnAddNote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNoteActionPerformed(evt);
            }
        });
        pnCreate.add(btnAddNote);
        btnAddNote.setBounds(510, 10, 40, 40);

        cboCategory.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        cboCategory.setMaximumRowCount(5);
        cboCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "មិនកំណត់", "សំខាន់", "ប្រឡង", "កិច្ចការសាលា", "ការងារ", "កិច្ចការផ្ទាល់ខ្លួន", "ខួបកំណើត", "វិស្សមកាល" }));
        cboCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoryActionPerformed(evt);
            }
        });
        pnCreate.add(cboCategory);
        cboCategory.setBounds(380, 70, 170, 30);

        lblColorCategory.setBackground(new java.awt.Color(145, 210, 255));
        lblColorCategory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblColorCategory.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblColorCategory.setOpaque(true);
        pnCreate.add(lblColorCategory);
        lblColorCategory.setBounds(360, 70, 20, 30);

        chbSetTime.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        chbSetTime.setText("កំណត់ម៉ោងរោទិ៍");
        chbSetTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chbSetTimeMouseClicked(evt);
            }
        });
        chbSetTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbSetTimeActionPerformed(evt);
            }
        });
        pnCreate.add(chbSetTime);
        chbSetTime.setBounds(110, 120, 140, 30);
        pnCreate.add(jSeparator1);
        jSeparator1.setBounds(110, 110, 340, 20);
        pnCreate.add(jSeparator2);
        jSeparator2.setBounds(110, 160, 340, 20);

        cboHour.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        cboHour.setMaximumRowCount(5);
        pnCreate.add(cboHour);
        cboHour.setBounds(260, 120, 60, 30);

        cboMinute.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        cboMinute.setMaximumRowCount(5);
        pnCreate.add(cboMinute);
        cboMinute.setBounds(320, 120, 60, 30);

        cboTimeState.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        cboTimeState.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AM", "PM" }));
        pnCreate.add(cboTimeState);
        cboTimeState.setBounds(380, 120, 70, 30);

        btnAddSound.setFont(new java.awt.Font("Khmer OS Content", 0, 12)); // NOI18N
        btnAddSound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/music.png"))); // NOI18N
        btnAddSound.setToolTipText("<html><font face=\"Khmer OS Content\">ជ្រើសរើសបទភ្លេងពីកុំព្យូទ័រ</font></html>");
        btnAddSound.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddSoundMouseClicked(evt);
            }
        });
        pnCreate.add(btnAddSound);
        btnAddSound.setBounds(480, 170, 70, 30);

        lblID.setText("0");
        pnCreate.add(lblID);
        lblID.setBounds(30, 130, 6, 14);

        cboSound.setFont(new java.awt.Font("Khmer OS Content", 0, 15)); // NOI18N
        cboSound.setMaximumRowCount(5);
        cboSound.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSoundItemStateChanged(evt);
            }
        });
        pnCreate.add(cboSound);
        cboSound.setBounds(110, 170, 310, 30);

        jLabel25.setFont(new java.awt.Font("Khmer OS Content", 0, 16)); // NOI18N
        jLabel25.setText("សម្លេងរោទិ៍");
        pnCreate.add(jLabel25);
        jLabel25.setBounds(30, 170, 69, 30);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/stop.png"))); // NOI18N
        btnStop.setToolTipText("<html><font face=\"Khmer OS Content\">បញ្ឈប់ភ្លេង</font></html>");
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        pnCreate.add(btnStop);
        btnStop.setBounds(450, 170, 30, 30);

        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/taskexpert/images/play.png"))); // NOI18N
        btnPlay.setToolTipText("<html><font face=\"Khmer OS Content\">លេងភ្លេង</font></html>");
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        pnCreate.add(btnPlay);
        btnPlay.setBounds(420, 170, 30, 30);

        pnClickToCreate.add(pnCreate);
        pnCreate.setBounds(0, 0, 570, 220);

        add(pnClickToCreate);
        pnClickToCreate.setBounds(10, 50, 570, 220);

        pnTasksToday.setBackground(new java.awt.Color(51, 255, 204));
        pnTasksToday.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnTasksToday.setForeground(new java.awt.Color(102, 102, 102));
        pnTasksToday.setMaximumSize(new Dimension(700,120));
        pnTasksToday.setMinimumSize(new Dimension(570,120));
        pnTasksToday.setOpaque(false);
        pnTasksToday.setPreferredSize(new Dimension(570,120));
        add(pnTasksToday);
        pnTasksToday.setBounds(10, 310, 570, 120);

        jLabel4.setFont(new java.awt.Font("Khmer OS Content", 0, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("ការងារដែលបានបង្កើតថ្ងៃនេះ");
        jLabel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 2, true));
        add(jLabel4);
        jLabel4.setBounds(10, 280, 570, 30);

        lblTotalTasksToday.setFont(new java.awt.Font("Khmer OS Content", 0, 13)); // NOI18N
        lblTotalTasksToday.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalTasksToday.setText("សរុប = 0");
        add(lblTotalTasksToday);
        lblTotalTasksToday.setBounds(500, 280, 70, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddNoteActionPerformed

    private void lblCreateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCreateMouseEntered
        if(lblCreate.isEnabled()){
            lblCreate.setFont(new Font("Khmer OS Content",Font.BOLD,13));
        }
    }//GEN-LAST:event_lblCreateMouseEntered

    private void lblCreateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCreateMouseExited
        lblCreate.setFont(new Font("Khmer OS Content",Font.PLAIN,13));
    }//GEN-LAST:event_lblCreateMouseExited

    private void lblCreateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCreateMouseClicked
        if(lblCreate.isEnabled()){
            
            pnCreate.setVisible(true);
            lblClickToCreate.setVisible(false);
            lblCreate.setEnabled(false);
            lblSave.setEnabled(true);
            lblCancel.setEnabled(true);
            pnAppOpenBlock.setVisible(true);

            tfTitle.setText("");
            tfTitle.requestFocus();
            stnote = "";
            Date d = new Date();
            dpkDate.setDate(d);
            cboCategory.setSelectedIndex(0);
            cboSound.setSelectedIndex(0);

            chbSetTime.setSelected(true);

            pnAppDocOpen.removeAll();
            pnAppDocBlock.removeAll();
            pnAppDocOpen.add(pnAdd[0]);
            pnAppDocBlock.add(pnAdd[1]);
            pnAppDocOpen.revalidate();  pnAppDocOpen.repaint();
            pnAppDocBlock.revalidate(); pnAppDocBlock.repaint();

            cboHourBlock.setSelectedIndex(0);
            cboMinuteBlock.setSelectedIndex(0);
        }
    }//GEN-LAST:event_lblCreateMouseClicked

    private void lblSaveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSaveMouseEntered
        if(lblSave.isEnabled()){
            lblSave.setFont(new Font("Khmer OS Content",Font.BOLD,13));
        }
    }//GEN-LAST:event_lblSaveMouseEntered

    private void lblSaveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSaveMouseExited
        lblSave.setFont(new Font("Khmer OS Content",Font.PLAIN,13));
    }//GEN-LAST:event_lblSaveMouseExited

    private void lblCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCancelMouseEntered
        if(lblCancel.isEnabled()){
            lblCancel.setFont(new Font("Khmer OS Content",Font.BOLD,13));
        }
    }//GEN-LAST:event_lblCancelMouseEntered

    private void lblCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCancelMouseExited
        lblCancel.setFont(new Font("Khmer OS Content",Font.PLAIN,13));
    }//GEN-LAST:event_lblCancelMouseExited

    private void lblCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCancelMouseClicked
        if(lblCancel.isEnabled()){
            lblCreate.setEnabled(true);
            lblSave.setEnabled(false);
            lblCancel.setEnabled(false);
            lblClickToCreate.setVisible(true);
            pnCreate.setVisible(false);
            pnAppOpenBlock.setVisible(false);
            
            if(threadSound.isAlive()){
                threadSound.stop();
            }
            
            btnPlay.setEnabled(true);
            btnStop.setEnabled(false);
        }
    }//GEN-LAST:event_lblCancelMouseClicked

    String stnote;
    private void btnSaveNoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveNoteMouseClicked
        stnote = taNote.getText();
        String message = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>កំណត់ត្រារបស់អ្នកបានរក្សាទុក</FONT></HTML>";
        Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>យល់ព្រម</font></html"};
        ImageIcon noteSavedIcon = createImageIcon("images/noteSaved.png");
        
            /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
        JOptionPane.showOptionDialog(null, message, "", 0, 0, noteSavedIcon, options, options[0]);
        dialogNote.setVisible(false);
    }//GEN-LAST:event_btnSaveNoteMouseClicked

    private void btnCancelNoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelNoteMouseClicked
        dialogNote.dispose();
    }//GEN-LAST:event_btnCancelNoteMouseClicked

    private void cboCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoryActionPerformed
        String stGet = cboCategory.getSelectedItem().toString();
        for(int i=0;i<arrColorCate.size();i++){
            if(stGet.equals(arrColorCate.get(i).stCategory)){
                lblColorCategory.setBackground(arrColorCate.get(i).colorCate);
            }
        }
    }//GEN-LAST:event_cboCategoryActionPerformed

    private void btnCancelRepeatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelRepeatMouseClicked
        dialogRepeat.setVisible(false);
    }//GEN-LAST:event_btnCancelRepeatMouseClicked

    String repeatInfo = "D1";
    private void btnSaveRepeatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveRepeatMouseClicked
        switch (tpRepeat.getSelectedIndex()) {
            case 0:
                repeatInfo = "D" + cboRepeatDaily.getSelectedItem();
                JOptionPane.showMessageDialog(null,repeatInfo);
                break;
            case 1:
                String daysRepeatWeekly = "";
                if(chbMon.isSelected()){daysRepeatWeekly += "Mon";}
                if(chbTue.isSelected()){daysRepeatWeekly += "Tue";}
                if(chbWed.isSelected()){daysRepeatWeekly += "Wed";}
                if(chbThu.isSelected()){daysRepeatWeekly += "Thu";}
                if(chbFri.isSelected()){daysRepeatWeekly += "Fri";}
                if(chbSat.isSelected()){daysRepeatWeekly += "Sat";}
                if(chbSun.isSelected()){daysRepeatWeekly += "Sun";}
                repeatInfo = "W" + cboRepeatWeekly.getSelectedItem() + daysRepeatWeekly;
                JOptionPane.showMessageDialog(null,repeatInfo);
                break;
            case 2:
                repeatInfo = "M" + cboRepeatMonthly.getSelectedItem();
                JOptionPane.showMessageDialog(null,repeatInfo);
                break;
            case 3:
                repeatInfo = "Y" + cboRepeatYearly.getSelectedItem();
                JOptionPane.showMessageDialog(null,repeatInfo);
                break;
            default:
                break;
        }
        dialogRepeat.setVisible(false);
    }//GEN-LAST:event_btnSaveRepeatMouseClicked

    private void lblSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSaveMouseClicked
        
        
        if(lblSave.isEnabled()){
            if(tfTitle.getText().equals("")){
            String message1 = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>អ្នកត្រូវបញ្ជាក់ពីការងាររបស់អ្នក</FONT></HTML>";
            Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>យល់ព្រម</font></html"};
            ImageIcon warningIcon = createImageIcon("images/warning.png");
        
            /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
            JOptionPane.showOptionDialog(null, message1, "", 0, 0, warningIcon, options, options[0]);
            tfTitle.requestFocus();
            return;
            }
            boolean rightDate = true;
            Date currentDate = new Date();
            SimpleDateFormat daFormat = new SimpleDateFormat("dd/MM/yy");
            String curDate = daFormat.format(currentDate);
            Date cDate = new Date();
            try {
                cDate = daFormat.parse(curDate);
            } catch (ParseException ex) {
                Logger.getLogger(CreateTasksPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            Date sDate = dpkDate.getDate();
            if(sDate.before(cDate)){
                String message = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>អ្នកមិនអាចកំណត់កាលបរិច្ឆេទការងារ ឲ្យមុនកាលបរិច្ឆេទបច្ចុប្បន្នបានទេ</FONT></HTML>";
                Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>យល់ព្រម</font></html"};
                ImageIcon warningIcon = createImageIcon("images/warning.png");
        
                /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
                JOptionPane.showOptionDialog(null, message, "", 0, 0, warningIcon, options, options[0]);
                dpkDate.setDate(cDate);
                //rightDate = false;
                return;
            }else if(sDate.equals(cDate)){
                rightDate = false;
                
            }else{
                rightDate = true;
            }
            
            if(chbSetTime.isSelected() && rightDate == false){
                Date currentTime = new Date();
                SimpleDateFormat tiFormat = new SimpleDateFormat("hh:mm aa");
                String curTime = tiFormat.format(currentTime);
                String receiveTime = cboHour.getSelectedItem().toString() + ":" + cboMinute.getSelectedItem().toString() + " " + cboTimeState.getSelectedItem().toString();
                Date cTime = new Date();
                Date receTime = new Date();
                try {
                    cTime = tiFormat.parse(curTime);
                    receTime = tiFormat.parse(receiveTime);
                } catch (ParseException ex) {
                    Logger.getLogger(CreateTasksPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(receTime.before(cTime)){
                String message = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>អ្នកមិនអាចកំណត់ម៉ោងការងារ ឲ្យមុនម៉ោងបច្ចុប្បន្នបានទេ</FONT></HTML>";
                Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>យល់ព្រម</font></html"};
                ImageIcon warningIcon = createImageIcon("images/warning.png");
        
                /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
                JOptionPane.showOptionDialog(null, message, "", 0, 0, warningIcon, options, options[0]);
                dpkDate.setDate(cDate);
                return;
            }
            }
            
        Statement stm;
        try{
            stm = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        
            Date date = new Date();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
                        
            String stTime = cboHour.getSelectedItem().toString() + ":" + cboMinute.getSelectedItem().toString() + " " + cboTimeState.getSelectedItem().toString();
            Date timeTask = timeFormat.parse(stTime);   
            
            String timeTaskSet = timeFormat.format(timeTask);
            if(!chbSetTime.isSelected()){
                timeTaskSet = "";
                
            }
            
            int timeBlockTotal = Integer.parseInt(cboHourBlock.getSelectedItem().toString())*60 + Integer.parseInt(cboMinuteBlock.getSelectedItem().toString());
            
            /////////////////////////////////////////////////////////////////
            String allPathOpen = "";
            int compCountOpen = pnAppDocOpen.getComponentCount();
            if(compCountOpen == 1){
                allPathOpen = "";
            }else if(compCountOpen == 2){
                for(int i=0;i<3;i++){
                    if(pnAppDocOpen.getComponent(0).equals(pnAppDocName[i])){
                        allPathOpen = pnAppDocName[i].path + "<";
                        break;
                    }
                }
            }else{
                for(int i=0;i<3;i++){
                    if(!pnAppDocOpen.getComponent(i).equals(pnAdd[0])){
                        for(int j=0;j<3;j++){
                            if(pnAppDocOpen.getComponent(i).equals(pnAppDocName[j])){
                                allPathOpen = allPathOpen + pnAppDocName[j].path + "<";
                                break;
                            }
                        }
                    }
                }
            }
            //////////////////////////////////////////////////////
            String allPathBlock = "";
            int compCountBlock = pnAppDocBlock.getComponentCount();
            if(compCountBlock == 1){
                allPathBlock = "";
                timeBlockTotal = 0;
            }else if(compCountBlock == 2){
                for(int i=3;i<6;i++){
                    if(pnAppDocBlock.getComponent(0).equals(pnAppDocName[i])){
                        allPathBlock = pnAppDocName[i].path + "<";
                        break;
                    }
                }
            }else{
                for(int i=0;i<3;i++){
                    if(!pnAppDocBlock.getComponent(i).equals(pnAdd[1])){
                        for(int j=3;j<6;j++){
                            if(pnAppDocBlock.getComponent(i).equals(pnAppDocName[j])){
                                allPathBlock = allPathBlock + pnAppDocName[j].path + "<";
                                break;
                            }
                        }
                    }
                }
            }      
            
        
            
            boolean sta = false;
            String stSound = cboSound.getSelectedItem().toString();
            if(stSound.equals("(មិនដាក់សម្លេងរោទិ៍)")){
                stSound = "";
                sta = false;
            }else{
                for(int i=0;i<arrExistSounds.size();i++){
                    if(stSound.equals(arrExistSounds.get(i))){
                        final String dir = System.getProperty("user.dir");  //get project directory
                        stSound = dir + "\\src\\taskexpert\\sounds\\" + stSound + ".mp3";
                        sta = false;
                        break;
                    }else{
                        sta = true;
                    }
                }
            }
            if(sta==true){
                try{
                    FileReader frSound = new FileReader("soundNamePath.txt");
                    BufferedReader br = new BufferedReader(frSound);
            
                    File fTest;
            
                    String line;
                    while((line = br.readLine()) != null){
                        fTest = new File(line);
                        if(stSound.equals(fTest.getName().replaceFirst("[.][^.]+$", ""))){
                            stSound = fTest.getAbsolutePath();
                            break;
                        }
                    }
                    br.close();
                    frSound.close();
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }
         
            boolean doneState = false;
            
            int ID = Integer.parseInt(lblID.getText());
             
            String sqlUpdate = "UPDATE tbTasks SET createDate='" + dateFormat.format(date) + "',title='" + tfTitle.getText() + "',notes='" + stnote + "',category='" + cboCategory.getSelectedItem() + "',sounds='" + stSound + "',taskDate='" + dateFormat.format(dpkDate.getDate()) + "',times='" + timeTaskSet + "',appToOpen='" + allPathOpen + "',appToBlock='" + allPathBlock + "',timeForBlock=" + timeBlockTotal + ",doneStatus=" + doneState + " WHERE taskID=" + ID;
            
            String sqlInsert = "Insert into tbTasks(createDate,title,notes,category,sounds,taskDate,times,appToOpen,appToBlock,timeForBlock,doneStatus) "
                            + "values('" + dateFormat.format(date) + "','" + tfTitle.getText() + "','" + stnote 
                            + "','" + cboCategory.getSelectedItem() + "','" + stSound 
                            + "','" + dateFormat.format(dpkDate.getDate()) + "','" + timeTaskSet 
                            + "','" + allPathOpen + "','" + allPathBlock 
                            + "'," + timeBlockTotal + "," + doneState + ");";
            
            boolean haveID = false;
            for(int i=0;i<MainForm.listTask.size();i++){
                if(MainForm.listTask.get(i).taID == ID){
                    haveID = true;
                    break;
                }else{
                    haveID = false;
                }
            }
            
            if(haveID==true){
                stm.executeUpdate(sqlUpdate);
                
            }else{
                stm.executeUpdate(sqlInsert);
            }
            lblID.setText("0");
            
            stm.close();
            
            //update arraylist
            Statement stmt;
        ResultSet rs;
        try{
            stmt = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT * FROM tbTasks WHERE doneStatus=false");
            MainForm.listTask.clear();
            while(rs.next()){
                MainForm.listTask.add(new Task(Integer.parseInt(rs.getString("taskID")), rs.getString("createDate"),
                        rs.getString("title"), rs.getString("notes"), rs.getString("category"),
                        rs.getString("sounds"), rs.getString("taskDate"), rs.getString("times"),
                        rs.getString("appToOpen"), rs.getString("appToBlock"),
                        Integer.parseInt(rs.getString("timeForBlock")), Boolean.parseBoolean(rs.getString("doneStatus"))));
            }
            rs.close();
            stmt.close();
        }catch(Exception exc){
            System.out.println("From function listTaskUpdate(): " + exc.getMessage());
        }
        
        ArrayList<PanelTaskToday> pnTToday = new ArrayList<>();
        PanelTaskToday pnTT;
        try{
            stmt = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT * FROM tbTasks WHERE createDate=Date() AND doneStatus=false");
            
            
            while(rs.next()){
                int tid = Integer.parseInt(rs.getString("taskID"));
                String tcreatedate = rs.getString("createDate");
                String ttitle = rs.getString("title");
                String tnote = rs.getString("notes");
                String tcategory = rs.getString("category");
                String tsound = rs.getString("sounds");
                String ttaskdate = rs.getString("taskDate");
                
                String ttime = rs.getString("times");
                
                String tapptoopen = rs.getString("appToOpen");
                String tapptoblock = rs.getString("appToBlock");
                int ttimeforblock = Integer.parseInt(rs.getString("timeForBlock"));
                boolean tdonestatus = Boolean.parseBoolean(rs.getString("doneStatus"));
                
                SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d = sdformat.parse(tcreatedate);
                SimpleDateFormat sdformat1 = new SimpleDateFormat("dd/MM/yy");
                
                Color c = new Color(0,0,0);
                for(int i=0;i<arrColorCate.size();i++){
                    if(tcategory.equals(arrColorCate.get(i).stCategory)){
                        c = arrColorCate.get(i).colorCate;
                    }
                }
                pnTT = new PanelTaskToday(ttitle,c,tid,tcreatedate,ttitle,tnote,tcategory,tsound,ttaskdate,ttime,
                                            tapptoopen,tapptoblock,ttimeforblock,tdonestatus);
                pnTToday.add(pnTT);
                
            }
            rs.close();
            stmt.close();
            
        }catch(Exception exc){
            exc.printStackTrace();
        }

            pnScrollPane.removeAll();
            taskTodayCount = 0;
            for(int i=0;i<pnTToday.size();i++){
                pnScrollPane.add(pnTToday.get(i));
                scrollPane.revalidate();
                
                lblTotalTasksToday.setText("សរុប = " + (taskTodayCount+1));
                taskTodayCount++;
            }

            String message = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>ការកត់ត្រាការងារបានជោគជ័យ</FONT></HTML>";
            Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>យល់ព្រម</font></html"};
            ImageIcon successfulIcon = createImageIcon("images/successful.png");
        
            /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
            JOptionPane.showOptionDialog(null, message, "", 0, 0, successfulIcon, options, options[0]);
            
            pnCreate.setVisible(false);
            pnAppOpenBlock.setVisible(false);
            lblClickToCreate.setVisible(true);
            lblCancel.setEnabled(false);
            lblSave.setEnabled(false);
            lblCreate.setEnabled(true);
            
            if(threadSound.isAlive()){
                threadSound.stop();
            }
            btnPlay.setEnabled(true);
            btnStop.setEnabled(false);
            
            MainForm.pnViewTasks = new ViewTasksPanel();
        
        }catch(Exception e){
            String message1 = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>សូមអធ្យាស្រ័យ ការកត់ត្រាការងារមិនបានជោគជ័យទេ</FONT></HTML>";
            Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>ព្យាយាមម្ដងទៀត</font></html"};
            ImageIcon unsuccessfulIcon = createImageIcon("images/unsuccessful.png");
        
            /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
            JOptionPane.showOptionDialog(null, message1, "", 0, 0, unsuccessfulIcon, options, options[0]);
        }
      }
    }//GEN-LAST:event_lblSaveMouseClicked

    private void chbSetTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chbSetTimeMouseClicked
        //use this checkbox with MouseListener is not good as with ActionListener
        if(!chbSetTime.isSelected()){
            cboHour.setEnabled(false);
            cboMinute.setEnabled(false);
            cboTimeState.setEnabled(false);

        }else{
            cboHour.setEnabled(true);
            cboMinute.setEnabled(true);
            cboTimeState.setEnabled(true);


        }
    }//GEN-LAST:event_chbSetTimeMouseClicked

    private void chbSetTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbSetTimeActionPerformed
        if(!chbSetTime.isSelected()){
            cboHour.setEnabled(false);
            cboMinute.setEnabled(false);
            cboTimeState.setEnabled(false);
            
            String message1 = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>បើអ្នកមិនកំណត់ម៉ោងរោទិ៍ទេ ការងារនេះនឹងគ្រាន់តែត្រូវបានកត់ត្រាទុក<br>ហើយនឹងមិនត្រូវបានរំលឹកឡើយ</FONT></HTML>";
            Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>យល់ព្រម</font></html"};
            ImageIcon informationIcon = createImageIcon("images/information.png");
        
            /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
            JOptionPane.showOptionDialog(null, message1, "", 0, 0, informationIcon, options, options[0]);
        }else{
            cboHour.setEnabled(true);
            cboMinute.setEnabled(true);
            cboTimeState.setEnabled(true);
            

        }
    }//GEN-LAST:event_chbSetTimeActionPerformed

    private void btnAddNoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddNoteMouseClicked
        
        if(stnote.equals("")){
            taNote.setText("");
        }else{
            taNote.setText(stnote);
        }
        dialogNote.setVisible(true);
    }//GEN-LAST:event_btnAddNoteMouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        
    }//GEN-LAST:event_jLabel8MouseClicked

    private void cboRepeatMonthlyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboRepeatMonthlyItemStateChanged
        
    }//GEN-LAST:event_cboRepeatMonthlyItemStateChanged

    private void btnAddSoundMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddSoundMouseClicked
        JFileChooser fc = new JFileChooser();
        fc.setBackground(new Color(230,255,255));
        fc.addChoosableFileFilter(new FileNameExtensionFilter("mp3 (.mp3)", "mp3"));
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            try {
                FileWriter fSound = new FileWriter("soundNamePath.txt",true); //'true' means to append the file
                PrintWriter pw = new PrintWriter(fSound);
                pw.println(fc.getSelectedFile().getAbsolutePath());
                pw.close();
                fSound.close();
            } catch (IOException ex) {
                Logger.getLogger(CreateTasksPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            String nameSound = fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", "");
            cboSound.addItem(nameSound);
            cboSound.setSelectedItem(nameSound);
        }
    }//GEN-LAST:event_btnAddSoundMouseClicked

    
    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        
        
        threadSound = new Thread(new Runnable(){
            @Override
            public void run(){
                
                boolean sta = false;
                String stSound = cboSound.getSelectedItem().toString();
                if(stSound.equals("(មិនដាក់សម្លេងរោទិ៍)")){
                   stSound = "";
                    sta = false;
                }else{
                    for(int i=0;i<arrExistSounds.size();i++){
                        if(stSound.equals(arrExistSounds.get(i))){
                            final String dir = System.getProperty("user.dir");  //get project directory
                            stSound = dir + "\\src\\taskexpert\\sounds\\" + stSound + ".mp3";
                            sta = false;
                            break;
                        }else{
                            sta = true;
                        }
                    }
                }
                if(sta==true){
                    try{
                        FileReader frSound = new FileReader("soundNamePath.txt");
                        BufferedReader br = new BufferedReader(frSound);
            
                        File fTest;
            
                        String line;
                        while((line = br.readLine()) != null){
                            fTest = new File(line);
                            if(stSound.equals(fTest.getName().replaceFirst("[.][^.]+$", ""))){
                                stSound = fTest.getAbsolutePath();
                                break;
                            }
                        }
                        br.close();
                        frSound.close();
                    }catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                }
                
                try{
                    File mp3;
                    File f = new File(stSound);
                    if(f.exists()){
                        mp3 = new File(stSound);
                    }else{
                        String message1 = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4>កម្មវិធីជំនួយការឆ្លាតវៃមិនអាចរកឃើញឯកសារសម្លេង ឬបទចម្រៀងនេះទេ។<br><br>" + cboSound.getSelectedItem().toString() + "</FONT></HTML>";
                        Object[] options= new Object[]{"<html><font face=\"Khmer OS\" size=3>យល់ព្រម</font></html"};
                        ImageIcon warningIcon = createImageIcon("images/warning.png");
        
                        /*Method showOptionDialog(Component parentComponent, 
                                    Object message, 
                                    String title, 
                                    int optionType, 
                                    int messageType,
                                    Icon icon, 
                                    Object[] options, 
                                    Object selectedValue)*/
                        JOptionPane.showOptionDialog(null, message1, "", 0, 0, warningIcon, options, options[0]);
                        btnPlay.setEnabled(true);
                        btnStop.setEnabled(false);
                        return;
                    }
                            
                    //File mp3 = new File("E:\\RUPP IT\\302\\Java\\TaskExpert\\src\\taskexpert\\sounds\\Moves Like Jagger.mp3");
                            
                    FileInputStream fis = new FileInputStream(mp3);
                    BufferedInputStream bis = new BufferedInputStream(fis);                           
                    Player player = new Player(bis);                          
                       
                    
//                    AudioFile audioFile = AudioFileIO.read(mp3);
//                    duration = audioFile.getAudioHeader().getTrackLength();
                    
                    //JOptionPane.showMessageDialog(null, duration);
                    
                    player.play();
                    
                    while(player.isComplete()){
                        player.close();
                        btnPlay.setEnabled(true);
                        btnStop.setEnabled(false);
                        break;
                    }
                    
                    
       
                }catch(Exception e){
                    //JOptionPane.showMessageDialog(null, "From ThreadCheck" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        
        btnPlay.setEnabled(false);
        btnStop.setEnabled(true);
        
        threadSound.start();
        
//        try{
//            Thread.sleep(duration * 1000);
//        }catch(InterruptedException e){
//            e.printStackTrace();
//        }
//        
//        threadSound.stop();
//        if(!threadSound.isAlive()){
//            btnPlay.setEnabled(true);
//            btnStop.setEnabled(false);
//        }
//        try {
//            threadSound.wait();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(CreateTasksPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if(!threadSound.isAlive()){
//            btnPlay.setEnabled(true);
//            btnStop.setEnabled(false);
//        }
       // btnPlayActionPerformed(evt);
        
        
        
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        threadSound.stop();
        btnPlay.setEnabled(true);
        btnStop.setEnabled(false);
    }//GEN-LAST:event_btnStopActionPerformed

    boolean stateSoundSelected = false;
    private void cboSoundItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSoundItemStateChanged
        String stSound = cboSound.getSelectedItem().toString();
        if(stSound.equals("(មិនដាក់សម្លេងរោទិ៍)")){
            btnPlay.setEnabled(false);
            btnStop.setEnabled(false);
            stateSoundSelected = false;
            if(threadSound.isAlive()){
                threadSound.stop();
            }
        }else{
            if(stateSoundSelected==false){
                btnPlay.setEnabled(true);
                btnStop.setEnabled(false);
                stateSoundSelected = true;
            }
            else{
                
            }
        }
    }//GEN-LAST:event_cboSoundItemStateChanged

    
    class PanelAddAppDoc extends JPanel{
        PanelAddAppDoc(){
            setLayout(new BorderLayout());
            
            setBackground(pnAppDocOpen.getBackground());
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            
            Dimension size = new Dimension(280,40);
            setMinimumSize(size);
            setPreferredSize(size);
            setMaximumSize(size);
            
            ImageIcon addIcon = createImageIcon("images/add.png");
            JLabel lblAdd = new JLabel("បញ្ចូល",addIcon,JLabel.CENTER);
            lblAdd.setIconTextGap(10);
            lblAdd.setFont(new Font("Khmer OS Content",Font.PLAIN,14));
            
            add(lblAdd,BorderLayout.CENTER);   
        }
    }
    
    class PanelAppDocName extends JPanel{
        JLabel lblChangeIcon;
        JLabel lblDeleteIcon;
        JLabel lblNameAppDoc;
        String path;
           
        public JLabel getlblChangeIcon(){return lblChangeIcon;}
        public JLabel getlblDeleteIcon(){return lblDeleteIcon;}
        
        int beginIndex = 0;
        int endIndex = 0;
        public void setPath(String p){
            path = p;
            
            for(int i=path.length()-1;i>=0;i--){
                if(path.charAt(i) == '\\'){
                    beginIndex = i+1;
                    endIndex = path.length();
                    break;
                }else{
                    endIndex = path.length()-1;
                }
            }
            lblNameAppDoc.setText(path.substring(beginIndex, endIndex));
        }
                
        PanelAppDocName(){
            setLayout(new BorderLayout());
            
            setBackground(new Color(205,215,200));
            setBorder(BorderFactory.createRaisedBevelBorder());
            
            Dimension size = new Dimension(280,40);
            setMinimumSize(size);
            setPreferredSize(size);
            setMaximumSize(size);
            
            lblNameAppDoc = new JLabel(path);
            lblNameAppDoc.setBackground(new Color(205,215,200));
            lblNameAppDoc.setOpaque(true);
            lblNameAppDoc.setFont(new Font("Khmer OS Content",Font.PLAIN,13));
            lblNameAppDoc.setSize(new Dimension(195,30));
            
            
            ImageIcon changeIcon = createImageIcon("images/change.png");
            lblChangeIcon = new JLabel(changeIcon,JLabel.CENTER);
            lblChangeIcon.setToolTipText("<html><font face=\"Khmer OS Content\" Size=3>ផ្លាស់ប្តូរពីបើកទៅបិទ ឬពីបិទទៅបើក</font></html>");
            
            ImageIcon deleteIcon = createImageIcon("images/delete.png");
            lblDeleteIcon = new JLabel(deleteIcon,JLabel.CENTER);
            lblDeleteIcon.setToolTipText("<html><font face=\"Khmer OS Content\" Size=3>លុប</font></html>");
            
            JPanel pnControl = new JPanel();
            pnControl.setBackground(new Color(205,215,200));
            pnControl.add(lblChangeIcon);
            pnControl.add(lblDeleteIcon);
            
            JPanel pnFarLeftGap = new JPanel();
            pnFarLeftGap.setBackground(new Color(205,215,200));
            pnFarLeftGap.setSize(new Dimension(10,40));
            
            add(pnFarLeftGap,BorderLayout.WEST);
            add(lblNameAppDoc,BorderLayout.CENTER);
            add(pnControl,BorderLayout.EAST);
            
        }
    }
    
    class PanelTaskToday extends JPanel{
        int tID;
        String tCreateDate;
        String tTitle;
        String tNote;
        String tCategory;
        String tSound;
        String tTaskDate;
        
        String tTime;
        
        String tAppToOpen;
        String tAppToBlock;
        int tTimeForBlock;
        boolean tDoneStatus;
        
        
        JLabel lblTaskTitle;
        JLabel lblDeleteTask;
        JLabel lblEditTask;
        Color colorBackground;
        
        PanelTaskToday(String stToShow,Color c,int id,String td,String tt,String tn,String tc,String ts,String ttd
                        ,String ttm,String tato,String tatb,int ttfb,boolean tds){
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(170,85));
            setMinimumSize(this.getPreferredSize());
            setMaximumSize(this.getPreferredSize());
            setBackground(c);
            colorBackground = c;
            tID = id;   tCreateDate=td;   tTitle=tt;  tNote=tn;   tCategory=tc;   tSound=ts;  tTaskDate=ttd;
            tTime=ttm;     tAppToOpen=tato;
            tAppToBlock=tatb;   tTimeForBlock=ttfb; tDoneStatus=tds;
          
            lblTaskTitle = new JLabel("<html>" + stToShow+ "</html>");
            lblTaskTitle.setPreferredSize(new Dimension(90,80));
            lblTaskTitle.setMaximumSize(lblTaskTitle.getPreferredSize());
            lblTaskTitle.setFont(new Font("Khmer OS Content",Font.PLAIN,14));
            
            
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
            
            Date dT = new Date();
            Date tT = new Date();
            try{
                ///////TASK DATE
                dT = forDate.parse(tTaskDate);
                //////TASK TIME
                tT = forTime.parse(tTime);
            }catch(ParseException ex){
                Logger.getLogger(CreateTasksPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            
             
            ImageIcon edit = createImageIcon("images/edit.png");
            lblEditTask = new JLabel(edit);
            lblEditTask.setOpaque(false);
            lblEditTask.setToolTipText("<html><font face=\"Khmer OS Content\" Size=3>កែប្រែ</font></html>");
            ImageIcon delete = createImageIcon("images/delete1.png");
            lblDeleteTask = new JLabel(delete);
            lblDeleteTask.setOpaque(false);
            lblDeleteTask.setToolTipText("<html><font face=\"Khmer OS Content\" Size=3>លុប</font></html>");
            
            JPanel pnControl = new JPanel();
            pnControl.setLayout(new BorderLayout());
            Color color = this.getBackground();
            pnControl.setBackground(new Color(color.getRed(),color.getGreen()-50,color.getBlue()));
            pnControl.add(lblEditTask,BorderLayout.WEST);
            pnControl.add(lblDeleteTask,BorderLayout.EAST);
            
            JPanel pnFarLeftGap = new JPanel();
            pnFarLeftGap.setBackground(this.getBackground());
            pnFarLeftGap.setPreferredSize(new Dimension(5,80));
            pnFarLeftGap.setMinimumSize(pnFarLeftGap.getPreferredSize());
            pnFarLeftGap.setMaximumSize(pnFarLeftGap.getPreferredSize());
            
            add(pnFarLeftGap,BorderLayout.WEST);
            add(lblTaskTitle,BorderLayout.CENTER);
            add(pnControl,BorderLayout.SOUTH);
            
            lblEditTask.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mousePressed(MouseEvent me){
//                    lblEditTask.setOpaque(true);
//                    lblEditTask.setBackground(Color.WHITE);
//                }
//                @Override
//                public void mouseReleased(MouseEvent me){
//                    lblEditTask.setOpaque(false);
//                }
                @Override
                public void mouseClicked(MouseEvent me){
//                    lblEditTask.setOpaque(true);
//                    lblEditTask.setBackground(Color.DARK_GRAY);
                    lblID.setText(tID + "");
                    
                    lblCreateMouseClicked(me);
                    tfTitle.setText(tTitle);
                    stnote = tNote;
                    
                    SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yy");
                    try{    
                        dpkDate.setDate(sdformat.parse(tTaskDate));
                    } catch (ParseException ex) {
                        Logger.getLogger(CreateTasksPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    cboCategory.setSelectedItem(tCategory);
                    if(tSound.equals("")){
                        cboSound.setSelectedItem("(មិនដាក់សម្លេងរោទិ៍)");
                    }else{
                        //cboSound.setSelectedItem(tSound);
                        File fTest = new File(tSound);
                        cboSound.setSelectedItem(fTest.getName().replaceFirst("[.][^.]+$", ""));
                    }
                    
                    if(tTime.equals("")){
                        chbSetTime.setSelected(false);
                        chbSetTimeMouseClicked(me);
                    }else{
                        chbSetTime.setSelected(true);
                        chbSetTimeMouseClicked(me);
                        cboHour.setSelectedIndex(Integer.parseInt(tTime.substring(0, 2))-1);
                        cboMinute.setSelectedItem(tTime.substring(3,5));
                        cboTimeState.setSelectedItem(tTime.substring(6,8));
                    }
                    
                    pnAppDocOpen.removeAll();
                    pnAppDocBlock.removeAll();
                    pnAppDocOpen.add(pnAdd[0]);
                    pnAppDocBlock.add(pnAdd[1]);
                    pnAppDocOpen.revalidate();  pnAppDocOpen.repaint();
                    pnAppDocBlock.revalidate(); pnAppDocBlock.repaint();
                    
                    if(!tAppToOpen.equals("")){
                        String[] stOpen = tAppToOpen.split("<");    //after split, String "" is auto deleted 
                        for(int i=0;i<stOpen.length;i++){
                            pnAddZeroMouseClicked(stOpen[i]);
                        }
                    }
                    if(!tAppToBlock.equals("")){
                        String[] stBlock = tAppToBlock.split("<");
                        for(int i=0;i<stBlock.length;i++){
                            pnAddOneMouseClicked(stBlock[i]);
                        }
                        cboHourBlock.setSelectedIndex((tTimeForBlock/60));
                        cboMinuteBlock.setSelectedIndex((tTimeForBlock%60)-1);
                    }
                    
                    
                }
            });
            
            lblDeleteTask.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent me){
                    
                    String message = "<HTML><FONT FACE=\"Khmer OS Content\" SIZE=4><p>តើអ្នកពិតជាចង់លុបការងារនេះមែនឬទេ?<br><br>" + tTitle + "<br><br>បើអ្នកចុច <b>បាទ ឬចាស</b> នោះការងារនេះនឹងត្រូវបានលុបចេញពីកម្មវិធី។</p></FONT></HTML>";
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
                    
                        String sqlInsertToDelete = "Insert into tbDeletedTasks(taskID,createDate,title,notes,category,sounds,taskDate,times,appToOpen,appToBlock,timeForBlock,doneStatus) "
                                + "values(" + tID + ",'" + tCreateDate + "','" + tTitle + "','" + tNote 
                                + "','" + tCategory + "','" + tSound 
                                + "','" + tTaskDate + "','" + tTime 
                                + "','" + tAppToOpen + "','" + tAppToBlock 
                                + "'," + tTimeForBlock + "," + tDoneStatus + ");";
                    
                        try{
                            stmt = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            stmt.executeUpdate(sqlInsertToDelete);
                            stmt.close();
                        }catch(Exception e){
                            JOptionPane.showMessageDialog(null, "From insert to delete query" + e.getMessage());
                        }
                    
                    
                        String sqlDelele = "DELETE FROM tbTasks WHERE taskID=" + tID;
                        try{
                            stmt = MainForm.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            stmt.executeUpdate(sqlDelele);
                            stmt.close();
                        }catch(Exception e){
                            JOptionPane.showMessageDialog(null, "From delete query" + e.getMessage());
                        }
                    
                    
                        CreateTasksPanel pnCrTask = new CreateTasksPanel();
                        pnCrTask.setBackground(new Color(230,255,255));
                        MainForm.pnDisplay.removeAll();
                        MainForm.pnDisplay.add(pnCrTask, BorderLayout.CENTER);
                        MainForm.pnDisplay.revalidate();
                        MainForm.pnDisplay.repaint(); 
                    }
                }
            });
        }
    }

    
    class ColorCategory{
        int colorID;
        String stCategory;
        Color colorCate;
        
        ColorCategory(int i,String st,Color c){
            colorID = i;
            stCategory = st;
            colorCate = c;
        }
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CreateTasksPanel.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNote;
    private javax.swing.JButton btnAddSound;
    private javax.swing.JButton btnCancelNote;
    private javax.swing.JButton btnCancelRepeat;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnSaveNote;
    private javax.swing.JButton btnSaveRepeat;
    private javax.swing.JButton btnStop;
    private javax.swing.JComboBox<String> cboCategory;
    private javax.swing.JComboBox<String> cboHour;
    private javax.swing.JComboBox<String> cboHourBlock;
    private javax.swing.JComboBox<String> cboMinute;
    private javax.swing.JComboBox<String> cboMinuteBlock;
    private javax.swing.JComboBox<String> cboRepeatDaily;
    private javax.swing.JComboBox<String> cboRepeatMonthly;
    private javax.swing.JComboBox<String> cboRepeatWeekly;
    private javax.swing.JComboBox<String> cboRepeatYearly;
    private javax.swing.JComboBox<String> cboSound;
    private javax.swing.JComboBox<String> cboTimeState;
    private javax.swing.JCheckBox chbFri;
    private javax.swing.JCheckBox chbMon;
    private javax.swing.JCheckBox chbSat;
    private javax.swing.JCheckBox chbSetTime;
    private javax.swing.JCheckBox chbSun;
    private javax.swing.JCheckBox chbThu;
    private javax.swing.JCheckBox chbTue;
    private javax.swing.JCheckBox chbWed;
    private javax.swing.JDialog dialogNote;
    private javax.swing.JDialog dialogOpenSoundFile;
    private javax.swing.JDialog dialogRepeat;
    private org.jdesktop.swingx.JXDatePicker dpkDate;
    private javax.swing.JFileChooser fchSound;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    static javax.swing.JLabel lblCancel;
    static javax.swing.JLabel lblClickToCreate;
    private javax.swing.JLabel lblColorCategory;
    static javax.swing.JLabel lblCreate;
    private javax.swing.JLabel lblID;
    static javax.swing.JLabel lblSave;
    private javax.swing.JLabel lblTotalTasksToday;
    private javax.swing.JPanel pnAppDocBlock;
    private javax.swing.JPanel pnAppDocOpen;
    static javax.swing.JPanel pnAppOpenBlock;
    private javax.swing.JPanel pnClickToCreate;
    static javax.swing.JPanel pnCreate;
    private javax.swing.JPanel pnTasksToday;
    private javax.swing.JTextArea taNote;
    private javax.swing.JTextField tfTitle;
    private javax.swing.JTabbedPane tpRepeat;
    // End of variables declaration//GEN-END:variables
}


