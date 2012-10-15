package gui;
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



public class ContentPanel implements ActionListener {
	
	static JFrame frame = new JFrame("P&O - Groen");
    static JFrame variableFrame = new JFrame("P&O - Groen - Variables");
    private JPanel titlePanel,titlePanel2, buttonPanel, inputPanel, variablePanel, debugPanel;
    private JLabel buttonLabel, actionLabel, titleLabel;
    private JLabel xLabel, yLabel, speedLabel, angleLabel;
    private JButton upButton, rightButton,leftButton, downButton, cancelButton, variableButton;
    private JTextArea debugText;
    final JPanel totalGUI = new JPanel();
    final JPanel variableGUI = new JPanel();
    static int totalXDimensions = 700;
    static int totalYDimensions = 700;
    static int buttonXDimension = 90;
    static int buttonYDimension = 30;
    DrawingPanel drawingPanel;
	
    
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
		// We create a bottom JPanel to place everything on.
        totalGUI.setLayout(null);
        
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
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP){
					actionLabel.setText("The robot is going forward!");
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					actionLabel.setText("The robot is turning left!");
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					actionLabel.setText("The robot is going back!");
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
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					actionLabel.setText("The robot is doing nothing atm!");
					leftButton.setSelected(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP){
					actionLabel.setText("The robot is doing nothing atm!");
					upButton.setSelected(false);
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					actionLabel.setText("The robot is doing nothing atm!");
					downButton.setSelected(false);
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					actionLabel.setText("The robot is turning right!");
					rightButton.setSelected(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_UP){
					actionLabel.setText("The robot is going forward!");
					upButton.setSelected(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					actionLabel.setText("The robot is turning left!");
					leftButton.setSelected(true);
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					actionLabel.setText("The robot is going back!");
					downButton.setSelected(true);
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
        drawingPanel = new DrawingPanel();
        fixPanelLayout(drawingPanel, 350, 500, 25, 50);
        drawingPanel.setBackground(Color.WHITE);
        
        //________________________
        //Creating variable panel
        VariablePanel variablePanel = new VariablePanel(variableFrame);
        variableFrame.setContentPane(variablePanel.getContentPanel());;
        variableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        variableFrame.setSize(400, 400);
        
        
        
        
        drawLine(100, 100, 300, 300);
	}
	
	public void actionPerformed(ActionEvent e) {
        if(e.getSource() == upButton){
        	actionLabel.setText("The robot is going forward!");
        	upButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        }
            
        else if(e.getSource() == downButton){
        	actionLabel.setText("The robot is going back!");
        	downButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        }
        	
        else if(e.getSource() == leftButton){
        	actionLabel.setText("The robot is turning left!");
        	leftButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        }
        	
        else if(e.getSource() == rightButton){
        	actionLabel.setText("The robot is turning right!");
        	rightButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        }
        	
        else if(e.getSource() == cancelButton){
        	actionLabel.setText("The robot cancelled all actions.");
        	cancelButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        }
        else if(e.getSource() == variableButton){
        	variableFrame.setVisible(true);
        	actionLabel.setText("The robot is setting parameters.");
        	variableButton.setSelected(false);
        	buttonPanel.requestFocusInWindow();
        	
        }	
        
    }
	
	
	public JPanel getContentPanel(){
		return totalGUI;
	}
	
	public void drawLine(int x, int y, int z, int u){
        Graphics g = drawingPanel.getGraphics(); 
        g.setColor(Color.black);
        g.drawLine(x, y, z, u);
        drawingPanel.repaint();
	}
	
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
	
}
