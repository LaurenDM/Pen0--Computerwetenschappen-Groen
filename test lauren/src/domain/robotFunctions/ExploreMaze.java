package domain.robotFunctions;

import domain.robots.CannotMoveException;
import domain.robots.Robot;
import domain.robots.RobotPilot;
import domain.robots.SimRobotPilot;

public class ExploreMaze {
	
	
	private enum Direction {
	    LEFT,FORWARD,RIGHT,BACKWARD
	}
	private RobotPilot robot;
	private final int valuedDistance = 25;
	private final int distanceBlocks = 40;
	
	public ExploreMaze(RobotPilot simRobotPilot){
		this.robot = simRobotPilot;
	}
	public void start(){
		//TODO stopcondition needs to be determined.
		while(true){
			System.out.println("test");
			double[] distances = new double[3];
			
			//makeWall(distances);
			distances = checkDistances();
			for (int i = 0; i < distances.length; i++) {
				System.out.println(distances[i]);
			}
			Direction direction = getNextDirection(distances);
			move(direction);
		}
	}
	
	private void makeWall(double[] distances) {
		
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
		distances[0] = robot.readUltrasonicValue();
		robot.turnSensorForward();
		distances[1] = robot.readUltrasonicValue();
		robot.turnSensorRight();
		distances[2] = robot.readUltrasonicValue();
		robot.turnSensorForward();
		return distances;
	}
}
