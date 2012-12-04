package domain.robotFunctions;

import gui.ContentPanel;

import java.util.ArrayList;

import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.Wall;
import domain.maze.graph.MazeGraph;
import domain.robots.CannotMoveException;
import domain.robots.Robot;
import domain.robots.RobotPilot;
import domain.robots.SimRobotPilot;

public class ExploreMaze extends Thread {
	
	private enum Direction {
	    LEFT,FORWARD,RIGHT,BACKWARD
	}
	private RobotPilot robot;
	private final int valuedDistance = 25;
	private final int distanceBlocks = 40;
	private ArrayList<Wall> wallList = new ArrayList<Wall>();
	private MazeGraph maze = new MazeGraph();
	private boolean backWall = false;
	private boolean interrupted = false;
	
	public ExploreMaze(RobotPilot simRobotPilot){
		this.robot = simRobotPilot;
	}
	
	public RobotPilot getRobotPilot(){
		return this.robot;
	}
	
	public void start(){
		initialSetup();
		continueExploring(0,0,Orientation.NORTH);
	}
	
	private void initialSetup(){
		ContentPanel.writeToDebug("Exploration started");
		double[] dummy = new double[3];
		dummy = checkDistances();
		robot.turnLeft();
		robot.turnSensorLeft();
		double backValue = robot.readUltrasonicValue();
		if(backValue < valuedDistance){
			maze.generateWallNodeAt(Orientation.SOUTH);
			double x = robot.getPosition().getX();
			double y = robot.getPosition().getY();
			calculateWall(x, y, robot.getOrientation(), Direction.LEFT);
		}else{
			maze.generateTileNodeAt(Orientation.SOUTH);
		}
		robot.turnSensorForward();
		robot.turnRight();
	}
	
	public void resumeExplore(int x, int y, Orientation o){
		interrupted = false;
		continueExploring(0,0,null);
	}
	
	private void continueExploring(int x, int y, Orientation o){
		maze.continueExploring(x,y,o);
		while(!maze.isComplete() && interrupted==false){
			double[] distances = new double[3];
			distances = checkDistances();
			makeWall(distances);
			if(!maze.isComplete()){
				Direction direction = getNextDirection(distances);
				if(checkStraigthen(distances)){
					moveWithStraighten(direction);
				}
				else{
					move(direction);
					System.out.println("Now at "+maze.getCurrentNode().getX()+" "+maze.getCurrentNode().getY());
				}
			}
		}
		if(!interrupted){
			ContentPanel.writeToDebug("Maze completed.");
			robot.setMovingSpeed(robot.getDefaultMovingSpeed()*2);
			maze.driveToFinish(robot);
		}
	}
	
	private boolean checkStraigthen(double[] distances){
		for (int i = 0; i < distances.length; i++) {
			if(distances[i] < 16)
				return true;
		}
		return false;
	}
	
	public void setCurrentTileToFinish(){
		maze.setCurrentTileToFinish();
	}
	
	public void setCurrentTileToCheckpoint(){
		maze.setCurrentTileToCheckpoint();
	}
	
	private void moveWithStraighten(Direction direction) {
		switch (direction) {
		case LEFT:
			maze.turnLeft();
			robot.turnLeft();
			break;
		case RIGHT:
			maze.turnRight();
			robot.turnRight();
			break;
		case BACKWARD:
			maze.turnBack();
			robot.turnRight();
			robot.turnRight();
			break;
		}
		robot.straighten();
		robotMoveStraighten();
		
	}

	private void robotMoveStraighten() {
		try {
			maze.move();
			robot.move(distanceBlocks / 2);
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
			if(maze.generateWallNodeAt(Orientation.WEST)){
				calculateWall(x,y,orientation,Direction.LEFT);
			}
		} else {
			maze.generateTileNodeAt(Orientation.WEST);
		}
			
		if(distances[1] < valuedDistance){
			if(maze.generateWallNodeAt(Orientation.NORTH)){
				calculateWall(x,y,orientation,Direction.FORWARD);
			}
		} else {
			maze.generateTileNodeAt(Orientation.NORTH);
		}
			
		if(distances[2] < valuedDistance){
			if(maze.generateWallNodeAt(Orientation.EAST)){
				calculateWall(x,y,orientation,Direction.RIGHT);
			}			
		} else {
			maze.generateTileNodeAt(Orientation.EAST);
		}
	}
	private void calculateWall(double x, double y, double orientation,Direction direction) {
		int MAZECONSTANT = 40;
		x = (int) (Math.floor((x)/MAZECONSTANT))*MAZECONSTANT+20;
		y = (int) (Math.floor((y)/MAZECONSTANT))*MAZECONSTANT+20;
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
				maze.turnLeft();
				robot.turnLeft();
				break;
			case RIGHT:
				maze.turnRight();
				robot.turnRight();
				break;
			case BACKWARD:
				maze.turnBack();
				robot.turnRight();
				robot.turnRight();
				break;
				
			}
		robotMove();
	}
	
	private void robotMove(){
		try {
			maze.move();
			robot.move(distanceBlocks);
		} catch (CannotMoveException e) {
			//Normally never gets called.
			e.printStackTrace();
		}
	}
	private Direction getNextDirection(double[] distances){
		Orientation next = maze.getNextMoveOrientation();
		if(next == Orientation.WEST)
			return Direction.LEFT;
		else if(next == Orientation.NORTH)
			return Direction.FORWARD;
		else if(next == Orientation.EAST)
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

	public void driveToFinish() {
		interrupted = true;
		maze.driveToFinish(robot);
	}
	
	public synchronized void stopExploring(){
		interrupted = true;
	}
}
