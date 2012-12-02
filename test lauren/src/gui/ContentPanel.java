package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import controller.Controller;
import domain.barcodes.Barcode;
import domain.robots.CannotMoveException;
import domain.util.ColorPolygon;
import exceptions.ConnectErrorException;

public class ContentPanel implements ActionListener {
	static JFrame frame = new JFrame("P&O - Groen");
    static JFrame variableFrame = new JFrame("P&O - Groen - Variables");
    static JFrame calibrationFrame = new JFrame("P&O - Groen - Lightsensor Calibration");
    static JFrame sensorOrientationFrame = new JFrame("P&O - Groen - Ultrasonicsensor Orientation");
    private static ControllerPoller controllerPoller;
    private JPanel titlePanel, buttonPanel, debugPanel;
    private JLabel buttonLabel, actionLabel, titleLabel;
    private JLabel xLabel, yLabel, speedLabel, angleLabel, lightLabel, distanceLabel, touchingLabel, lineLabel;
    private JButton upButton, rightButton,leftButton, downButton, cancelButton, variableButton, connectButton, 
    calibrateButton, sensorOrientationButton, loadMazeButton, straightenButton, sensorButton,
    rotateSlowLeft,rotateSlowRight,startButton, barcodeButton;
    private static JTextArea debugText;
    final JPanel totalGUI = new JPanel();
    final JPanel variableGUI = new JPanel();
    static int totalXDimensions = 1100;
    static int totalYDimensions = 750;
    static int buttonXDimension = 90;
    static int buttonYDimension = 30;
    private boolean connected = false;
    private DrawingPanel drawingPanel;
    private boolean upButtonPressed = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;
    private boolean downButtonPressed = false;
    private boolean showRawData = false;
    private int rotateSlowAmount = 10;
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
    	object.addActionListener(this);
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
    	if(button == Button.RIGHT){
    		this.rightButtonPressed = pressed;
    	}
    		
    	else if(button == Button.LEFT){
    		this.leftButtonPressed = pressed;
    	}
    		
    	else if(button == Button.UP){
    		this.upButtonPressed = pressed;
    	}
    		
    	else if(button == Button.DOWN){
    		this.downButtonPressed = pressed;
    	}
    		
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
        fixPanelLayout(buttonPanel, 300, 500, 800, 250);
        buttonPanel.addKeyListener(l);
        
        buttonLabel = new JLabel("Control the robot here");
        fixLabelLayout(buttonPanel, buttonLabel, 300, 20, 0, 0);
        
        actionLabel = new JLabel("The robot is doing nothing at this moment.");
        fixLabelLayout(buttonPanel, actionLabel, 300, 30, 0, buttonYDimension + 180);

        upButton = new JButton("FORWARD");
        fixButtonLayout(buttonPanel, upButton, 120, 30, buttonXDimension, buttonYDimension);

        rightButton = new JButton("RIGHT");
        fixButtonLayout(buttonPanel, rightButton, 120, 30, 155, buttonYDimension + 30);

        leftButton = new JButton("LEFT");
        fixButtonLayout(buttonPanel, leftButton, 120, 30, 25, buttonYDimension + 30);
        
        downButton = new JButton("BACKWARD");
        fixButtonLayout(buttonPanel, downButton, 120, 30, buttonXDimension, buttonYDimension + 60);
        
        rotateSlowLeft = new JButton("Slow L");
        fixButtonLayout(buttonPanel, rotateSlowLeft, 70, 30, 25, buttonYDimension + 60);
        
        rotateSlowRight = new JButton("Slow R");
        fixButtonLayout(buttonPanel,rotateSlowRight, 70, 30, buttonXDimension + 115, buttonYDimension + 60);
        
        cancelButton = new JButton("STOP");
        fixButtonLayout(buttonPanel, cancelButton, 240, 30, 30, buttonYDimension + 110);
        
        variableButton = new JButton("EDIT POLYGON VARIABLES");
        fixButtonLayout(buttonPanel, variableButton, 240, 30, 30, buttonYDimension + 140);
        
        connectButton = new JButton("CONNECT TO ROBOT");
        fixButtonLayout(buttonPanel, connectButton, 240, 30, 30, buttonYDimension + 220);
        
        calibrateButton = new JButton("CALIBRATE LIGHTSENSOR");
        fixButtonLayout(buttonPanel, calibrateButton, 240, 30, 30, buttonYDimension + 250);
        
        sensorOrientationButton = new JButton("SET SENSOR ORIENTATION");
        fixButtonLayout(buttonPanel, sensorOrientationButton, 240, 30, 30, buttonYDimension + 280);
        
        loadMazeButton = new JButton("LOAD MAZE FROM FILE");
        fixButtonLayout(buttonPanel, loadMazeButton, 240, 30, 30, buttonYDimension + 310);
        
        straightenButton = new JButton("STRAIGHTEN AT WHITE LINE");
        fixButtonLayout(buttonPanel, straightenButton, 240, 30, 30, buttonYDimension + 340);
        
        barcodeButton = new JButton("FIND BARCODE");
        fixButtonLayout(buttonPanel, barcodeButton, 240, 30, 30, buttonYDimension + 370);
        
        sensorButton = new JButton("SHOW SENSOR DATA ON MAP");
        fixButtonLayout(buttonPanel, sensorButton, 240, 30, 30, buttonYDimension + 400);
        
