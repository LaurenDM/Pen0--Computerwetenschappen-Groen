package gui;
import domain.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import lejos.pc.tools.Upload;

import controller.Controller;
import domain.robots.CannotMoveException;
import domain.util.ColorPolygon;

public class ContentPanel implements ActionListener {
	static JFrame frame = new JFrame("P&O - Groen");
    static JFrame variableFrame = new JFrame("P&O - Groen - Variables");
    static JFrame calibrationFrame = new JFrame("P&O - Groen - Lightsensor Calibration");
    private static ControllerPoller controllerPoller;
    private JPanel titlePanel,titlePanel2, buttonPanel, inputPanel, variablePanel, calibrationPanel, debugPanel;
    private JLabel buttonLabel, actionLabel, titleLabel;
    private JLabel xLabel, yLabel, speedLabel, angleLabel, lightLabel, distanceLabel, touchingLabel;
    private JButton upButton, rightButton,leftButton, downButton, cancelButton, variableButton, connectButton, calibrateButton;
    private JTextArea debugText;
    final JPanel totalGUI = new JPanel();
    final JPanel variableGUI = new JPanel();
    static int totalXDimensions = 700;
    static int totalYDimensions = 700;
    static int buttonXDimension = 90;
    static int buttonYDimension = 30;
    private boolean connected = false;
    DrawingPanel drawingPanel;
    private boolean upButtonPressed = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;
    private boolean downButtonPressed = false;
    public enum Button {
        UP, LEFT, DOWN, RIGHT,NONE
    }
	
    
    Controller controller;
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
    
