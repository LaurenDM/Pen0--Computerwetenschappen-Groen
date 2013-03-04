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
	private JButton submitButton;
	private Controller controller;
	private JTextField correctBarcodeField;

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

		titleLabel = new JLabel("P&O - Team Groen - Ball Number Input");
		titleLabel.setLocation(0, 0);
		titleLabel.setSize(totalXDimensions, 30);
		titleLabel.setHorizontalAlignment(0);
		titleLabel.setForeground(Color.black);
		titlePanel.add(titleLabel);
		
		JLabel correctBarcodeLabel = new JLabel("Own ball number:");
		correctBarcodeLabel.setBounds(20, 20, 200, 30);
		titlePanel.add(correctBarcodeLabel);
		correctBarcodeField = new JTextField(16);
		correctBarcodeField.setText("0");
		correctBarcodeField.setBounds(20, 50, 50, 30);
		titlePanel.add(correctBarcodeField);
		
		submitButton = new JButton("Submit the ball number");
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
			controller.setBallNumber(getCorrectBallNumber());
			controller.setFalseBallNumbers(getFalseBallNumber());
			surroundingFrame.setVisible(false);
		}
		
	}
	
	/**
	 * The number of the ball our robot needs to pick up.
	 * @return
	 */
	public int getCorrectBallNumber(){
		return Integer.parseInt(correctBarcodeField.getText());
	}
	
	
	/**
	 * Ball numbers are in the range [0,3] so this method returns a list with 3 ints, those that are not the correct numbers.
	 * @return
	 */
	public int[] getFalseBallNumber(){
		int[] list = new int[] {0,0,0};
		int index = 0;
		for(int i = 0; i<4; i++){
			if(i!=getCorrectBallNumber()){
				list[index] = i;
				index++;
			}
		}
		return list;
 	}
}
