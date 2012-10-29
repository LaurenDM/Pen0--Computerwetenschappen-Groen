package gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Controller;

public class CalibrationPanel implements ActionListener {

	final JPanel variableGUI = new JPanel();
	JPanel titlePanel;
	JLabel titleLabel;
	JFrame surroundingFrame;
	static int totalXDimensions = 400;
	static int totalYDimensions = 400;
	private JButton okButton, calibrateHighButton, calibrateLowButton;
	private Controller controller;

	public CalibrationPanel(JFrame surroundingFrame, Controller controller) {
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

		titleLabel = new JLabel("P&O - Team Groen - Lightsensor Calibration");
		titleLabel.setLocation(0, 0);
		titleLabel.setSize(totalXDimensions, 30);
		titleLabel.setHorizontalAlignment(0);
		titleLabel.setForeground(Color.black);
		titlePanel.add(titleLabel);

		calibrateHighButton = new JButton("Calibrate High");
		calibrateHighButton.setBounds(20, 20, 200, 30);
		titlePanel.add(calibrateHighButton);

		calibrateLowButton = new JButton("Calibrate Low");
		calibrateLowButton.setBounds(20, 80, 200, 30);
		titlePanel.add(calibrateLowButton);

		variableGUI.setOpaque(true);

		okButton = new JButton();
		okButton.setBounds(20, 140, 120, 30);
		okButton.setText("OK");
		okButton.addActionListener(this);
		
		titlePanel.add(okButton);
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
		if (e.getSource() == calibrateHighButton){
			calibrateHighButton.setSelected(false);
			controller.calibrateLightHigh();
		}
		if (e.getSource() == calibrateLowButton){
			calibrateLowButton.setSelected(false);
			controller.calibrateLightLow();
		}
	}

}
