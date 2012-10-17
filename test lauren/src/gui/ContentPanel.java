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
import domain.pixels.Pixel;

public class ContentPanel implements ActionListener {
	static JFrame frame = new JFrame("P&O - Groen");
    static JFrame variableFrame = new JFrame("P&O - Groen - Variables");
    private static ControllerPoller controllerPoller;
    private JPanel titlePanel,titlePanel2, buttonPanel, inputPanel, variablePanel, debugPanel;
    private JLabel buttonLabel, actionLabel, titleLabel;
    private JLabel xLabel, yLabel, speedLabel, angleLabel;
    private JButton upButton, rightButton,leftButton, downButton, cancelButton, variableButton, connectButton;
    private JTextArea debugText;
    final JPanel totalGUI = new JPanel();
    final JPanel variableGUI = new JPanel();
    static int totalXDimensions = 700;
    static int totalYDimensions = 700;
    static int buttonXDimension = 90;
    static int buttonYDimension = 30;
    DrawingPanel drawingPanel;
	
    
    Controller controller;
    
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
    
	public ContentPanel() {
		//We create a controller that controls communication with the domain.
	    controller = new Controller();
		// We create a bottom JPanel to place everything on.
        totalGUI.setLayout(null);
        // We create a controllerPoller to update the infolabel data
        
        
        //___________________________________________________________
        // Creation of a Panel to contain the title labels
        titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setLocation(0, 0);
        titlePanel.setSize(totalXDimensions, 30);
        totalGUI.add(titlePanel);
        
        //Set the titleLabel
        titleLabel = new JLabel("P&O - Team Groen");
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
					controller.rotateRight();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP){
					actionLabel.setText("The robot is going forward!");
					controller.moveForward();
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					actionLabel.setText("The robot is turning left!");
					controller.rotateLeft();
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					actionLabel.setText("The robot is going back!");
					controller.moveBack();
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// never gets called.
					e1.printStackTrace();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					actionLabel.setText("The robot is doing nothing atm!");
					rightButton.setSelected(false);
					controller.cancel();
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					actionLabel.setText("The robot is doing nothing atm!");
					leftButton.setSelected(false);
					controller.cancel();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP){
					actionLabel.setText("The robot is doing nothing atm!");
					upButton.setSelected(false);
					controller.cancel();
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					actionLabel.setText("The robot is doing nothing atm!");
					downButton.setSelected(false);
					controller.cancel();
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					actionLabel.setText("The robot is turning right!");
					rightButton.setSelected(true);
					controller.rotateRight();
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP){
					actionLabel.setText("The robot is going forward!");
					upButton.setSelected(true);
					controller.moveForward();
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					actionLabel.setText("The robot is turning left!");
					leftButton.setSelected(true);
					controller.rotateLeft();
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					actionLabel.setText("The robot is going back!");
					downButton.setSelected(true);
					controller.moveBack();
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
        
        variableButton = new JButton("EDIT POLYGON VARIABLES");
        variableButton.setLocation(30, buttonYDimension + 140);
        variableButton.setSize(240, 30);
        variableButton.addActionListener(this);
        buttonPanel.add(variableButton);
        
        connectButton = new JButton("CONNECT TO ROBOT");
        connectButton.setLocation(30, buttonYDimension + 220);
        connectButton.setSize(240, 30);
        connectButton.addActionListener(this);
        buttonPanel.add(connectButton);
        
        buttonPanel.setFocusable(true);
        
        //_____________________________________________________
        // Creation of a Panel to contain the debug information
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
        
        
    	
	    //We create a controllerPoller that monitors debug info.
	    controllerPoller = new ControllerPoller(controller, this);
	    //We start the controllerPoller
        controllerPoller.start();
                
	}
	
	
	

	public void updateBoard(List<Pixel> collection){
		drawingPanel.clear();
		for (Pixel pixel:collection) {
			drawingPanel.drawMyLine(pixel.getX(),pixel.getY(),pixel.getX(),pixel.getY(), pixel.getColor());
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
					contentPanel.updateBoard(controller.getPixels());
					contentPanel.setRobotX(controller.getXCo());
					contentPanel.setRobotY(controller.getYCo());
					contentPanel.setRobotSpeed(controller.getSpeed());
					contentPanel.setRobotAngle(controller.getAngle());
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
        	controller.moveForward();
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
        	controller.rotateLeft();
        }
        	
        else if(e.getSource() == rightButton){
        	actionLabel.setText("The robot is turning right!");
        	rightButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        	controller.rotateRight();
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
        	//We make the controller connect to a real robot
        	controller.connectNewBtRobot();
        }
        
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
    	angleLabel.setText("Angle: "+Double.valueOf(angle).intValue());
    }

	public Controller getController() {
		return controller;
	}
	
}
