package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

public class SensorOrientationPanel implements ActionListener{

	final JPanel variableGUI = new JPanel();
	JPanel titlePanel;
	JLabel titleLabel;
	JFrame surroundingFrame;
	static int totalXDimensions = 400;
	static int totalYDimensions = 400;
	private JButton okButton, forwardButton, leftButton, rightButton, catapultButton;
	private Controller controller;

	public SensorOrientationPanel(JFrame surroundingFrame, Controller controller) {
		if (surroundingFrame == null) {
			throw new IllegalArgumentException();
		}
		this.surroundingFrame=surroundingFrame;
		this.controller = controller;
		
		variableGUI.setLayout(null);
		titlePanel = new JPanel();
		titlePanel.setLayout(null);
		titlePanel.setLocation(0, 0);
		titlePanel.setSize(totalXDimensions, totalYDimensions);
		variableGUI.add(titlePanel);

		titleLabel = new JLabel("P&O - Team Groen - Ultrasonicsensor Orientation");
		titleLabel.setLocation(0, 0);
		titleLabel.setSize(totalXDimensions, 30);
		titleLabel.setHorizontalAlignment(0);
		titleLabel.setForeground(Color.black);
		titlePanel.add(titleLabel);

		forwardButton = new JButton("Look Forward");
		forwardButton.setBounds(20, 20, 200, 30);
		titlePanel.add(forwardButton);
		forwardButton.addActionListener(this);

		leftButton = new JButton("Look Left");
		leftButton.setBounds(20, 80, 200, 30);
		titlePanel.add(leftButton);
		leftButton.addActionListener(this);
		
		rightButton = new JButton("Look Right");
		rightButton.setBounds(20,140,200,30);
		titlePanel.add(rightButton);
		rightButton.addActionListener(this);

		catapultButton = new JButton("Launch Catapult");
		catapultButton.setBounds(20,140,200,30);
		titlePanel.add(catapultButton);
		catapultButton.addActionListener(this);
		
		okButton = new JButton();
		okButton.setBounds(20, 140, 120, 30);
		okButton.setText("OK");
		okButton.addActionListener(this);
		
		titlePanel.add(okButton);
		
		variableGUI.setOpaque(true);
	}

	public JPanel getContentPanel() {
		return variableGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			okButton.setSelected(false);
			surroundingFrame.setVisible(false);
		}
		if (e.getSource() == forwardButton){
			forwardButton.setSelected(false);
			controller.turnSensorForward();
		}
		if (e.getSource() == leftButton){
			leftButton.setSelected(false);
			controller.turnSensorLeft();
		}
		if(e.getSource() == rightButton){
			rightButton.setSelected(false);
			controller.turnSensorRight();
		}
	}
	
}
