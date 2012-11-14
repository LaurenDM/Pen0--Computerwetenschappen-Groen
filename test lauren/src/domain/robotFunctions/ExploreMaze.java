package domain.robotFunctions;

import java.util.ArrayList;

import domain.Position.Position;
import domain.maze.Wall;
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
	private ArrayList<Wall> wallList = new ArrayList<Wall>(); 
	
	public ExploreMaze(RobotPilot simRobotPilot){
		this.robot = simRobotPilot;
	}
	
	public RobotPilot getRobotPilot(){
		return this.robot;
	}
	public void start(){
		//TODO stopcondition needs to be determined.
		while(true){
			double[] distances = new double[3];
			distances = checkDistances();
			makeWall(distances);
			Direction direction = getNextDirection(distances);
			move(direction);
		}
	}
	
	private void makeWall(double[] distances) {
		double x = robot.getPosition().getX();
		double y = robot.getPosition().getY();
		double orientation = robot.getOrientation();
		if(distances[0] < valuedDistance){
			calculateWall(x,y,orientation,Direction.LEFT); 
		}
			
		if(distances[1] < valuedDistance){
			calculateWall(x,y,orientation,Direction.FORWARD);
		}
			
		if(distances[2] < valuedDistance){
			calculateWall(x,y,orientation,Direction.RIGHT);
		}
	}
	private void calculateWall(double x, double y, double orientation,Direction direction) {
		if(orientation > -20 && orientation < 20){
			switch (direction) {
			case FORWARD:
				addWall(x+20,y-20,x+20,y+20);
				break;
			case LEFT:
				addWall(x-20,y-20,x+20,y-20);
				break;
			case RIGHT:
				addWall(x-20,y+20,x+20,y+20);
				break;
			}
		}
		else if(orientation > -110 && orientation < -70){
			switch (direction) {
			case FORWARD:
				addWall(x-20,y-20,x+20,y-20);
				break;
			case LEFT:
				addWall(x-20,y-20,x-20,y+20);
				break;
			case RIGHT:
				addWall(x+20,y-20,x+20,y+20);
				break;
			}
		}
		else if(orientation > 160 && orientation < 200){
			switch (direction) {
			case FORWARD:
				addWall(x-20,y-20,x-20,y+20);
				break;
			case LEFT:
				addWall(x-20,y+20,x+20,y+20);
				break;
			case RIGHT:
				addWall(x-20,y-20,x+20,y-20);
				break;
			}
		}
		else if(orientation > 70 && orientation < 110){
			switch (direction) {
			case FORWARD:
				addWall(x-20,y+20,x+20,y+20);
				break;
			case LEFT:
				addWall(x+20,y-20,x+20,y+20);
				break;
			case RIGHT:
				addWall(x-20,y+20,x-20,y-20);
				break;
			}
		}

	}
	
	
	private void addWall(double x1, double y1, double x2, double y2){
		Position pos1 = new Position(x1, y1);
		Position pos2 = new Position(x2,y2);
		Wall wall = new Wall(pos1, pos2);
		getRobotPilot().addFoundWall(wall);
	}
	
	public ArrayList<Wall> getWalls(){
		return wallList;
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
				break;
			case BACKWARD:
				robot.turnRight();
				robot.turnRight();
				robotMove();
				break;
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
