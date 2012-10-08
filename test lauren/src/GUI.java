import javax.swing.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeListener;
import java.text.AttributedCharacterIterator;

public class GUI{

    // Definition of global values and items that are part of the GUI.
    
    static JFrame frame = new JFrame("P&O - Groen");
    static JFrame variableFrame = new JFrame("P&O - Groen - Variables");
    JPanel titlePanel,titlePanel2, buttonPanel, inputPanel, drawingPanel, variablePanel;
    JLabel buttonLabel, actionLabel, titleLabel;
    JButton upButton, rightButton,leftButton, downButton, cancelButton, variableButton;
    final JPanel totalGUI = new JPanel();
    final JPanel variableGUI = new JPanel();
     
    static int totalXDimensions = 700;
    static int totalYDimensions = 700;
    static int buttonXDimension = 90;
    static int buttonYDimension = 30;
  
    
    
    public JPanel createContentPane (){
    	

        
        //_______________________________________
        //Creating of the variable fields.
        
        
        
        //_______________________________________
        //Creation of the "drawing" board.
        
        /*DrawingPanel drawingPanel = new DrawingPanel();
        fixPanelLayout(drawingPanel, 350, 500, 25, 50);
        drawingPanel.setBackground(Color.WHITE);
        Graphics g = drawingPanel.getGraphics(); 
        g.setColor(Color.black);
        g.drawLine(100, 100, 200, 200);
      
        //drawingPanel.getGraphics().setColor(Color.black);
        //drawingPanel.getGraphics().drawLine(100,100,200,200);
        drawingPanel.repaint();
        g.drawLine(200, 200, 100,300);
        drawingPanel.repaint();*/
        
        
        
        //_______________________________________
    
        totalGUI.setOpaque(true);
        return totalGUI;
    }
    
    public JPanel createVariablePane (){
    	variableGUI.setLayout(null);
    	titlePanel2 = new JPanel();
    	titlePanel2.setLayout(null);
    	titlePanel2.setLocation(0, 0);
    	titlePanel2.setSize(350, 30);
        variableGUI.add(titlePanel2);
        JTextField firstImputfield = new JTextField(16);
        titlePanel.add(titleLabel);
        variableGUI.add(firstImputfield);
        variableGUI.setOpaque(true);
        
    	return variableGUI;
    }
    


    public static void createAndShowGUI() {

        JFrame.setDefaultLookAndFeelDecorated(true);
        ContentPanel contentPanel = new ContentPanel();
        //VariablePanel variablePanel = new VariablePanel();
        frame.setContentPane(contentPanel.getContentPanel());
        
        
//        variableFrame.setContentPane(variablePanel.getContentPanel());
//        //variableFrame.setContentPane(demo.createVariablePane());
//        //variableFrame.setSize(400, 400);
//        variableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        variableFrame.setSize(400, 400);
//        variableFrame.setVisible(true);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(totalXDimensions, totalYDimensions);
        frame.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}