	public ContentPanel() {
		//We create a controller that controls communication with the domain.
	    controller = new Controller();
		// We create a bottom JPanel to place everything on.
        totalGUI.setLayout(null);
        totalGUI.requestFocusInWindow();
        //___________________________________________________________
        // Creation of a Panel to contain the title labels
        titlePanel = new JPanel();
        fixPanelLayout(titlePanel, totalXDimensions, 30, 0, 0);
        //Set the titleLabel
        titleLabel = new JLabel("P&O - Team Groen");
        fixLabelLayout(titlePanel, titleLabel, totalXDimensions, 30, 0, 0);
        
        //Definition of the KeyListener
        KeyListener l = createListener();

        //___________________________________________________
        // Creation of a Panel to contain all the JButtons.
        buttonPanel = new JPanel();
        fixPanelLayout(buttonPanel, 300, 400, 400, 300);
        buttonPanel.addKeyListener(l);
        
        buttonLabel = new JLabel("Control the robot here");
        fixLabelLayout(buttonPanel, buttonLabel, 300, 20, 0, 0);
        
        actionLabel = new JLabel("The robot is doing nothing at this moment.");
        fixLabelLayout(buttonPanel, actionLabel, 300, 30, 0, buttonYDimension + 180);

        upButton = new JButton("UP");
        fixButtonLayout(buttonPanel, upButton, 120, 30, buttonXDimension, buttonYDimension);

        rightButton = new JButton("RIGHT");
        fixButtonLayout(buttonPanel, rightButton, 120, 30, 155, buttonYDimension + 30);

        leftButton = new JButton("LEFT");
        fixButtonLayout(buttonPanel, leftButton, 120, 30, 25, buttonYDimension + 30);
        
        downButton = new JButton("DOWN");
        fixButtonLayout(buttonPanel, downButton, 120, 30, buttonXDimension, buttonYDimension + 60);
        
        cancelButton = new JButton("CANCEL");
        fixButtonLayout(buttonPanel, cancelButton, 240, 30, 30, buttonYDimension + 110);
        
        variableButton = new JButton("EDIT POLYGON VARIABLES");
        fixButtonLayout(buttonPanel, variableButton, 240, 30, 30, buttonYDimension + 140);
        
        connectButton = new JButton("CONNECT TO ROBOT");
        fixButtonLayout(buttonPanel, connectButton, 240, 30, 30, buttonYDimension + 220);
        
        calibrateButton = new JButton("CALIBRATE LIGHTSENSOR");
        fixButtonLayout(buttonPanel, calibrateButton, 240, 30, 30, buttonYDimension + 250);
        buttonPanel.setFocusable(true);
        
        //_____________________________________________________
        // Creation of a Panel to contain the debug information
        createDebugPanel();
        
        
        //_________________________________________________________________________
        // Creation of a Drawing Panel to display the map and the robot's movements
        drawingPanel = new DrawingPanel(this);
        fixPanelLayout(drawingPanel, 350, 500, 25, 50);
        drawingPanel.setBackground(Color.WHITE);
        
        //________________________
        //Creating variable panel
        VariablePanel variablePanel = new VariablePanel(variableFrame,controller);
        variableFrame.setContentPane(variablePanel.getContentPanel());;
        variableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        variableFrame.setSize(400, 400);
        
        //Creating calibration panel
        CalibrationPanel calibrationPanel = new CalibrationPanel(calibrationFrame, controller);
        calibrationFrame.setContentPane(calibrationPanel.getContentPanel());
        calibrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        calibrationFrame.setSize(400,400);
        
        
    	
	    //We create a controllerPoller that monitors debug info.
	    controllerPoller = new ControllerPoller(controller, this);
	    //We start the controllerPoller
        controllerPoller.start();
        
        frame.requestFocusInWindow(); 


                
	}
	private void createDebugPanel() {
		debugPanel = new JPanel(null);
        debugText = new JTextArea(5,22);
        debugText.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(debugText);
        debugText.setEditable(false);
        debugPanel.add(scrollPane);
        fixPanelLayout(debugPanel, 300, 300, 400, 50);
        scrollPane.setLocation(0, 0);
        scrollPane.setSize(250,100);
        writeToDebug("Program started successfully");
        //Infolabels
        xLabel = new JLabel("X: 0");
        xLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, xLabel, 125, 20, 0, 100);
        yLabel = new JLabel("Y: 0");
        yLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, yLabel, 125, 20, 125, 100);
        speedLabel = new JLabel("Speed: 0");
        speedLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, speedLabel, 125, 20, 0, 120);
        angleLabel = new JLabel("Angle: 0 degrees");
        angleLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, angleLabel, 125, 20, 125, 120);
		lightLabel = new JLabel("Lightsensor: 0");
	    lightLabel.setHorizontalTextPosition(JLabel.LEFT);
	    fixLabelLayout(debugPanel, lightLabel, 125, 20, 0, 140);
        distanceLabel = new JLabel("Distance: 0");
        distanceLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, distanceLabel, 125, 20, 125, 140);
        touchingLabel = new JLabel("Touching: FALSE");
        touchingLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, touchingLabel, 125, 20, 0, 160);
	}
	
	

	public void updateBoard(List<ColorPolygon> collection){
		for (ColorPolygon colorPoly:collection) {
			drawingPanel.reDrawMyPolygon(colorPoly);
		}
	} 
	
	/**
	 * A thread class that, while it's thread is running, polls the controller every 10 ms for changes to the 
	 * robot's variables.
	 */
	private static class ControllerPoller extends Thread {
    	Controller controller;
    	ContentPanel contentPanel;
    	
    	/**
    	 * Make a new ControllerPoller.
    	 * @param controller The controller this ControllerPoller should poll
    	 * @param contentPanel The parent object for this ControllerPoller
    	 */
		public ControllerPoller(Controller controller, ContentPanel contentPanel){
    		this.controller = controller;
    		this.contentPanel = contentPanel;
    	}
		
		/**
		 * Infinite loop that runs while the thread is active.
		 */
		public void run(){
			try{
				while(true){
					contentPanel.updateBoard(controller.getColorPolygons());
					contentPanel.setRobotX(controller.getXCo());
					contentPanel.setRobotY(controller.getYCo());
					contentPanel.setRobotSpeed(controller.getSpeed());
					contentPanel.setRobotAngle(controller.getAngle());
					contentPanel.setRobotLightValue(controller.readLightValue());
					contentPanel.setRobotDistanceValue(controller.readUltrasonicValue());
					contentPanel.setRobotTouchingValue(controller.isTouching());
					//TODO
					sleep(50);
				}
			} catch(InterruptedException e){
				//Do absolutely nothing
			}
		}
    }
	
	public void actionPerformed(ActionEvent e) {
        if(e.getSource() == upButton){
        	actionLabel.setText("The robot is going forward!");
        	upButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        	try {
				controller.moveForward();
			} catch (CannotMoveException e1) {
				actionLabel.setText("The robot has encountered an obstacle");
			}
        }
            
        else if(e.getSource() == downButton){
        	actionLabel.setText("The robot is going back!");
        	downButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        	controller.moveBack();
        }
        	
        else if(e.getSource() == leftButton){
        	actionLabel.setText("The robot is turning left!");
        	leftButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        	controller.rotateLeftNonBlocking();
        }
        	
        else if(e.getSource() == rightButton){
        	actionLabel.setText("The robot is turning right!");
        	rightButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        	controller.rotateRightNonBlocking();
        }
        	
        else if(e.getSource() == cancelButton){
        	actionLabel.setText("The robot cancelled all actions.");
        	cancelButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        	controller.cancel();
        }
        else if(e.getSource() == variableButton){
        	variableFrame.setVisible(true);
        	actionLabel.setText("The robot is setting parameters.");
        	variableButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        	
        }
        else if(e.getSource() == connectButton){
        	if(getConnected() == true ){
        		connectButton.setText("Connect to robot");
        		setConnected(false);
        		controller.connectNewSimRobot();
        	}
        	else{
        		connectButton.setText("Disconnect from robot");
        		setConnected(true);
        		controller.connectNewBtRobot();
        	}
        }
        else if(e.getSource() == calibrateButton){
        	calibrationFrame.setVisible(true);
        	actionLabel.setText("The lightsensor is being calibrated.");
        	calibrateButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        }
        
    }
	private KeyListener createListener() {
		KeyListener l;
		l = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					actionLabel.setText("The robot is turning right!");
					controller.rotateRightNonBlocking();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP){
					actionLabel.setText("The robot is going forward!");
					try {
						controller.moveForward();
					} catch (CannotMoveException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					actionLabel.setText("The robot is turning left!");
					controller.rotateLeftNonBlocking();
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					actionLabel.setText("The robot is going back!");
					controller.moveBack();
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("released");
				if (e.getKeyCode() == KeyEvent.VK_RIGHT && getCurrentPressedButton() == Button.RIGHT){
					setCurrentPressedButton(false,Button.RIGHT);
					actionLabel.setText("The robot is doing nothing atm!");
					rightButton.setSelected(false);
					controller.cancel();
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT && getCurrentPressedButton() == Button.LEFT){
					setCurrentPressedButton(false,Button.LEFT);
					actionLabel.setText("The robot is doing nothing atm!");
					leftButton.setSelected(false);
					controller.cancel();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP && getCurrentPressedButton() == Button.UP){
					setCurrentPressedButton(false,Button.UP);
					actionLabel.setText("The robot is doing nothing atm!");
					upButton.setSelected(false);
					controller.cancel();
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN && getCurrentPressedButton() == Button.DOWN){
					setCurrentPressedButton(false,Button.DOWN);
					actionLabel.setText("The robot is doing nothing atm!");
					downButton.setSelected(false);
					controller.cancel();
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (getCurrentPressedButton() == Button.NONE){
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					setCurrentPressedButton(true,Button.RIGHT);
					actionLabel.setText("The robot is turning right!");
					rightButton.setSelected(true);
					controller.rotateRightNonBlocking();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP){
					setCurrentPressedButton(true,Button.UP);
					actionLabel.setText("The robot is going forward!");
					upButton.setSelected(true);
					try {
						controller.moveForward();
					} catch (CannotMoveException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					setCurrentPressedButton(true,Button.LEFT);
					actionLabel.setText("The robot is turning left!");
					leftButton.setSelected(true);
					controller.rotateLeftNonBlocking();
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					setCurrentPressedButton(true,Button.DOWN);
					actionLabel.setText("The robot is going back!");
					downButton.setSelected(true);
					controller.moveBack();
				}
				}
			}
			
		};
		return l;
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
	
//	public void drawLine(int x, int y, int z, int u, Color color){
//        Graphics g = drawingPanel.getGraphics(); 
//        g.setColor(color);
//        g.drawLine(x, y, z, u);
//        drawingPanel.repaint();
//	} //TODO
	
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
    
    public void setRobotLightValue(double value){
    	lightLabel.setText("Lightsensor: " + Double.valueOf(value).intValue());
    }
    
    public void setRobotDistanceValue(double value){
    	distanceLabel.setText("Distance: " + Double.valueOf(value).intValue());
    }
    
    public void setRobotTouchingValue(boolean value){
    	touchingLabel.setText("Touching: " + Boolean.valueOf(value).toString());
    }

	public Controller getController() {
		return controller;
	}
	
}
