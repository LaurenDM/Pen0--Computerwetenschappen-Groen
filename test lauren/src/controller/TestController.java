package controller;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import domain.calibration.TestDriver;

import gui.TestGUI;
import gui.TestTextInterface;

public class TestController {
	
	private Controller robotController;
	private TestDriver testDriver;
	private int testAmount;
	private ArrayList<Method> methodList = new ArrayList<Method>();
	private ArrayList<Method> selectedMethods = new ArrayList<Method>();
	
	public static void main(String[] args) {
		TestController testController = new TestController();
	}
	
	public TestController(){
		robotController = new Controller();
		testDriver = new TestDriver(robotController.getRobot(), this);
		loadTests();
		TestTextInterface.printWelcome();
        TestTextInterface.execCommandLoop(this);
	}
	
	public void connectNewBtRobot(){
		robotController.connectNewBtRobot();
	}
	
	private void loadTests(){
		methodList = new ArrayList<Method>();
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
			if(meth.getName().toLowerCase().startsWith(type,4)){ selectedMethods.add(meth); }
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
			if(meth.getName().toLowerCase().contains(partOfName)){ selectedMethods.add(meth); }
		}
		if(selectedMethods.isEmpty()){ throw(new IllegalArgumentException("No commands were selected!")); }
	}
	
	/**
	 * Returns all test methods
	 */
	public ArrayList<Method> getTestMethodList(){
		return methodList;
	}
	
	/**
	 * Returns all selected test methods
	 */
	public ArrayList<Method> getSelectedMethodList(){
		return selectedMethods;
	}
	
	public void runSelectedTests() throws IllegalArgumentException{
		if(selectedMethods.isEmpty()){
			throw(new IllegalArgumentException("No commands are selected!"));
		} else {
			for(Method meth:selectedMethods){
				try {
					System.out.println(meth.getName());
					Double doubleArgument = Double.parseDouble(TestTextInterface.getUserInput("Distance/Angle? "));
					Class<?> otherType = (meth.getParameterTypes().length>1?meth.getParameterTypes()[1]:null);
					if(otherType!=null){
						Integer otherArgument = Integer.valueOf((TestTextInterface.getUserInput("How many times? ")));
						meth.invoke(testDriver,doubleArgument,otherArgument);
					} else {
						meth.invoke(testDriver,doubleArgument);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setRobotController(Controller controller){
		robotController = controller;
	}
	
	public Controller getRobotController(){
		return robotController;
	}
	
	public void endTest(){
		testDriver.endTest();
	}

	public String askUserInput(String string) {
		return TestTextInterface.getUserInput(string);
	}

}
