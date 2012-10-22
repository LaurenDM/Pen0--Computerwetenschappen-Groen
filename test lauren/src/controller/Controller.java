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

	public void rotateRightNonBlocking() {
		(new TurnRightThread()).start();
	}

	public void rotateLeftNonBlocking() {
		(new TurnLeftThread()).start();
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
}
