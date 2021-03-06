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
		if(dist[1] < 30)
			checkForward(dist[1]);
		if(dist[0] < 30)
			checkForward(dist[1]);
		checkLeft(dist[0]);
		robot.turnRight();
		if(dist[2] < 30){
			robot.turnRight();
			checkRight(dist[2]);
		}
		
	}
	
	private void checkRight(double dist){
		double distance = (dist % 40);
		try {
			robot.move(getFixedDistance(distance));
		} catch (CannotMoveException e) {
			// Normally never gets called.
			e.printStackTrace();
		}
		robot.turnLeft();
		
	}
	
	private void checkForward(double dist){
		double distance = (dist % 40);
		try {
			robot.move(getFixedDistance(distance));
		} catch (CannotMoveException e) {
			// Normally never gets called.
			e.printStackTrace();
		}
	}
	
	private void checkLeft(double dist){
		double distance = (dist % 40);
		robot.turnLeft();
		try {
			robot.move(getFixedDistance(distance));
		} catch (CannotMoveException e) {
			// Normally never gets called.
			e.printStackTrace();
		}
	}
	
	private double getFixedDistance(double distance){
		double newDistance;
		if(distance > 17){
			newDistance = distance - 17;
		}
		else{
			newDistance = -1 * (17 - distance);
		}
		//Marge waarmee we kunnen werken.
		if(newDistance < 4){
			newDistance = 0;
		}
		return newDistance;
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
