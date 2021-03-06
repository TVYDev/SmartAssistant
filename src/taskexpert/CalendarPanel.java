/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
/**
 *
 * @author TVY
 */
public class CalendarPanel extends javax.swing.JPanel {

    /**
     * Creates new form CreateTasksPanel
     */
    Calendar cal = Calendar.getInstance();
    DefaultTableModel model = new DefaultTableModel();
    public CalendarPanel() {
        initComponents();
        setSize(930, 480); 
        SwingCalendar cl = new SwingCalendar();
        cl.updateMonth();
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        JLABEL1 = new javax.swing.JLabel();
        JLABEL2 = new javax.swing.JLabel();
        JLABEL3 = new javax.swing.JLabel();
        JLABEL4 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 204, 153));
        setPreferredSize(new java.awt.Dimension(930, 480));
        setLayout(null);

        jPanel1.setBackground(new java.awt.Color(230, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 51), 3));
        jPanel1.setLayout(new java.awt.BorderLayout());
        add(jPanel1);
        jPanel1.setBounds(0, 0, 930, 40);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 51)));
        jPanel2.setLayout(new java.awt.BorderLayout());
        add(jPanel2);
        jPanel2.setBounds(0, 40, 660, 440);

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setOpaque(false);
        jPanel3.setLayout(null);

        JLABEL1.setFont(new java.awt.Font("Khmer OS Content", 2, 14)); // NOI18N
        JLABEL1.setForeground(java.awt.Color.red);
        JLABEL1.setText("1");
        jPanel3.add(JLABEL1);
        JLABEL1.setBounds(0, 0, 270, 60);

        JLABEL2.setFont(new java.awt.Font("Khmer OS Content", 2, 14)); // NOI18N
        JLABEL2.setForeground(java.awt.Color.red);
        JLABEL2.setText("2");
        jPanel3.add(JLABEL2);
        JLABEL2.setBounds(0, 80, 270, 60);

        JLABEL3.setFont(new java.awt.Font("Khmer OS Content", 2, 14)); // NOI18N
        JLABEL3.setForeground(java.awt.Color.red);
        JLABEL3.setText("3");
        jPanel3.add(JLABEL3);
        JLABEL3.setBounds(0, 160, 270, 60);

        JLABEL4.setFont(new java.awt.Font("Khmer OS Content", 2, 14)); // NOI18N
        JLABEL4.setForeground(java.awt.Color.red);
        JLABEL4.setText("4");
        jPanel3.add(JLABEL4);
        JLABEL4.setBounds(0, 240, 270, 60);

        add(jPanel3);
        jPanel3.setBounds(660, 40, 270, 440);
    }// </editor-fold>//GEN-END:initComponents

