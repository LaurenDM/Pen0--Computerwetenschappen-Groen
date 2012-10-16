package controller;

import domain.PolygonDriver;
import domain.robots.BTRobot;

//Robotclass, generates/keeps track of current positioning.
public class Controller {
	
	private BTRobot btRobot;
	
	public Controller() {
		try {
			btRobot = new BTRobot();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void polygonDriver(int numberOfVertices, double edgeLength) {
		PolygonDriver driver = new PolygonDriver(btRobot);
		driver.drive(numberOfVertices, edgeLength);
		
	}

	public void moveForward() {
		btRobot.forward();
		
	}

	public void moveBack() {
		btRobot.backward();
		
	}

	public void rotateRight() {
		btRobot.turnRight();
		
	}

	public void rotateLeft() {
		btRobot.turnRight();
		
	}

	public void cancel() {
		btRobot.stop();
		
	}
	
	public double getXCo(){
		return btRobot.getPosition().getX();
	}

	public double getYCo(){
		return btRobot.getPosition().getY();
	}

	public double getSpeed() {
		return btRobot.getMovingSpeed();
	}

	public double getAngle() {
		return btRobot.getOrientation();
	}


	
	
}
