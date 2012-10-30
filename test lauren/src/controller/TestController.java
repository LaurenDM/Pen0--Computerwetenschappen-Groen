package controller;

import java.io.Console;

import javax.swing.SwingUtilities;

import gui.TestGUI;
import gui.TestTextInterface;

public class TestController {
	
	private Controller robotController;
	
	public static void main(String[] args) {
		TestController testController = new TestController();
	}
	
	public TestController(){
		robotController = new Controller();
		//robotController.connectNewBtRobot();	
		TestTextInterface.printWelcome();
        TestTextInterface.execCommandLoop(this);
	}
	
	public void setRobotController(Controller controller){
		robotController = controller;
	}
	
	public Controller getRobotController(){
		return robotController;
	}

	public String askUserInput(String...strings) {
		return null;
	}

}
