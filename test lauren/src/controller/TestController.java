package controller;

import java.io.Console;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import domain.calibration.TestDriver;

import gui.TestGUI;
import gui.TestTextInterface;

public class TestController {
	
	private Controller robotController;
	private TestDriver testDriver;
	private ArrayList<Method> methodList;
	private ArrayList<Method> selectedMethods;
	
	public static void main(String[] args) {
		TestController testController = new TestController();
	}
	
	public TestController(){
		robotController = new Controller();
		testDriver = new TestDriver(robotController.getRobot(), null);
		loadTests();
		//robotController.connectNewBtRobot();	
		TestTextInterface.printWelcome();
        TestTextInterface.execCommandLoop(this);
	}
	
	private void loadTests(){
		ArrayList<Method> methodList = new ArrayList<Method>();
		for(Method meth:TestDriver.class.getMethods()){
			if(meth.getName().startsWith("test")){ methodList.add(meth); }
		}
	}
	
	/**
	 * Selects test methods of the right type into selectedMethods based on the name that's supplied in type
	 * @param type If none of the test methods start with this string after "test" an IllegalArgumentException is thrown.
	 */
	public void selectTestMethodsOnType(String type) throws IllegalArgumentException{
		selectedMethods.clear();
		for(Method meth:methodList){
			if(meth.getName().startsWith(type,4)){ selectedMethods.add(meth); }
		}
		if(selectedMethods.isEmpty()){ throw(new IllegalArgumentException("No commands were selected!")); }
	}
	
	/**
	 * Selects test methods that contain the String partOfName
	 * @param partOfName If none of the test methods contain this string an IllegalArgumentException is thrown.
	 */
	public void selectTestMethodsOnName(String partOfName) throws IllegalArgumentException{
		selectedMethods.clear();
		for(Method meth:methodList){
			if(meth.getName().contains(partOfName)){ selectedMethods.add(meth); }
		}
		if(selectedMethods.isEmpty()){ throw(new IllegalArgumentException("No commands were selected!")); }
	}
	
	/**
	 * Returns all test methods
	 */
	public List<Method> getTestMethodList(){
		return methodList;
	}
	
	/**
	 * Returns all selected test methods
	 */
	public List<Method> getSelectedMethodList(){
		return selectedMethods;
	}
	
	public void runSelectedTests() throws IllegalArgumentException{
		if(selectedMethods.isEmpty()){
			throw(new IllegalArgumentException("No commands are selected!"));
		} else {
			for(Method meth:selectedMethods){
				meth.
			}
		}
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
