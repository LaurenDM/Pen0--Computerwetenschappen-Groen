package domain.robotFunctions;

import domain.robots.CannotMoveException;
import domain.robots.Robot;

public class ExploreMaze {
	
	
	private enum Direction {
	    LEFT,FORWARD,RIGHT,BACKWARD
	}
	private Robot robot;
	private final int valuedDistance = 30;
	private final int distanceBlocks = 40;
	
	public ExploreMaze(Robot newRobot){
		this.robot = newRobot;
	}
	public void start(){
		//TODO stopcondition needs to be determined.
		while(true){
			double[] distances = new double[3];
			distances = checkDistances();
			Direction direction = getNextDirection(distances);
			move(direction);
		}
	}
	
	private void move(Direction direction){
		switch (direction) {
			case LEFT:
				robot.turnLeft();
				robotMove();
				break;
			case FORWARD:
				robotMove();
				break;
			case RIGHT:
				robot.turnRight();
				robotMove();
			case BACKWARD:
				robot.turnRight();
				robot.turnRight();
				robotMove();
			default:
				break;
			}
	}
	
	private void robotMove(){
		try {
			robot.move(distanceBlocks);
		} catch (CannotMoveException e) {
			//Normally never gets called.
			e.printStackTrace();
		}
	}
	private Direction getNextDirection(double[] distances){
		if(distances[0] > valuedDistance)
			return Direction.LEFT;
		else if(distances[1] > valuedDistance)
			return Direction.FORWARD;
		else if(distances[2] > valuedDistance)
			return Direction.RIGHT;
		return Direction.BACKWARD;
	}
	
	private double[] checkDistances(){
		double[] distances = new double[3];
		robot.turnSensorLeft();
		sleep();
		distances[0] = robot.readUltrasonicValue();
		robot.turnSensorForward();
		sleep();
		distances[1] = robot.readUltrasonicValue();
		robot.turnSensorRight();
		sleep();
		distances[2] = robot.readUltrasonicValue();
		robot.turnSensorForward();
		return distances;
	}
	private void sleep(){
		 try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
