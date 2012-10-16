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
	
	//Current coordinate in the GUI
	private int xCo;
	
	public int getXCo(){
		return xCo;
	}
	
	public void setXCo(int co){
		xCo = co;
	}
	
	//Current coordinate in the GUI
	private int yCo;
	
	public int getYCo(){
		return yCo;
	}
	
	public void setYCo(int co){
		yCo = co;
	}

	public void rotateRight() {
		btRobot.turnRight();
		
	}

	public void moveForward() {
		btRobot.forward();
		
	}

	public void moveBack() {
		btRobot.backward();
		
	}

	public void rotateLeft() {
		btRobot.turnRight();
		
	}

	public void cancel() {
		btRobot.stop();
		
	}
	
	public void polygonDriver(int numberOfVertices, double edgeLength) {
		PolygonDriver driver = new PolygonDriver(btRobot);
		driver.drive(numberOfVertices, edgeLength);
		
	}

	public double getSpeed() {
		return btRobot.getMovingSpeed();
	}
	public double getAngle() {
		return btRobot.getOrientation();
	}


	
	
}