        startButton = new JButton("Start");
        fixButtonLayout(buttonPanel, startButton, 20, 150, 0, 0);
        buttonPanel.setFocusable(true);
        //_____________________________________________________
        // Creation of a Panel to contain the debug information
        createDebugPanel();
        
        
        //_________________________________________________________________________
        // Creation of a Drawing Panel to display the map and the robot's movements
        drawingPanel = new DrawingPanel(this);
        fixPanelLayout(drawingPanel, 700, 600, 25, 50);
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
        
        //Creating sensorOrientation panel
        SensorOrientationPanel sensorOrientationPanel = new SensorOrientationPanel(sensorOrientationFrame, controller);
        sensorOrientationFrame.setContentPane(sensorOrientationPanel.getContentPanel());
        sensorOrientationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sensorOrientationFrame.setSize(400,400);
        
        
    	
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
        fixPanelLayout(debugPanel, 300, 300, 815, 50);
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
        angleLabel = new JLabel("Angle: 0");
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
        lineLabel = new JLabel("Line: FALSE");
        lineLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, lineLabel, 125, 20, 0, 180);

	}
	
	

	public void updateBoard(List<ColorPolygon> collection){
		drawingPanel.drawFoundWalls();
		drawingPanel.drawFoundBarcodes();
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
    		setPriority(Thread.MAX_PRIORITY);
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
					contentPanel.setLineValue(controller.detectWhiteLine());
					contentPanel.drawRawData();
					contentPanel.updateInfoPanel();
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
        		drawingPanel.clear();
        		drawingPanel.drawWhiteLines();
        		drawingPanel.updateUI();
        		setConnected(false);
        		controller.connectNewSimRobot();
        	}
        	else{
        		try{
        		controller.connectNewBtRobot();
        		connectButton.setText("Disconnect from robot");
        		setConnected(true);
        		drawingPanel.clear();
        		drawingPanel.drawWhiteLines();
        		drawingPanel.updateUI();}
        		catch(ConnectErrorException e1){
        		}
        	}
        	buttonPanel.requestFocusInWindow();
        }
        else if(e.getSource() == calibrateButton){
        	calibrationFrame.setVisible(true);
        	actionLabel.setText("The lightsensor is being calibrated.");
        	calibrateButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        }
        else if(e.getSource() == sensorOrientationButton){
        	sensorOrientationFrame.setVisible(true);
        	actionLabel.setText("The ultrasonicsensor is being oriented.");
        	sensorOrientationButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        }
        else if(e.getSource() == loadMazeButton){
        	actionLabel.setText("Maze loaded from file.");
        	FileDialog fileDialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
            fileDialog.setVisible(true);
            String fileName = fileDialog.getFile();
            String fileDirectory = fileDialog.getDirectory();
        	controller.readMazeFromFile(fileDirectory+fileName);
        	drawingPanel.drawSimulatedWalls();
        	buttonPanel.requestFocusInWindow();
        }
        else if(e.getSource() == straightenButton){
        	actionLabel.setText("Finding white line and straightening");
        	controller.findLineAndStraighten();
        	buttonPanel.requestFocusInWindow();
        }
        else if(e.getSource() == barcodeButton){
        	actionLabel.setText("Finding barcode and reading it");
        	controller.findBlackLineAndCreateBarcode();
        	buttonPanel.requestFocusInWindow();
        }
        else if(e.getSource() == sensorButton){
        	actionLabel.setText("Showing raw distance data");
        	showRawData = true;
        	buttonPanel.requestFocusInWindow();
        }
        else if(e.getSource() == rotateSlowRight){
        	controller.rotateAmount(rotateSlowAmount);
        	buttonPanel.requestFocusInWindow();
        }
		else if(e.getSource() == rotateSlowLeft){
			controller.rotateAmount(-rotateSlowAmount);
			buttonPanel.requestFocusInWindow();
        }
		else if(e.getSource() == startButton){
			controller.startExplore();
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
						actionLabel.setText("The robot has encountered an obstacle");
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
				//System.out.println("released");
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
						actionLabel.setText("The robot has encountered an obstacle");
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
				else if(e.getKeyCode() == KeyEvent.VK_X){
					controller.turnSensorLeft();
				}
				else if(e.getKeyCode() == KeyEvent.VK_C){
					controller.turnSensorRight();
				}
				else if(e.getKeyCode() == KeyEvent.VK_D){
					controller.turnSensorForward();
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
	
	/**
	 * Write a line to the debugging text area.
	 * @param text This line will appear at the bottom of the text area.
	 */
	public static void writeToDebug(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				debugText.append(text + "\n");
			}
		});
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
    
    public void setLineValue(boolean value){
    	lineLabel.setText("Line: " + Boolean.valueOf(value).toString());
    }

	public Controller getController() {
		return controller;
	}
	
	public void drawRawData(){
		if(showRawData)
		drawingPanel.drawRawSensorData();
	}
	
	//barcodes
	public void updateInfoPanel(){
		for(Barcode b :controller.getRobot().getBoard().getFoundBarcodes()){
			if(!b.getPrinted()){
				b.setPrinted(true);
				writeToDebug("Barcode with value "+Integer.toString(b.getDecimal())+" added.");
				if(b.getAction()!=null){
				writeToDebug("Action: "+b.getAction().toString());
				}

			}
		}
	}
	
}
 