/**
 * 
 */
package gui;

import gui.ContentPanel.Button;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.Controller;
import controller.TestController;
import domain.robots.CannotMoveException;

/**
 * @author Joren
 *
 */
public class TestGUI {

	private static TestContentPanel contentPanel;
	private static JFrame frame;
	private static int totalXDimensions = 700;
    private static int totalYDimensions = 700;
	private static TestController testController;

	public static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
        contentPanel = new TestContentPanel(testController);
        frame.setContentPane(contentPanel.getTotalGuiPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(totalXDimensions, totalYDimensions);
        frame.setVisible(true);
        contentPanel.setFocusButtons();
	}
	
	public static void setTestController(TestController testController){
		TestGUI.testController=testController;
	}
	
	private static class TestContentPanel implements ActionListener{
		static final JFrame frame = new JFrame("P&O - Groen - Test");
	    private JPanel titlePanel, buttonPanel;
	    private JLabel buttonLabel, actionLabel, titleLabel;
	    private JLabel xLabel, yLabel, speedLabel, angleLabel;
	    private JButton upButton, rightButton,leftButton, downButton, cancelButton, connectButton;
	    private JTextArea debugText;
	    static final JPanel totalGUI = new JPanel();
	    static final int totalXDimensions = 700;
	    static final int totalYDimensions = 700;
	    static final int buttonXDimension = 90;
	    static final int buttonYDimension = 30;
	    private boolean connected = false;
	    private boolean upButtonPressed = false;
	    private boolean leftButtonPressed = false;
	    private boolean rightButtonPressed = false;
	    private boolean downButtonPressed = false;
		
	    TestController testController;
	    
	    public void setFocusButtons(){
	    	buttonPanel.requestFocusInWindow();
	    }
	    
	    public void fixPanelLayout(JPanel object, int xsize, int ysize, int xco, int yco){
	    	object.setLayout(null);
	    	object.setLocation(xco, yco);
	    	object.setSize(xsize, ysize);
	    	totalGUI.add(object);
	    }
	   
	    public void fixLabelLayout(JPanel source, JLabel object, int xsize, int ysize, int xco, int yco){
	    	object.setLayout(null);
	    	object.setLocation(xco, yco);
	    	object.setSize(xsize, ysize);
	    	object.setHorizontalAlignment(0);
	        object.setForeground(Color.black);
	    	//totalGUI.add(object);
	    	source.add(object);
	    }
	    
	    public void fixButtonLayout(JPanel source, JButton object, int xsize, int ysize, int xco, int yco){
	    	object.setHorizontalAlignment(0);
	    	object.setLayout(null);
	    	object.setLocation(xco, yco);
	    	object.setSize(xsize, ysize);
	    	//totalGUI.add(object);
	    	source.add(object);
	    }
	    
	    public Button getCurrentPressedButton(){
	    	if(upButtonPressed == true)
	    		return Button.UP;
	    	else if(leftButtonPressed == true)
	    		return Button.LEFT;
	    	else if(rightButtonPressed == true)
	    		return Button.RIGHT;
	    	else if(downButtonPressed == true)
	    		return Button.DOWN;
	    	else
	    		return Button.NONE;
	    }
	    
	    public void setCurrentPressedButton(boolean pressed,Button button){
	    	if(button == Button.RIGHT)
	    		this.rightButtonPressed = pressed;
	    	else if(button == Button.LEFT)
	    		this.leftButtonPressed = pressed;
	    	else if(button == Button.UP)
	    		this.upButtonPressed = pressed;
	    	else if(button == Button.DOWN)
	    		this.downButtonPressed = pressed;
	    }
	    
