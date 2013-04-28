package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

public class ConnectionPanel implements ActionListener{

	final JPanel variableGUI = new JPanel();
	JPanel titlePanel;
	JLabel titleLabel;
	JFrame surroundingFrame;
	static int totalXDimensions = 400;
	static int totalYDimensions = 400;
	private JButton connectButton, okButton, teleportButton;
//	forwardButton, leftButton, rightButton, catapultButton;
	private Controller controller;

	public ConnectionPanel(JFrame surroundingFrame, Controller controller) {
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

		titleLabel = new JLabel("P&O - Team Groen - Connection through HTTTP");
		titleLabel.setLocation(0, 0);
		titleLabel.setSize(totalXDimensions, 30);
		titleLabel.setHorizontalAlignment(0);
		titleLabel.setForeground(Color.black);
		titlePanel.add(titleLabel);

		connectButton = new JButton("Set Ready");
		connectButton.setBounds(20, 20, 200, 30);
		titlePanel.add(connectButton);
		connectButton.addActionListener(this);
		
		teleportButton = new JButton("Teleport to starting position");
		teleportButton.setBounds(20, 80, 200, 30);
		titlePanel.add(teleportButton);
		teleportButton.addActionListener(this);

//		leftButton = new JButton("Look Left");
//		leftButton.setBounds(20, 80, 200, 30);
//		titlePanel.add(leftButton);
//		leftButton.addActionListener(this);
//		
//		rightButton = new JButton("Look Right");
//		rightButton.setBounds(20,140,200,30);
//		titlePanel.add(rightButton);
//		rightButton.addActionListener(this);
//
//		catapultButton = new JButton("Launch Catapult");
//		catapultButton.setBounds(20,200,200,30);
//		titlePanel.add(catapultButton);
//		catapultButton.addActionListener(this);
		
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
		if (e.getSource() == connectButton){
			connectButton.setSelected(false);
			controller.setReady(true);
		}
		if (e.getSource() == teleportButton){
			connectButton.setSelected(false);
			controller.teleport();
		}
//		if (e.getSource() == leftButton){
//			leftButton.setSelected(false);
//			controller.turnSensorLeft();
//		}
//		if(e.getSource() == rightButton){
//			rightButton.setSelected(false);
//			controller.turnSensorRight();
//		}
//		if(e.getSource() == catapultButton){
//			catapultButton.setSelected(false);
//			controller.launchCatapult();
//		}
	}
	
}