class SwingCalendar extends JPanel {
    JLabel b1, b2;
    DefaultTableModel model;
    Calendar cal = new GregorianCalendar();
    JLabel label;
    SwingCalendar(){
        
        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icon0 = createImageIcon("images/Previous.png");
        b1 = new JLabel(icon0);
        b1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ae){
                cal.add(Calendar.MONTH, -1);
                updateMonth();
            }
        });
        ImageIcon icon = createImageIcon("images/Next.png");
        b2 = new JLabel(icon);
        b2.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ae){
                cal.add(Calendar.MONTH, +1);
                updateMonth();
            }
        });
        jPanel1.add(b1, BorderLayout.WEST);
        jPanel1.add(label, BorderLayout.CENTER);
        jPanel1.add(b2, BorderLayout.EAST);
        
        String [] columns = {"អាទិត្យ​","ច័ន្ទ","អង្គារ","ពុធ","ព្រហស្បតិ៍","សុក្រ","សៅរ៍"};
        model = new DefaultTableModel(null,columns){
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex){
                return false;
            }
         };
        
        JTable table = new JTable(model){
            //To apply cell text color only for column0 & column6
            @Override
            public Class<?> getColumnClass(int column) {
                if(convertColumnIndexToModel(column)==0 || convertColumnIndexToModel(column)==6) return Double.class;
                return super.getColumnClass(column);
            }
        }; 

        //Justify Column Header
        TableCellRenderer rendererFromHeader = table.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel)rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);        
        
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setFont(new Font("Khmer OS Content",0, 15));        
        table.setRowHeight(63);        
        table.getTableHeader().setReorderingAllowed(false);        
        table.setRowSelectionAllowed(false);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.GRAY);
        
        //To apply cell text color
        table.setDefaultRenderer(Double.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
                Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                c.setForeground(Color.RED);
                return c;
            }
        });
        
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createEmptyBorder());
        
        jPanel2.add(pane, BorderLayout.CENTER);
    }
    void updateMonth() {
        cal.set(Calendar.DAY_OF_MONTH, 1); 
        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        int year = cal.get(Calendar.YEAR);
        label.setFont(new Font("Khmer OS Content", 0, 15));
        if(month=="January")            
            label.setText("មករា" + ", " + year);
        if(month=="February")            
            label.setText("កុម្ភៈ" + ", " + year);
        if(month=="March")            
            label.setText("មីនា" + ", " + year);
        if(month=="April")            
            label.setText("មេសា" + ", " + year);
        if(month=="May")            
            label.setText("ឧសភា" + ", " + year);
        if(month=="June")            
            label.setText("មិថុនា" + ", " + year);
        if(month=="July")            
            label.setText("កក្កដា" + ", " + year);
        if(month=="August")            
            label.setText("សីហា" + ", " + year);
        if(month=="September")            
            label.setText("កញ្ញា" + ", " + year);
        if(month=="October")            
            label.setText("តុលា" + ", " + year);
        if(month=="November")            
            label.setText("វិច្ឆិកា" + ", " + year);
        if(month=="December")
            label.setText("ធ្នូ" + ", " + year);
        if(month=="January" && year==2017){
            JLABEL1.setText("០១ ទិវា​ចូល​ឆ្នាំ​សកល");
            JLABEL2.setText("០៧ ទិវា​ជ័យជំនះ​លើ​របប​ប្រល័យ​ពូជ​សាសន៍");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        if(month=="February" && year==2017){
            JLABEL1.setText("១១ ពិធី​បុណ្យ​មាឃ​បូជា");
            JLABEL2.setText("");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        if(month=="March" && year==2017){
            JLABEL1.setText("០៨ ទិវា​នារី​អន្តរជាតិ");
            JLABEL2.setText("");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        if(month=="April" && year==2017){
            JLABEL1.setText("១៤-១៥-១៦ ពិធី​បុណ្យ​ចូល​ឆ្នាំ​ថ្មី");
            JLABEL2.setText("");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        if(month=="May" && year==2017){
            JLABEL1.setText("០១ ទិវា​ពលកម្ម​អន្តរជាតិ");
            JLABEL2.setText("១០ ពិធីបុណ្យ​វិសាខបូជា");
            JLABEL3.setText("<html>១៣-១៤-១៥ ព្រះ​រាជ​ពិធី​បុណ្យ​ចម្រើន​ព្រះ​ជន្មព្រះ​មហាក្សត្រ​</html>");
            JLABEL4.setText("១៤ ព្រះ​រាជ​ពិធី​ច្រត់​ព្រះ​នង្គ័ល");
        }
        if(month=="June" && year==2017){
            JLABEL1.setText("០១ ទិវា​កុមារ​អន្តរ​ជាតិ");
            JLABEL2.setText("១៨ ព្រះ​រាជ​ពិធី​បុណ្យ​ចម្រើន​ព្រះ​ជន្មសម្តេចម៉ែ");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        if(month=="July" && year==2017){
            JLABEL1.setText("");
            JLABEL2.setText("");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        if(month=="August" && year==2017){
            JLABEL1.setText("");
            JLABEL2.setText("");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        if(month=="September" && year==2017){
            JLABEL1.setText("១៩-២០-២១ ពិធី​បុណ្យ​ភ្ជុំ​បិណ្ឌ");
            JLABEL2.setText("២៤ ទិវា​ប្រកាស​រដ្ឋ​ធម្មនុញ្ញ");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        if(month=="October" && year==2017){
            JLABEL1.setText("១៥ គោរព​ព្រះវិញ្ញាណក្ខន្ធ «ព្រះបរមរតនកោដ្ឋ»");
            JLABEL2.setText("<html>២៣ កិច្ចព្រមព្រៀង​សន្តិភាព​ទីក្រុង​ប៉ារីស</html>");
            JLABEL3.setText("២៩ ទិវាគ្រងរាជ្យរបស់ព្រះ​មហាក្សត្រ​");
            JLABEL4.setText("");
        }
        if(month=="November" && year==2017){
            JLABEL1.setText("<html>២, ៣, ៤ ព្រះរាជ​ពិធីបុណ្យ​អុំទូក បណ្ដែតប្រទីប ​សំពះព្រះខែ អកអំបុក</html>");
            JLABEL2.setText("០៩ ពិធី​បុណ្យ​ឯករាជ្យ​ជាតិ");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        if(month=="December" && year==2017){
            JLABEL1.setText("១០​ ទិវា​សិទ្ធិ​មនុស្ស​អន្តរជាតិ");
            JLABEL2.setText("");
            JLABEL3.setText("");
            JLABEL4.setText("");
        } 
        if(year!=2017){
            JLABEL1.setText("");
            JLABEL2.setText("");
            JLABEL3.setText("");
            JLABEL4.setText("");
        }
        
        int startDay = cal.get(Calendar.DAY_OF_WEEK);
        int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
 
        model.setRowCount(0);
        model.setRowCount(weeks);
 
        int i = startDay-1;
        for(int day=1; day<=numberOfDays; day++){
            model.setValueAt(day, i/7 , i%7 );    
            i = i + 1;
        }
 
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
    private javax.swing.JLabel JLABEL1;
    private javax.swing.JLabel JLABEL2;
    private javax.swing.JLabel JLABEL3;
    private javax.swing.JLabel JLABEL4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}

/* 
class YourTableCellRenderer
       extends DefaultTableCellRenderer {
  public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row,
                                                 int column) {
    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    c.setForeground(Color.RED);
    return c;
  }
}
*/
