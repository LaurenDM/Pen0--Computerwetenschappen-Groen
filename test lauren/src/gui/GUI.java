package gui;
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
    static ContentPanel contentPanel;
    static int totalXDimensions = 700;
    static int totalYDimensions = 700;
  
    
    //Definition of creation of new GUI.
    //Defines both variable and content panel.
    public static void createAndShowGUI() {

        JFrame.setDefaultLookAndFeelDecorated(true);
        contentPanel = new ContentPanel();
        frame.setContentPane(contentPanel.getTotalGuiPanel());
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
    
    /**
     * Updates the x-coordinate info on the content panel.
     */
    public void setRobotX(double newX) {
    	contentPanel.setRobotX(newX);
    }
    
    /**
     * Updates the y-coordinate info on the content panel.
     */
    public void setRobotY(double newY) {
    	contentPanel.setRobotY(newY);
    }
    
    /**
     * Updates the speed info on the content panel.
     */
    public void setRobotSpeed(double speed){
    	contentPanel.setRobotSpeed(speed);
    }
    
    /**
     * Updates the angle info on the content panel.
     */
    public void setRobotAngle(double angle){
    	contentPanel.setRobotAngle(angle);
    }
    
    /**
     * Write a new line to the debug info panel
     */
    public void writeToDebug(String debugInfo){
    	contentPanel.writeToDebug(debugInfo);
    }
    
    

}