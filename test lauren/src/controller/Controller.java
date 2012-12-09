package controller;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Motor;
import lejos.pc.comm.NXTCommandConnector;
import lejos.util.PilotProps;



import domain.PolygonDriver;
import domain.Position.Position;
import domain.maze.Board;
import domain.maze.MazeInterpreter;
import domain.maze.RandomMazeGenerator;
import domain.robotFunctions.BarcodeGenerator;
import domain.robots.BTRobotPilot;
import domain.robots.CannotMoveException;
import domain.robots.Robot;
import domain.robots.SimRobotPilot;
import domain.util.ColorPolygon;

//Robotclass, generates/keeps track of current positioning.
public class Controller {
	private static boolean stopped = false;
	private Thread executingThread;
	private Robot currentRobot;
	private Robot btRobot;
	private Robot simRobot;
	private Thread explorer;
	private SimRobotPilot simRobotPilot ;
	
	public Controller() {
		simRobotPilot = new SimRobotPilot();
		simRobot = new Robot(simRobotPilot);
		simRobot.getRobotPilot().setRobot(simRobot);
		currentRobot=simRobot;
		connectNewSimRobot();
	}


	  
	public void connectNewBtRobot() {
		if(btRobot==null)
		btRobot = new Robot(new BTRobotPilot());
		btRobot.getRobotPilot().setRobot(btRobot);
		currentRobot=btRobot;

//		currentRobot.findOrigin();

		currentRobot.setBoard(new Board());


	}
	public void connectNewSimRobot() {
		simRobotPilot = new SimRobotPilot();
		simRobot = new Robot(simRobotPilot);
		simRobot.getRobotPilot().setRobot(simRobot);
		currentRobot = simRobot ;
		currentRobot.setBoard(new Board());
	}
	
	public void polygonDriver(int numberOfVertices, double edgeLength) {
		PolygonDriver driver = new PolygonDriver(currentRobot);
		driver.drive(numberOfVertices, edgeLength);
		
	}

	public void moveForward() throws CannotMoveException {
		startNewThread(null);
		currentRobot.forward();
		
	}

	public void moveBack() {
		startNewThread(null);
		currentRobot.backward();
	}

	public void rotateRightNonBlocking() {
	startNewThread(new TurnRightThread());	
	}

	public void rotateLeftNonBlocking() {
		startNewThread(new TurnLeftThread());
	}
	public void startNewThread(Thread newThread){
		if (executingThread != null) {
			executingThread.interrupt();
			while(executingThread.isAlive());
		}

		executingThread = newThread;
		if (executingThread != null)
			executingThread.start();
	}
	
	public void cancel() {
		stopped = true;
		while(explorer!=null && explorer.isAlive()){}
		currentRobot.interrupt();
		startNewThread(null);
		currentRobot.stop();
		stopped = false;
	}
	
	public Position getPosition(){
		return currentRobot.getPosition();
	}
	
	public double getXCo(){
		return currentRobot.getPosition().getX();
	}

	public double getYCo(){
		return currentRobot.getPosition().getY();
	}

	public double getSpeed() {
		return currentRobot.getMovingSpeedSetting();
//		return currentRobot.getActualMovingSpeed();
	}

	public double getAngle() {
		return currentRobot.getOrientation();
	}

	public List<ColorPolygon> getColorPolygons(){
		List<ColorPolygon> colPolyList=new ArrayList<ColorPolygon>();
		colPolyList.add(currentRobot.getRobotPolygon());
		return colPolyList;
	}
	
	private class TurnRightThread extends Thread{
		@Override
		public void run(){
			currentRobot.turnRight();
		}
	}
	
	private class TurnLeftThread extends Thread{
		@Override
		public void run(){
			currentRobot.turnLeft();
		}
	}
	
	public void calibrateLightHigh(){
		currentRobot.calibrateLightHigh();
	}
	
	public void calibrateLightLow(){
		currentRobot.calibrateLightLow();
	}
	
	public double readLightValue(){
		return currentRobot.readLightValue();
	}
	
	public double readUltrasonicValue(){
		return currentRobot.readUltrasonicValue();
	}
	
	public boolean isTouching(){
		return currentRobot.isTouching();
	}
	
	public void turnUltrasonicSensor(int angle){
		currentRobot.turnUltrasonicSensor(angle);
	}
	
	public int getSensorAngle(){
		return currentRobot.getSensorAngle();
	}
	
	public void turnSensorRight(){
		currentRobot.turnSensorRight();
	}
	
	public void turnSensorLeft(){
		currentRobot.turnSensorLeft();
	}
	
	public void turnSensorForward(){
		currentRobot.turnSensorForward();
	}
	
	public boolean detectWhiteLine(){
		return currentRobot.detectWhiteLine();
	}
	
	public boolean wall(){
		final int DISTANCE_LIMIT= 50;
		return readUltrasonicValue() < DISTANCE_LIMIT;
	}

	public Robot getRobot() {
		return currentRobot;
	}
	public void arcForward(boolean left){
		currentRobot.arcForward(left);
	};

	public void arcBackward(boolean left) {
		currentRobot.arcBackward(left);
	};

	public void readMazeFromFile(String fileLocation) {
		MazeInterpreter MI = new MazeInterpreter(this.getRobot().getBoard());
		if(fileLocation.equals("nullnull")){
		RandomMazeGenerator RMG = new RandomMazeGenerator(MI);
		}
		else
		MI.readFile(fileLocation);
		
	}

	public void findLineAndStraighten() {
		Runnable straightener = new Runnable() {

			@Override
			public void run() {
				//currentRobot.findOpening();
				currentRobot.straighten();
			}
		};
		(new Thread(straightener)).start();
	}



	public void rotateAmount(int i) {
		currentRobot.setTurningSpeed(2);
		currentRobot.turn(i);
		currentRobot.resetToDefaultSpeeds();
		
	}
	
	public void findBlackLineAndCreateBarcode(){
		Runnable barcodeGen = new Runnable() {

			@Override
			public void run() {
				currentRobot.findBlackLine();
				currentRobot.scanBarcode();
			}
		};
		(new Thread(barcodeGen)).start();
	}

	public void startExplore() {
		if(explorer!=null){
			stopped = true;
			while(explorer.isAlive()){}
			stopped = false;
		}
		Runnable explore = new Runnable() {

			@Override
			public void run() {
				currentRobot.startExplore();
			}
		};
		explorer = new Thread(explore);
		explorer.start();
	}

	public void resumeExplore() {
		stopped = true;
		while(explorer.isAlive()){}
		stopped = false;
		explorer = new Thread(new Runnable() {
			public void run(){
				currentRobot.resumeExplore();
			}
		});
		explorer.start();
	}

	public void driveToFinish() {
		stopped = true;
		while(explorer.isAlive()){}
		stopped = false;
		explorer = new Thread(new Runnable() {
			public void run(){
				currentRobot.driveToFinish();
			}
		});
		explorer.start();
	}
	
	public void reset(){
		stopped = true;
		while(explorer!=null && explorer.isAlive()){}
		connectNewSimRobot();
	}

	public static boolean isStopped() {
		return stopped ;
	}



	public void autoCalibrateLight() {
		currentRobot.autoCalibrateLight();}
	
	public void disableError() {
		simRobotPilot.disableError();
	}
}
