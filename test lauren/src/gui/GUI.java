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
    static String gameID;
    static String playerID;
    
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

    
    
    public static void main(final String[] args) {

    	if(args.length>0){
    	try {
    		gameID=args[1];
    		playerID=args[2];
			Thread.sleep(Integer.parseInt(args[0]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	}

    	SwingUtilities.invokeLater(new Runnable() {
            @Override
			public void run() {
                createAndShowGUI();
            	if(args.length>0){
                contentPanel.automaticConnection();
            	}
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
    
    public static String getGameID(){
    	return gameID;
    }
    
    public static String getPlayerID(){
    	return playerID;
    }
    
    

}