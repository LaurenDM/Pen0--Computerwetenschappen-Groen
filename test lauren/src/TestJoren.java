import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

/**
 * A Swing test class. Heavily uses code from the Java Swing tutorial.
 * @author r0258417
 *
 */
public class TestJoren extends WindowAdapter implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the window.
	 */
	public TestJoren() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		
	}
	
	/**
	 * 
	 */
	private static void createAndShowGUI() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }

			
        });
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
