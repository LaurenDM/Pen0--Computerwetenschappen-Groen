package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

public class BallBarcodePanel implements ActionListener{
	final JPanel variableGUI = new JPanel();
	JPanel titlePanel;
	JLabel titleLabel;
	JFrame surroundingFrame;
	static int totalXDimensions = 400;
	static int totalYDimensions = 210;
	private JButton submitButton, correctBarcodeButton, otherBarcodeButton1, otherBarcodeButton2,otherBarcodeButton3;
	private Controller controller;
	private JTextField correctBarcodeField; // ,falseBarcodeField1,falseBarcodeField2;

	public BallBarcodePanel(JFrame surroundingFrame, Controller controller) {
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

		titleLabel = new JLabel("P&O - Team Groen - Barcode numbers for teams");
		titleLabel.setLocation(0, 0);
		titleLabel.setSize(totalXDimensions, 30);
		titleLabel.setHorizontalAlignment(0);
		titleLabel.setForeground(Color.black);
		titlePanel.add(titleLabel);
		
		JLabel correctBarcodeLabel = new JLabel("Barcode number:");
		correctBarcodeLabel.setBounds(20, 20, 200, 30);
		titlePanel.add(correctBarcodeLabel);
		correctBarcodeField = new JTextField(16);
		correctBarcodeField.setText("0");
		correctBarcodeField.setBounds(20, 50, 50, 30);
		titlePanel.add(correctBarcodeField);
		
		submitButton = new JButton("Submit the barcode number");
		submitButton.setBounds(20, 150, 200, 30);
		titlePanel.add(submitButton);
		submitButton.addActionListener(this);

	}

	public JPanel getContentPanel() {
		return variableGUI;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitButton) {
			controller.setBallNumber(getBallNumber());
			//controller.setFalseBallBarcodes(getFalseBallBarcodes());
			surroundingFrame.setVisible(false);
		}
		
	}
	public int getBallNumber(){
		return Integer.parseInt(correctBarcodeField.getText());
	}
	
	
	
//	public int[] getFalseBallBarcodes(){
//		int[] list = new int[] {Integer.parseInt(falseBarcodeField1.getText()), 
//				Integer.parseInt(falseBarcodeField2.getText())};
//		return list;
// 	}
}
