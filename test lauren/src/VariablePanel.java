import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VariablePanel implements ActionListener {

	final JPanel variableGUI = new JPanel();
	JPanel titlePanel;
	JLabel titleLabel;
	JFrame surroundingFrame;
	static int totalXDimensions = 400;
	static int totalYDimensions = 400;
	private JButton okButton;
	private JTextField edgeLengthIputfield;
	private JTextField nbVerticesInputfield;
	

	public VariablePanel(JFrame surroundingFrame) {
		if (surroundingFrame == null) {
			throw new IllegalArgumentException();
		}
		this.surroundingFrame=surroundingFrame;
		
		variableGUI.setLayout(null);
		titlePanel = new JPanel();
		titlePanel.setLayout(null);
		titlePanel.setLocation(0, 0);
		titlePanel.setSize(totalXDimensions, totalYDimensions);
		variableGUI.add(titlePanel);

		titleLabel = new JLabel("P&O - Team Groen - Variables");
		titleLabel.setLocation(0, 0);
		titleLabel.setSize(totalXDimensions, 30);
		titleLabel.setHorizontalAlignment(0);
		titleLabel.setForeground(Color.black);
		titlePanel.add(titleLabel);

		JLabel edgeLengthLabel = new JLabel("Fill in the edgelength:");
		edgeLengthLabel.setBounds(20, 20, 200, 30);
		titlePanel.add(edgeLengthLabel);
		edgeLengthIputfield = new JTextField(16);
		edgeLengthIputfield.setBounds(20, 50, 100, 30);
		titlePanel.add(edgeLengthIputfield);

		JLabel nBVerticesLabel = new JLabel("Fill in the number of vertices:");
		nBVerticesLabel.setBounds(20, 80, 200, 30);
		titlePanel.add(nBVerticesLabel);
		nbVerticesInputfield = new JTextField(16);
		nbVerticesInputfield.setBounds(20, 110, 100, 30);
		titlePanel.add(nbVerticesInputfield);

		variableGUI.setOpaque(true);

		okButton = new JButton();
		okButton.setBounds(20, 140, 60, 30);
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
			variableGUI.setVisible(false);
			surroundingFrame.setVisible(false);
		}
	}
	public int getNumberOfVertices(){
		return Integer.parseInt(nbVerticesInputfield.getText());
	}
	public double getEdgeLength(){
		return Integer.parseInt(edgeLengthIputfield.getSelectedText());
	}
	
}
