package domain.robotFunctions;

import java.util.ArrayList;

import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.Wall;
import domain.maze.graph.MazeGraph;
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
	private MazeGraph maze = new MazeGraph();
	private boolean backWall = false;
	
	public ExploreMaze(RobotPilot simRobotPilot){
		this.robot = simRobotPilot;
	}
	
	public RobotPilot getRobotPilot(){
		return this.robot;
	}
	
	public void start(){
		double[] dummy = new double[3];
		dummy = checkDistances();
		robot.turnLeft();
		robot.turnLeft();
		double backValue = robot.readUltrasonicValue();
		if(backValue < valuedDistance){
			maze.generateWallNodeAt(Orientation.SOUTH);
			double x = robot.getPosition().getX();
			double y = robot.getPosition().getY();
			System.out.println(robot.getOrientation());
			calculateWall(x, y, robot.getOrientation(), Direction.FORWARD);
		}else{
			maze.generateTileNodeAt(Orientation.SOUTH);
		}
		robot.turnLeft();
		robot.turnLeft();
		int count = 1;
		while(!maze.isComplete()){
			double[] distances = new double[3];
			distances = checkDistances();
			makeWall(distances);
			Direction direction = getNextDirection(distances);
			if(checkStraigthen(distances)){
				moveWithStraighten(direction);
			}
			else{
				move(direction);
			}
				
			count++;
			
		}
		maze.driveToFinish(robot);
	}
	private boolean checkStraigthen(double[] distances){
		for (int i = 0; i < distances.length; i++) {
			if(distances[i] < 14)
				return true;
		}
		return false;
	}
	
	private void moveWithStraighten(Direction direction) {
		switch (direction) {
		case LEFT:
			robot.turnLeft();
			maze.turnLeft();
			break;
		case RIGHT:
			robot.turnRight();
			maze.turnRight();
			break;
		case BACKWARD:
			robot.turnRight();
			robot.turnRight();
			maze.turnBack();
			break;
		}
		robot.findWhiteLine();
		robot.straighten();
		robotMoveStraighten();
		
	}

	private void robotMoveStraighten() {
		try {
			robot.move(distanceBlocks / 2);
			maze.move();
		} catch (CannotMoveException e) {
			//Normally never gets called.
			e.printStackTrace();
		}
	}

	private void makeWall(double[] distances) {
		double x = robot.getPosition().getX();
		double y = robot.getPosition().getY();
		double orientation = robot.getOrientation();
		if(distances[0] < valuedDistance){
			calculateWall(x,y,orientation,Direction.LEFT);
			maze.generateWallNodeAt(Orientation.WEST);
		} else {
			maze.generateTileNodeAt(Orientation.WEST);
		}
			
		if(distances[1] < valuedDistance){
			calculateWall(x,y,orientation,Direction.FORWARD);
			maze.generateWallNodeAt(Orientation.NORTH);
		} else {
			maze.generateTileNodeAt(Orientation.NORTH);
		}
			
		if(distances[2] < valuedDistance){
			calculateWall(x,y,orientation,Direction.RIGHT);
			maze.generateWallNodeAt(Orientation.EAST);
		} else {
			maze.generateTileNodeAt(Orientation.EAST);
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
		else if(orientation > 160 || orientation < -160){
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
		Position pos1 = new Position(x1,y1);
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
				maze.turnLeft();
				break;
			case RIGHT:
				robot.turnRight();
				maze.turnRight();
				break;
			case BACKWARD:
				robot.turnRight();
				robot.turnRight();
				maze.turnBack();
				break;
				
			}
		robotMove();
	}
	
	private void robotMove(){
		try {
			robot.move(distanceBlocks);
			maze.move();
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
