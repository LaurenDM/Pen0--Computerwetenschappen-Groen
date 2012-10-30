package controller;

import java.util.ArrayList;
import java.util.List;


import domain.PolygonDriver;
import domain.robots.BTRobotPilot;
import domain.robots.CannotMoveException;
import domain.robots.Robot;
import domain.robots.SimRobotPilot;
import domain.util.ColorPolygon;

//Robotclass, generates/keeps track of current positioning.
public class Controller {
	private Thread executingThread;
	private Robot currentRobot;
	private Robot btRobot;
	private Robot simRobot;
	
	public Controller() {
		simRobot = new Robot(new SimRobotPilot());
		currentRobot=simRobot;
	}

	public void connectNewBtRobot() {
		if(btRobot==null){
			btRobot = new Robot(new BTRobotPilot());
		}			
		currentRobot=btRobot;

	}
	
	public void connectNewSimRobot() {
		simRobot = new Robot(new SimRobotPilot());
		currentRobot = simRobot ;
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
		if(executingThread!=null){
			executingThread.interrupt();
		}
		executingThread=newThread;
		if(executingThread!=null)
		executingThread.start();
	}
	
	public void cancel() {
		startNewThread(null);
		currentRobot.stop();
		
	}
	
	public double getXCo(){
		return currentRobot.getPosition().getX();
	}

	public double getYCo(){
		return currentRobot.getPosition().getY();
	}

	public double getSpeed() {
		return currentRobot.getActualMovingSpeed();
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
	
	public int getSensorAngle(){
		return currentRobot.getSensorAngle();
	}
	
	public void turnUltrasonicSensor(int angle){
		currentRobot.turnUltrasonicSensor(angle);
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
	
	public void makeWallIfNecessary(){
		currentRobot.makeWallIfNecessary();
	}
}
