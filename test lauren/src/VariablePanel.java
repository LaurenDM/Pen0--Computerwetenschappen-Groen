import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VariablePanel {
	
	final JPanel variableGUI = new JPanel();
	JPanel titlePanel;
	JLabel titleLabel;
	static int totalXDimensions = 700;
    static int totalYDimensions = 700;
    static int buttonXDimension = 90;
    static int buttonYDimension = 30;
	
	public VariablePanel() {
		variableGUI.setLayout(null);
    	titlePanel = new JPanel();
    	titlePanel.setLayout(null);
    	titlePanel.setLocation(0, 0);
    	titlePanel.setSize(400, 400);
        variableGUI.add(titlePanel);
        
        
        titleLabel = new JLabel("P&O - Team Groen - Variables");
        titleLabel.setLocation(0, 0);
        titleLabel.setSize(totalXDimensions, 30);
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setForeground(Color.black);
        titlePanel.add(titleLabel);
        
        
        JTextField firstImputfield = new JTextField(16);
        firstImputfield.setBounds(20, 20, 100, 30);
        titlePanel.add(firstImputfield);
        
        variableGUI.setOpaque(true);
	}
	
	public JPanel getContentPanel(){
		return variableGUI;
	}
}
