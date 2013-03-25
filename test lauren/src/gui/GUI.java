package gui;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GUI{

    // Definition of global values and items that are part of the GUI.
    
    static JFrame frame = new JFrame("P&O - Groen");
    static JFrame variableFrame = new JFrame("P&O - Groen - Variables");
    static ContentPanel contentPanel;
    static int totalXDimensions = 1100;
    static int totalYDimensions = 750;
  
    
    //Definition of creation of new GUI.
    //Defines both variable and content panel.
    public static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        contentPanel = new ContentPanel();
        frame.setContentPane(contentPanel.getTotalGuiPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(totalXDimensions, totalYDimensions);
        frame.setVisible(true);
        contentPanel.setFocusButtons();

    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
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
    	ContentPanel.writeToDebug(debugInfo);
    }
    
    

}