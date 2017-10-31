/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;


import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
/**
 *
 * @author TVY
 */
public class SystemTrayUtils {
    public static void createSystemTrayIcon(){
        if(SystemTray.isSupported()){
            SystemTray tray = SystemTray.getSystemTray();
            ImageIcon icon = createImageIcon("images/logo.png");
            Image imag = icon.getImage();
            //Image image = Toolkit.getDefaultToolkit().getImage(SystemTray.class.getResource("images/delete.png"));
            
            PopupMenu popup = new PopupMenu();
            final MenuItem menuOpen = new MenuItem("Open");
            final MenuItem menuExit = new MenuItem("Close");
            
            //menuExit.setFont(new Font("Khmer OS",Font.PLAIN,8));
            //menuExit.setLabel("បិទកម្មវិធី");
            
           
            menuOpen.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae){
                    MainForm.mainForm.setVisible(true);
                }
            });
            
            menuExit.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae){
                    Runtime r = Runtime.getRuntime();
                    r.exit(0);
                }
            });
            
            popup.add(menuOpen);
            popup.add(menuExit);
            
            //final TrayIcon trayIcon = new TrayIcon(imag,"<html><font face=\"Khmer OS Content\" size=5>ជំនួយការឆ្លាតវៃ</font></html>",popup);
            final TrayIcon trayIcon = new TrayIcon(imag,"ជំនួយការឆ្លាតវៃ",popup);
            ActionListener actionListener = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    MainForm.mainForm.setVisible(true);
                }
            };
            
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            //trayIcon.addMouseListener(new MouseAdapter(){});
            
            try{
                tray.add(trayIcon);
            }catch(AWTException e){
                e.printStackTrace();
            }
        }else{
            System.err.println("System Tray is not supported");
        }
    }
    public static void main(String[] args){
        //SystemTrayUtils.createSystemTrayIcon();
        //new MainForm();
    }
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SystemTrayUtils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
