package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Set;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.jfree.ui.Align;

import controller.Controller;
import domain.Position.Position;
import domain.barcodes.Barcode;
import domain.robots.CannotMoveException;
import domain.util.ColorPolygon;
import exceptions.ConnectErrorException;
/**
 * 
 * @author PenO Groen
 *
 */
public class ContentPanel implements ActionListener {
	static JFrame frame = new JFrame("P&O - Groen");
    static JFrame variableFrame = new JFrame("P&O - Groen - Variables");
    static JFrame calibrationFrame = new JFrame("P&O - Groen - Lightsensor Calibration");
    static JFrame sensorOrientationFrame = new JFrame("P&O - Groen - Ultrasonicsensor Orientation");
    static JFrame barcodeFrame = new JFrame("P&O - Groen - Set the barcode values");
    private static ControllerPoller controllerPoller;
    private JPanel titlePanel, buttonPanel, debugPanel, bottomButtonPanel;
    private JLabel buttonLabel, actionLabel, titleLabel;
    private JLabel xLabel, yLabel, speedLabel, angleLabel, lightLabel, distanceLabel, touchingLabel, lineLabel;
    private JButton upButton, rightButton,leftButton, downButton, cancelButton, variableButton, connectButton, 
    calibrateButton, sensorOrientationButton, loadMazeButton, straightenButton, sensorButton,
    rotateSlowLeft,rotateSlowRight,startButton, barcodeButton, finishButton, resumeButton, resetButton,setBarcodeButton;
    private static JTextArea debugText;
    final JPanel totalGUI = new JPanel();
    final JPanel variableGUI = new JPanel();
    final static int totalXDimensions = 1100;
//    final static int totalYDimensions = 750; TODO verwijderen indien niet gebruikt na i4
    final static int moveButtonWidth = 100;
    final static int allButtonHeight = 20;
    final static int wideButtonWidth=150;
	final static int rightPanelWidth=365;
	final static int yPaddingTop=50;
	final static int debugPanelHeight=100+3*20; //100 for the scrollpane an 2*20 for 2 textlines
	final static int sensorGraphsPanelHeight=350;

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
    	object.setHorizontalAlignment(JLabel.LEFT);
        object.setForeground(Color.black);
    	//totalGUI.add(object);
    	source.add(object);
    }
    public void fixButtonLayout(JPanel source, JButton jButton, int xsize, int ysize, int xco, int yco, int fontSize){
    	Font curFont = jButton.getFont();
    	jButton.setFont(new Font(curFont.getFontName(), curFont.getStyle(), fontSize));
    	jButton.setHorizontalAlignment(0);
    	jButton.setLayout(null);
    	jButton.setLocation(xco, yco);
    	jButton.setSize(xsize, ysize);
    	jButton.addActionListener(this);
    	source.add(jButton);
    }

    public void fixButtonLayout(JPanel source, JButton jButton, int xsize, int ysize, int xco, int yco){
    	fixButtonLayout(source, jButton, xsize, ysize, xco,  yco, 9);
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
        fixPanelLayout(titlePanel, totalXDimensions, allButtonHeight, 0, 0);
        //Set the titleLabel
        titleLabel = new JLabel("P&O - Team Groen");
        fixLabelLayout(titlePanel, titleLabel, totalXDimensions, allButtonHeight, 0, 0);
        
        //Definition of the KeyListener
        KeyListener l = createListener();

        //___________________________________________________
        // Creation of a Panel to contain all the JButtons.
        buttonPanel = new JPanel();
		fixPanelLayout(buttonPanel, 300,8*allButtonHeight, 750, yPaddingTop+debugPanelHeight+sensorGraphsPanelHeight);
        buttonPanel.addKeyListener(l);
        
        
        actionLabel = new JLabel("The robot is doing nothing at this moment.");
        fixLabelLayout(buttonPanel, actionLabel, 300, allButtonHeight, 0, 7*allButtonHeight);
        
       

        upButton = new JButton("FORWARD");
        fixButtonLayout(buttonPanel, upButton, moveButtonWidth, allButtonHeight, moveButtonWidth, 0);

        rightButton = new JButton("RIGHT");
        fixButtonLayout(buttonPanel, rightButton, moveButtonWidth, allButtonHeight, 2*moveButtonWidth, 0);

        leftButton = new JButton("LEFT");
        fixButtonLayout(buttonPanel, leftButton, moveButtonWidth, allButtonHeight, 0, 0);
        
        downButton = new JButton("BACKWARD");
        fixButtonLayout(buttonPanel, downButton, moveButtonWidth , allButtonHeight, moveButtonWidth, allButtonHeight);
        
        rotateSlowLeft = new JButton("Slow L");
        fixButtonLayout(buttonPanel, rotateSlowLeft, moveButtonWidth, allButtonHeight, 0, allButtonHeight);
        
        rotateSlowRight = new JButton("Slow R");
        fixButtonLayout(buttonPanel,rotateSlowRight, moveButtonWidth, allButtonHeight, 2*moveButtonWidth, allButtonHeight);
        
        cancelButton = new JButton("STOP");
		fixButtonLayout(buttonPanel, cancelButton, wideButtonWidth, allButtonHeight, 0, 2*allButtonHeight );
        
        variableButton = new JButton("POLYGON");
        fixButtonLayout(buttonPanel, variableButton, wideButtonWidth, allButtonHeight, wideButtonWidth, 2*allButtonHeight);
        
        connectButton = new JButton("CONNECT TO ROBOT");
        fixButtonLayout(buttonPanel, connectButton, wideButtonWidth, allButtonHeight, 0, 3*allButtonHeight );
        
        calibrateButton = new JButton("CALIBRATE LIGHTSENSOR");
        fixButtonLayout(buttonPanel, calibrateButton, wideButtonWidth, allButtonHeight, wideButtonWidth, 3*allButtonHeight);
        
        sensorOrientationButton = new JButton("SENSOR ANGLE");
        fixButtonLayout(buttonPanel, sensorOrientationButton, wideButtonWidth, allButtonHeight, 0, 4*allButtonHeight);
        
        loadMazeButton = new JButton("LOAD MAZE");
        fixButtonLayout(buttonPanel, loadMazeButton, wideButtonWidth, allButtonHeight, wideButtonWidth, 4*allButtonHeight);
        
        straightenButton = new JButton("STRAIGHTEN");
        fixButtonLayout(buttonPanel, straightenButton, wideButtonWidth, allButtonHeight, 0, 5*allButtonHeight);
        
        barcodeButton = new JButton("FIND BARCODE");
        fixButtonLayout(buttonPanel, barcodeButton, wideButtonWidth, allButtonHeight, wideButtonWidth, 5*allButtonHeight);
        
        setBarcodeButton = new JButton("SET BARCODE VALUES");
        fixButtonLayout(buttonPanel, setBarcodeButton, wideButtonWidth, allButtonHeight, wideButtonWidth, 6*allButtonHeight);
        
        sensorButton = new JButton("DISABLE TURN ERROR");
        fixButtonLayout(buttonPanel, sensorButton, wideButtonWidth, allButtonHeight, 0, 6*allButtonHeight);
        
        startButton = new JButton("Start");
//        fixButtonLayout(buttonPanel, startButton, 20, 150, 0, 0);
        buttonPanel.setFocusable(true);
        
      //___________________________________________________
        // Creation of a Panel to contain all the JButtons.
        bottomButtonPanel = new JPanel();
        fixPanelLayout(bottomButtonPanel, 700, 50, 25, 660);
        bottomButtonPanel.addKeyListener(l);
        
        startButton = new JButton("Start exploring");
        fixButtonLayout(bottomButtonPanel, startButton, 150, allButtonHeight, 0, 0);
        
        resumeButton = new JButton("Resume exploring");
        fixButtonLayout(bottomButtonPanel, resumeButton, 150, allButtonHeight, 170, 0);
        
        finishButton = new JButton("Drive to finish");
        fixButtonLayout(bottomButtonPanel, finishButton, 150, allButtonHeight, 340, 0);
        
        resetButton = new JButton("Reset");
        fixButtonLayout(bottomButtonPanel, resetButton, 150, allButtonHeight, 510, 0);
        
        bottomButtonPanel.setFocusable(true);
        
        
        //_____________________________________________________
        // Creation of a Panel to contain the debug information
        createDebugPanel();
        
        
        //_________________________________________________________________________
        // Creation of a Drawing Panel to display the map and the robot's movements
        drawingPanel = new DrawingPanel(this);
        fixPanelLayout(drawingPanel, 700, 600, 25, yPaddingTop);
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
        
        //Creating barcodePanel
        BallBarcodePanel ballBarcodePanel = new BallBarcodePanel(barcodeFrame, controller);
        barcodeFrame.setContentPane(ballBarcodePanel.getContentPanel());
        barcodeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        barcodeFrame.setSize(400,210);
        
        
    	
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
        fixPanelLayout(debugPanel, rightPanelWidth, debugPanelHeight , 750, yPaddingTop);
        scrollPane.setLocation(0, 0);
        scrollPane.setSize(315,100);
        writeToDebug("Program started successfully");
        //Infolabels
        xLabel = new JLabel("X: 0");
        xLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, xLabel, 125, 20, 0, 100);
        
        yLabel = new JLabel("Y: 0");
        yLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, yLabel, 125, 20, 100, 100);
        
        touchingLabel = new JLabel("Touch: FALSE");
        touchingLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, touchingLabel, 125, 20, 200, 100);
        
        speedLabel = new JLabel("Speed: 0");
        speedLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, speedLabel, 125, 20, 0, 120);
        
        angleLabel = new JLabel("Angle: 0");
        angleLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, angleLabel, 125, 20, 100, 120);
		
        lightLabel = new JLabel("Lightsensor: 0");
	    lightLabel.setHorizontalTextPosition(JLabel.LEFT);
	    fixLabelLayout(debugPanel, lightLabel, 125, 20, 0, 140);
        
	    distanceLabel = new JLabel("Distance: 0");
        distanceLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, distanceLabel, 125, 20, 125, 140);
