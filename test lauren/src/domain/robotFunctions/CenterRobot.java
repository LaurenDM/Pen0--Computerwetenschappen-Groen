package domain.robotFunctions;

import domain.robots.CannotMoveException;
import domain.robots.RobotPilot;

public class CenterRobot {
	
	private RobotPilot robot;
	
	public CenterRobot(RobotPilot robotPilot) {
		this.robot = robotPilot;
	}
	
	public void center(){
		double[] dist = checkDistances();
		if(dist[1] < 100)
			checkForward(dist[1]);
		if(dist[0] < 100)
			checkForward(dist[1]);
		checkLeft(dist[0]);
		robot.turnRight();
		if(dist[2] < 100){
			robot.turnRight();
			checkRight(dist[2]);
		}
		
	}
	
	private void checkRight(double dist){
		double distance = (dist % 40) % 20;
		try {
			robot.move(distance);
		} catch (CannotMoveException e) {
			// Normally never gets called.
			e.printStackTrace();
		}
		robot.turnLeft();
		
	}
	
	private void checkForward(double dist){
		double distance = (dist % 40) % 20;
		try {
			robot.move(distance);
		} catch (CannotMoveException e) {
			// Normally never gets called.
			e.printStackTrace();
		}
	}
	
	private void checkLeft(double dist){
		double distance = (dist % 40) % 20;
		robot.turnLeft();
		try {
			robot.move(distance);
		} catch (CannotMoveException e) {
			// Normally never gets called.
			e.printStackTrace();
		}
		
		
	}
	private double[] checkDistances(){
		double[] distances = new double[3];
		robot.turnSensorLeft();
		distances[0] = robot.readUltrasonicValue();
		robot.turnSensorForward();
		distances[1] = robot.readUltrasonicValue();
		robot.turnSensorRight();
		distances[2] = robot.readUltrasonicValue();
		robot.turnSensorForward();
		return distances;
	}
}
