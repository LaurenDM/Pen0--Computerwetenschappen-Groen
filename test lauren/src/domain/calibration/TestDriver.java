/**
 * 
 */
package domain.calibration;

import controller.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import domain.calibration.TestDriver.StepType;
import domain.robots.CannotMoveException;
import domain.robots.Robot;

/**
 * @author Joren
 *
 */
public class TestDriver {	
	private static Robot robot;
	private static TestController testController;
	private static boolean testRunning;
	
	public TestDriver(Robot robot,TestController testController){
		if(testRunning){
			throw(new IllegalArgumentException("A test is already running"));
		} else {
			TestDriver.robot=robot;
			TestDriver.testController=testController;
			TestDriver.testRunning=true;
		}
	}
	
	public void endTest(){
		TestDriver.testRunning=false;
	}
	
	private void test(TestType testType, StepType stepType, int steps, double distance, boolean requireSetup, String extraConditions) throws CannotMoveException{
		for(int i=0; i<=steps; i++){
			if(requireSetup){
				testType.setup(stepType, distance);
			} else { i++; }	
			String[] measurements = testType.measure();			
			testType.log(Integer.valueOf(i).toString());
			testType.log(Double.valueOf(distance).toString());
			for(String string:measurements){testType.log(string);}
			testType.log("\n");
			testType.step(stepType, i*distance/steps, (double)calcPolygonAngles(steps));
		}
		logTestResults(testType.logFileName()+stepType.logFileExtension()+extraConditions, testType.getLog());
	}
	
	private void test(TestType testType, StepType stepType, int steps, double distance, boolean requireSetup, String extraConditions, int times) throws CannotMoveException{
		for(int i=0;i<times;i++){
			test(testType,stepType,steps,distance,requireSetup,extraConditions);
		}
	}
	
	
	public static enum TestType {
		LIGHT_SENSOR{
			public String logFileName(){
				return "testLight";
			}
			@Override
			public String[] measure() {
				String[] retString = new String[1];
				retString[0]=Double.valueOf(robot.readLightValue()).toString();
				return retString;
			}
		}, DISTANCE_SENSOR{
			public String logFileName(){
				return "testDistance";
			}
			@Override
			public String[] measure() {
				String[] retString = new String[1];
				retString[0]=Double.valueOf(robot.readUltrasonicValue()).toString();
				return retString;
			}
		}, DRIVE{
			public String logFileName(){
				return "testDrive";
			}
			@Override
			public String[] measure() {
				String[] retString = new String[1];
				retString[0]=testController.askUserInput("Difference?");
				return retString;
			}
		};
		private String log="";
		public abstract String logFileName();
		public void setup(StepType stepType, double... input){
			if(input!=null){
				double[] inputs = input;
				for(double d:inputs){
					d= -d/2;
				}
				try {
					stepType.execute(input);
				} catch (CannotMoveException e) {
					
				}
			}
		}
		public void step(StepType stepType, double... input) throws CannotMoveException{
			stepType.execute(input);
		}
		public abstract String[] measure();
		public void log(String...strings){
			for(String string:strings){
				log+=string+" ";
			}
		}
		public String getLog(){
			return log;
		}
	}
	
	public static enum StepType {
		MOVE{
			@Override
			public void execute(double... input) throws CannotMoveException {
				try{
					robot.move(input[0]);
				} catch(CannotMoveException c) {
					throw(new CannotMoveException());
				}
			}
			@Override
			public String logFileExtension() {
				return "Move";
			}
			
		}, TURN{
			@Override
			public void execute(double... input) {
				robot.turn(input[0]);
			}
			@Override
			public String logFileExtension() {
				return "Turn";
			}
			
		}, POLYGON{
			@Override
			public void execute(double... input) throws CannotMoveException {
				robot.move(input[0]);
				robot.turn(input[1]);
			}
			@Override
			public String logFileExtension() {
				return "Polygon";
			}
		};
		public abstract void execute(double... input) throws CannotMoveException;
		public abstract String logFileExtension();
	}
	
	
	public void testDriveDistance(double distance, int times) throws CannotMoveException{
		test(TestType.DRIVE,StepType.MOVE,1,distance,false,"Fixed",times);
	}
	public void testDriveTurn(double angle, int times){
		try {
			test(TestType.DRIVE,StepType.TURN,1,angle,false,"Fixed",times);
		} catch (CannotMoveException e) {
			//Unreachable
		}
	}
	//Deze test is een speciaal geval... 
//	public void testDrivePolygon(double edgelength, int sides) throws CannotMoveException{
//		test(TestType.DRIVE,StepType.POLYGON,sides,edgelength,false,"");
//	}
	public void testLightWood(double distance, int times) throws CannotMoveException{
		test(TestType.LIGHT_SENSOR,StepType.MOVE,20,distance,false,"Wood",times);
	}
	public void testLightLine(double distance, int times) throws CannotMoveException{
		test(TestType.LIGHT_SENSOR,StepType.MOVE,20,distance,false,"Line",times);
	}
	public void testLightBarcode(double distance, int times) throws CannotMoveException{
		test(TestType.LIGHT_SENSOR,StepType.MOVE,20,distance,false,"Barcode",times);
	}
	public void testDistanceDrive(double distance, int times) throws CannotMoveException{
		test(TestType.DISTANCE_SENSOR,StepType.MOVE,20,distance,false,"",times);
	}
	public void testDistanceTurn(double angle, int times){
		try {
			test(TestType.DISTANCE_SENSOR,StepType.TURN,(int) (angle/5),angle,true,"",times);
		} catch (CannotMoveException e) {
			//Unreachable
		}
	}
	
	public void logTestResults(String logFileName, String log){
		BufferedWriter logOutputWriter = null;
		try {
			logOutputWriter = new BufferedWriter(new FileWriter(logFileName + ".txt"));
			logOutputWriter.append(log);
			logOutputWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				logOutputWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private double calcPolygonAngles(int nbVertices) {
		return 180-((double)((nbVertices-2)*180))/((double)nbVertices);
	}
	
}