//        TODO Francis: vervangen door grafiek
        
        lineLabel = new JLabel("Line: FALSE");
        lineLabel.setHorizontalTextPosition(JLabel.LEFT);
        fixLabelLayout(debugPanel, lineLabel, 125, 20, 200, 120);

	}
	
	

	public void updateBoard(final List<ColorPolygon> collection){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				drawingPanel.drawFoundWalls();
				drawingPanel.drawFoundBarcodes();
				drawingPanel.drawLineToFinish();
				drawingPanel.drawBalls();
				for(int i=0; i<collection.size(); i++){
					if(i==0){drawingPanel.reDrawMyPolygon(collection.get(i), Color.BLACK);}
					if(i==1){drawingPanel.reDrawMyPolygon(collection.get(i), Color.BLUE);}
					if(i==2){drawingPanel.reDrawMyPolygon(collection.get(i), Color.RED);}
					if(i==3){drawingPanel.reDrawMyPolygon(collection.get(i), Color.GREEN);}
					if(i>3){drawingPanel.reDrawMyPolygon(collection.get(i), Color.ORANGE);}
				}
//				for (ColorPolygon colorPoly:collection) {
//					drawingPanel.reDrawMyPolygon(colorPoly);
//				}
			}
		});
		
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
			int count = 0;
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
					if(controller.ballInPossesion() && count < 1){
						count++;
						contentPanel.ballAlert();
					}
					contentPanel.drawRawData();
					contentPanel.updateInfoPanel();
					//TODO
					sleep(100);
				}
			} catch(InterruptedException e){
				//Do absolutely nothing
			}
		}
    }
	
	public void ballAlert(){
		debugText.append("Robot picked up ball" + "\n");
		drawingPanel.removeBall(controller.getBall());
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
        		controller.connectNewSimRobot(0, new Position(20,20), 0);
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
        else if(e.getSource() == setBarcodeButton){
        	barcodeFrame.setVisible(true);
        	actionLabel.setText("The barcode numbers are being added");
        	setBarcodeButton.setSelected(false);
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
//        	actionLabel.setText("Showing raw distance data");
//        	showRawData = true;
//        	buttonPanel.requestFocusInWindow();
        	actionLabel.setText("Disabled error margin on turning");
        	controller.disableError();
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
		else if(e.getSource() == resumeButton){
			controller.resumeExplore();
			buttonPanel.requestFocusInWindow();
		}
		else if(e.getSource() == finishButton){
			controller.driveToFinish();
			buttonPanel.requestFocusInWindow();
		}
		else if(e.getSource() == resetButton){
			controller.reset();
			drawingPanel.reset();
			debugText.setText("");
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
    	lightLabel.setText("Lightsensor: " + Double.valueOf(value).intValue()); //TODO Francis
    }
    
    public void setRobotDistanceValue(double value){
    	distanceLabel.setText("Distance: " + Double.valueOf(value).intValue()); //TODO Francis
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
				writeToDebug("Barcode "+b.getBits()[0]+b.getBits()[1]+b.getBits()[2]+b.getBits()[3]+b.getBits()[4]+b.getBits()[5]+" with value "+b.getPossibleDecimal()+" added.");
				if(b.getAction()!=null){
				writeToDebug("Action: "+b.getAction().toString());
				}

			}
		}
	}
	
	
}

 
