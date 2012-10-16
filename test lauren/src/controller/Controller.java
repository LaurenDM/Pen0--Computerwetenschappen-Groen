package controller;

import domain.PolygonDriver;
import domain.robots.BTRobotPilot;
import domain.robots.Robot;
import domain.robots.RobotPilot;
import domain.robots.SimRobotPilot;

//Robotclass, generates/keeps track of current positioning.
public class Controller {
	
	private RobotPilot currentRobotPilot;
	private Robot robot;
	
	public Controller() {
			currentRobotPilot=new BTRobotPilot();
			robot = new Robot(currentRobotPilot);
		
	}
	
	public void polygonDriver(int numberOfVertices, double edgeLength) {
		PolygonDriver driver = new PolygonDriver(robot);
		driver.drive(numberOfVertices, edgeLength);
		
	}

	public void moveForward() {
		robot.forward();
		
	}

	public void moveBack() {
		robot.backward();
		
	}

	public void rotateRight() {
		robot.turnRight();
		
	}

	public void rotateLeft() {
		robot.turnRight();
		
	}

	public void cancel() {
		robot.stop();
		
	}
	
	public double getXCo(){
		return robot.getPosition().getX();
	}

	public double getYCo(){
		return robot.getPosition().getY();
	}

	public double getSpeed() {
		return robot.getMovingSpeed();
	}

	public double getAngle() {
		return robot.getOrientation();
	}


	
	
}