		public TestContentPanel(final TestController testController) {
			this.testController = testController;
			// We create a bottom JPanel to place everything on.
	        totalGUI.setLayout(null);
	        totalGUI.requestFocusInWindow();	        
	        
	        //___________________________________________________________
	        // Creation of a Panel to contain the title labels
	        titlePanel = new JPanel();
	        titlePanel.setLayout(null);
	        titlePanel.setLocation(0, 0);
	        titlePanel.setSize(totalXDimensions, 30);
	        totalGUI.add(titlePanel);
	        
	        //Set the titleLabel
	        titleLabel = new JLabel("P&O - Team Groen - Test");
	        titleLabel.setLocation(0, 0);
	        titleLabel.setSize(totalXDimensions, 30);
	        titleLabel.setHorizontalAlignment(0);
	        titleLabel.setForeground(Color.black);
	        titlePanel.add(titleLabel);
	        
	        //Definition of the KeyListener
	        KeyListener l;
			l = new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_RIGHT){
						actionLabel.setText("The robot is turning right!");
						testController.getRobotController().rotateRightNonBlocking();
					}
					else if (e.getKeyCode() == KeyEvent.VK_UP){
						actionLabel.setText("The robot is going forward!");
						try {
							testController.getRobotController().moveForward();
						} catch (CannotMoveException e1) {
							actionLabel.setText("The robot has encountered an obstacle!");
						}
					}
					else if (e.getKeyCode() == KeyEvent.VK_LEFT){
						actionLabel.setText("The robot is turning left!");
						testController.getRobotController().rotateLeftNonBlocking();
					}
					else if (e.getKeyCode() == KeyEvent.VK_DOWN){
						actionLabel.setText("The robot is going back!");
						testController.getRobotController().moveBack();
					}
					
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					System.out.println("released");
					if (e.getKeyCode() == KeyEvent.VK_RIGHT && getCurrentPressedButton() == Button.RIGHT){
						setCurrentPressedButton(false,Button.RIGHT);
						actionLabel.setText("The robot is doing nothing atm!");
						rightButton.setSelected(false);
						testController.getRobotController().cancel();
					}
					else if (e.getKeyCode() == KeyEvent.VK_LEFT && getCurrentPressedButton() == Button.LEFT){
						setCurrentPressedButton(false,Button.LEFT);
						actionLabel.setText("The robot is doing nothing atm!");
						leftButton.setSelected(false);
						testController.getRobotController().cancel();
					}
					else if (e.getKeyCode() == KeyEvent.VK_UP && getCurrentPressedButton() == Button.UP){
						setCurrentPressedButton(false,Button.UP);
						actionLabel.setText("The robot is doing nothing atm!");
						upButton.setSelected(false);
						testController.getRobotController().cancel();
					}
					else if (e.getKeyCode() == KeyEvent.VK_DOWN && getCurrentPressedButton() == Button.DOWN){
						setCurrentPressedButton(false,Button.DOWN);
						actionLabel.setText("The robot is doing nothing atm!");
						downButton.setSelected(false);
						testController.getRobotController().cancel();
					}
					
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					if (getCurrentPressedButton() == Button.NONE){
					if (e.getKeyCode() == KeyEvent.VK_RIGHT){
						setCurrentPressedButton(true,Button.RIGHT);
						actionLabel.setText("The robot is turning right!");
						rightButton.setSelected(true);
						testController.getRobotController().rotateRightNonBlocking();
					}
					else if (e.getKeyCode() == KeyEvent.VK_UP){
						setCurrentPressedButton(true,Button.UP);
						actionLabel.setText("The robot is going forward!");
						upButton.setSelected(true);
						try {
							testController.getRobotController().moveForward();
						} catch (CannotMoveException e1) {
							actionLabel.setText("The robot has encountered an obstacle");
						}
					}
					else if (e.getKeyCode() == KeyEvent.VK_LEFT){
						setCurrentPressedButton(true,Button.LEFT);
						actionLabel.setText("The robot is turning left!");
						leftButton.setSelected(true);
						testController.getRobotController().rotateLeftNonBlocking();
					}
					else if (e.getKeyCode() == KeyEvent.VK_DOWN){
						setCurrentPressedButton(true,Button.DOWN);
						actionLabel.setText("The robot is going back!");
						downButton.setSelected(true);
						testController.getRobotController().moveBack();
					}
					}
				}
				
			};

	        //___________________________________________________
	        // Creation of a Panel to contain all the JButtons.
	        buttonPanel = new JPanel();
	        fixPanelLayout(buttonPanel, 300, 400, 400, 300);
	        buttonPanel.addKeyListener(l);
	        
	        buttonLabel = new JLabel("Control the robot here");
	        fixLabelLayout(buttonPanel, buttonLabel, 300, 20, 0, 0);
	        
	        actionLabel = new JLabel("The robot is doing nothing at this moment.");
	        actionLabel.setLocation(0, buttonYDimension + 180);
	        actionLabel.setSize(300, 30);
	        actionLabel.setHorizontalAlignment(0);
	        actionLabel.setForeground(Color.black);
	        buttonPanel.add(actionLabel);

	        upButton = new JButton("UP");
	        upButton.setLocation(buttonXDimension, buttonYDimension);
	        upButton.setSize(120, 30);
	        upButton.addActionListener(this);
	        buttonPanel.add(upButton);

	        rightButton = new JButton("RIGHT");
	        rightButton.setLocation(155, buttonYDimension + 30);
	        rightButton.setSize(120, 30);
	        rightButton.addActionListener(this);
	        buttonPanel.add(rightButton);

	        leftButton = new JButton("LEFT");
	        leftButton.setLocation(25, buttonYDimension + 30);
	        leftButton.setSize(120, 30);
	        leftButton.addActionListener(this);
	        buttonPanel.add(leftButton);
	        
	        downButton = new JButton("DOWN");
	        downButton.setLocation(buttonXDimension, buttonYDimension + 60);
	        downButton.setSize(120, 30);
	        downButton.addActionListener(this);
	        buttonPanel.add(downButton);
	        
	        cancelButton = new JButton("CANCEL");
	        cancelButton.setLocation(30, buttonYDimension + 110);
	        cancelButton.setSize(240, 30);
	        cancelButton.addActionListener(this);
	        buttonPanel.add(cancelButton);
	        
	        connectButton = new JButton("CONNECT TO ROBOT");
	        connectButton.setLocation(30, buttonYDimension + 220);
	        connectButton.setSize(240, 30);
	        connectButton.addActionListener(this);
	        buttonPanel.add(connectButton);
	        
	        buttonPanel.setFocusable(true);
	        
	        frame.requestFocusInWindow(); 


	                
		}
		
		public void actionPerformed(ActionEvent e) {
	        if(e.getSource() == upButton){
	        	actionLabel.setText("The robot is going forward!");
	        	upButton.setSelected(false);
	        	buttonPanel.requestFocusInWindow();
	        	try {
					testController.getRobotController().moveForward();
				} catch (CannotMoveException e1) {
					actionLabel.setText("The robot has encountered an obstacle");
				}
	        }
	            
	        else if(e.getSource() == downButton){
	        	actionLabel.setText("The robot is going back!");
	        	downButton.setSelected(false);
	        	buttonPanel.requestFocusInWindow();
	        	testController.getRobotController().moveBack();
	        }
	        	
	        else if(e.getSource() == leftButton){
	        	actionLabel.setText("The robot is turning left!");
	        	leftButton.setSelected(false);
	        	buttonPanel.requestFocusInWindow();
	        	testController.getRobotController().rotateLeftNonBlocking();
	        }
	        	
	        else if(e.getSource() == rightButton){
	        	actionLabel.setText("The robot is turning right!");
	        	rightButton.setSelected(false);
	        	buttonPanel.requestFocusInWindow();
	        	testController.getRobotController().rotateRightNonBlocking();
	        }
	        	
	        else if(e.getSource() == cancelButton){
	        	actionLabel.setText("The robot cancelled all actions.");
	        	cancelButton.setSelected(false);
	        	buttonPanel.requestFocusInWindow();
	        	testController.getRobotController().cancel();
	        }
	        
	        else if(e.getSource() == connectButton){
	        	if(getConnected() == true ){
	        		connectButton.setText("Connect to robot");
	        		setConnected(false);
	        		testController.getRobotController().connectNewSimRobot();
	        	}
	        	else{
	        		connectButton.setText("Disconnect from robot");
	        		setConnected(true);
	        		testController.getRobotController().connectNewBtRobot();
	        	}
	        		
	        	
	        }
	        
	    }
		
		public boolean getConnected(){
			return this.connected;
		}
		
		public void setConnected(boolean nowConnected){
			this.connected = nowConnected;
		}
		
		public JPanel getTotalGuiPanel(){
			return totalGUI;
		}
		
