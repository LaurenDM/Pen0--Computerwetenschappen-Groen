package controller;

import domain.PolygonDriver;
import domain.robots.BTRobotPilot;
import domain.robots.Robot;
import domain.robots.RobotPilot;
import domain.robots.SimRobotPilot;

//Robotclass, generates/keeps track of current positioning.
public class Controller {
	
	private Robot currentRobot;
	public Controller() {
		currentRobot = new Robot(new SimRobotPilot());
	}

	public void connectNewBtRobot() {
		currentRobot = new Robot(new BTRobotPilot());
	}
	
	public void connectNewSimRobot() {
		currentRobot = new Robot(new SimRobotPilot());
	}
	
	public void polygonDriver(int numberOfVertices, double edgeLength) {
		PolygonDriver driver = new PolygonDriver(currentRobot);
		driver.drive(numberOfVertices, edgeLength);
		
	}

	public void moveForward() {
		currentRobot.forward();
		
	}

	public void moveBack() {
		currentRobot.backward();
		
	}

	public void rotateRight() {
		currentRobot.turnRight();
		
	}

	public void rotateLeft() {
		currentRobot.turnRight();
		
	}

	public void cancel() {
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

	
	
}
