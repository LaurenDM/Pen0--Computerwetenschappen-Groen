package controller;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import domain.PolygonDriver;
import domain.robots.BTRobotPilot;
import domain.robots.Robot;
import domain.robots.RobotPilot;
import domain.robots.SimRobotPilot;
import domain.util.ColorPolygon;

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
		currentRobot.turnLeft();
		
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

	public List<ColorPolygon> getColorPolygons(){
		List<ColorPolygon> colPolyList=new ArrayList<ColorPolygon>();
		colPolyList.add(currentRobot.getRobotPolygon());
		return colPolyList;
	}
}