//		public void drawLine(int x, int y, int z, int u, Color color){
//	        Graphics g = drawingPanel.getGraphics(); 
//	        g.setColor(color);
//	        g.drawLine(x, y, z, u);
//	        drawingPanel.repaint();
//		} //TODO
		
		/**
		 * Write a line to the debugging text area.
		 * @param text This line will appear at the bottom of the text area.
		 */
		public void writeToDebug(String text) {
			debugText.append(text + "\n");
		}

		/**
	     * Updates the x-coordinate info on the debug panel.
	     */
	    public void setRobotX(double newX) {
	    	xLabel.setText("X: "+Double.valueOf(newX).intValue());
	    }
	    
	    /**
	     * Updates the y-coordinate info on the debug panel.
	     */
	    public void setRobotY(double newY) {
	    	yLabel.setText("Y: "+Double.valueOf(newY).intValue());
	    }
	    
	    /**
	     * Updates the speed info on the debug panel.
	     */
	    public void setRobotSpeed(double speed){
	    	speedLabel.setText("Speed: "+Double.valueOf(speed).intValue());
	    }
	    
	    /**
	     * Updates the angle info on the debug panel.
	     */
	    public void setRobotAngle(double angle){
	    	angleLabel.setText("Angle: "+Double.valueOf(angle).intValue() % 360);
	    }
		
	}
	

}
