package domain.robotFunctions;

import gui.ContentPanel;

import java.util.ArrayList;

import peno.htttp.Tile;


import controller.Controller;

import domain.Position.Pose;
import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.Wall;
import domain.maze.graph.MazeGraph;
import domain.maze.graph.MazePath;
import domain.maze.graph.SeesawNode;
import domain.maze.graph.TileNode;
import domain.robots.CannotMoveException;
import domain.robots.RobotPilot;

public class ExploreMaze{
	
	private enum Direction {
	    LEFT(-1),FORWARD(0),RIGHT(1),BACKWARD(-2);
	    private int offset;
	    private Direction(int offset){
	    	this.offset = offset;
	    }
	    public int getOffset(){
	    	return this.offset;
	    }
	}
	private RobotPilot robot;
	private final int sideValuedDistance = 32;
	private final int frontValuedDistance = 27;

	private final int distanceBlocks = 40;
	private final int MAZECONSTANT = 40;
	private ArrayList<Wall> wallList = new ArrayList<Wall>();
	private MazeGraph maze = new MazeGraph();
	private boolean backWall = false;
	private boolean interrupted = false;
	
	private boolean atDeadEnd = false;
	
	public ExploreMaze(RobotPilot simRobotPilot){
		this.robot = simRobotPilot;
	}
	
	public RobotPilot getRobotPilot(){
		return this.robot;
	}
	
	public void start(){
		initialSetup();
		continueExploring(0,0,maze.getInitialOrientation());
	}
	
	private void initialSetup(){
		ContentPanel.writeToDebug("Exploration started");
		double[] dummy = new double[3];
		dummy = checkDistances();
		robot.turnLeft();
		robot.turnSensorLeft();
		double backValue = robot.readUltrasonicValue();
		if(backValue < sideValuedDistance){
			maze.generateWallNodeAt(Orientation.SOUTH); //Relative!
			double x = robot.getPosition().getX();
			double y = robot.getPosition().getY();
			calculateWall(x, y, robot.getOrientation(), Direction.LEFT);
		}else{
			maze.generateTileNodeAt(Orientation.SOUTH); //Relative!
		}
		robot.turnSensorForward();
		robot.turnRight();
	}
	
	public void resumeExplore(int x, int y, Orientation o){
		interrupted = false;
		continueExploring(0,0,o);
	}
	
	private void continueExploring(int x, int y, Orientation o){
		maze.continueExploring(x,y,o);
		while(!maze.isComplete() && !Controller.isStopped() && !interrupted){
			double[] distances = new double[3];
			distances = checkDistances();
			makeWall(distances);
			if(!maze.isComplete()){
				maze.decreaseAllBlockNavigationCounts();
				//robot.setMovingSpeed(robot.getDefaultMovingSpeed());
				checkForOtherRobots();
				Direction direction = getNextDirection(distances);
				updatePosition(direction);
				if(direction == null){
					stopExploring();	
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// Auto-generated catch block
						e.printStackTrace();
					}
					resumeExplore(0, 0, null);
					break;
				}
				else if(checkBadPosition(distances)){
					if(!closeSideWalls(distances)||direction.getOffset()==Direction.LEFT.getOffset()||direction.getOffset()==Direction.RIGHT.getOffset()){
						moveWithStraighten(direction);
					}else{
					adjustRotation(distances, direction);
					if(checkVeryBadPosition(distances)){
						moveWithStraighten(direction);
					}
					else{
						move(direction);
						robot.snapPoseToTileMid();
					}
					}
				}
				else{
					move(direction);
					robot.snapPoseToTileMid();
				}
				//System.out.println("Now at "+maze.getCurrentNode().getX()+" "+maze.getCurrentNode().getY());
			}
		}
		if(Controller.isStopped()==false){
			ContentPanel.writeToDebug("Maze completed.");
			robot.setDriveToFinishSpeed();
			maze.driveToFinish(robot);
		}
	}

	private boolean closeSideWalls(double[] distances) {
		double leftDistance=distances[0];
		double rightDistance=distances[2];
		return leftDistance<MAZECONSTANT && rightDistance<MAZECONSTANT;
	}

	private void adjustRotation(double[] distances,Direction direction) {
		
		double leftDistance=distances[0];
		double rightDistance=distances[2];
		double tooMuchLeft=-(rightDistance%MAZECONSTANT-MAZECONSTANT/2);
		if(Math.abs(leftDistance)<Math.abs(rightDistance)){
			tooMuchLeft=leftDistance%MAZECONSTANT-MAZECONSTANT/2;
		}
		
		double rotation=2*(Math.min(5, (180/Math.PI)*Math.abs(Math.atan(tooMuchLeft/MAZECONSTANT/2))) * -Math.signum(tooMuchLeft));
		switch (direction) {
		case BACKWARD:
			rotation=-rotation;
			break;
		default:
			break;
		}
		robot.turn(rotation);
	}

	public void checkForOtherRobots(){
		if(Controller.interconnected){
		for(Orientation o : Orientation.values()){
			TileNode node = (TileNode) maze.getCurrentTile();
			while(node.getNodeAt(o)!=null && TileNode.class.isAssignableFrom(node.getNodeAt(o).getClass())){
				node = (TileNode) node.getNodeAt(o);
				Pose relativePose = new Pose(node.getX()*40.0,node.getY()*40.0,maze.getCurrentRobotOrientation());
				Position pos = Position.getAbsolutePose(robot.getInitialPosition(), relativePose);	
				node.setAccessible(!robot.checkRobotSensor(pos));
			}
		}
		}
	}
	
	public void updatePosition(Direction direction){
		if(direction==null){
			robot.updatePosition(maze.getCurrentTile().getX(), maze.getCurrentTile().getY(), maze.getCurrentRobotOrientation().getAngleToHorizontal());
			return;
		}
		Orientation nextOrientation = maze.getCurrentRobotOrientation().getOffset(direction.getOffset());
		TileNode nextNode = (TileNode) maze.getCurrentTile().getNodeAt(nextOrientation);
		robot.updatePosition(nextNode.getX(), nextNode.getY(), nextOrientation.getAngleToHorizontal());

	}
	private boolean nextTileIsSeesaw(){
		return maze.nextTileIsSeesaw();
	}
	

	
	private boolean checkBadPosition(double[] distances){
		for (int i = 0; i < distances.length; i++) {
			if(distances[i]!=255 &&(distances[i]%MAZECONSTANT < 17 || distances[i]%MAZECONSTANT > 23)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkVeryBadPosition(double[] distances){
		double leftDistance=distances[0];
		double rightDistance=distances[2];
		double frontDistance=distances[1];
		double tooMuchLeft=leftDistance%MAZECONSTANT-rightDistance%MAZECONSTANT;
		return Math.abs(tooMuchLeft)>6||Math.abs(frontDistance%MAZECONSTANT-MAZECONSTANT/2)>4;
	}
	
	/**
	 * Set the current tile to the finish tile.
	 * A robot will drive to the finish after it had finished exploring the maze.
	 */
	public void setCurrentTileToFinish(){
		maze.setCurrentTileToFinish();
	}
	
	/**
	 * Makes the current tile a checkpoint tile.
	 * A robot will drive past the checkpoint(s) on its way to the finish.
	 */
	public void setCurrentTileToCheckpoint(){
		maze.setCurrentTileToCheckpoint();
	}
	
	/**
	 * Sets the tile in front of the robot to a dead end, meaning that walls are added around it left, forward and right.
	 * These walls are added in the graph and in the GUI.
	 */
	public void setNextTileToDeadEnd(){
		setNextTileToDeadEnd(true);
	}
	
	public void setNextTileToDeadEnd(boolean accessible){
		this.atDeadEnd = true;
		maze.setNextTileToDeadEnd(accessible);
		double orientation = robot.getOrientation();
		double deadEndX = robot.getPosition().getX()+Math.cos(orientation/180*Math.PI)*MAZECONSTANT;
		double deadEndY = robot.getPosition().getY()+Math.sin(orientation/180*Math.PI)*MAZECONSTANT;
		System.out.println("Current: ("+robot.getPosition().getX()+","+robot.getPosition().getY()+"), ori: "+orientation);
		System.out.println("Dead End: ("+deadEndX+","+deadEndY+")");
		calculateWall(deadEndX, deadEndY, orientation, Direction.LEFT);
		calculateWall(deadEndX, deadEndY, orientation, Direction.FORWARD);
		calculateWall(deadEndX, deadEndY, orientation, Direction.RIGHT);
	}
	
	public void setNextTileToSeesaw(boolean isUpAtThisSide){
		maze.setNextTileToSeesaw(isUpAtThisSide);
		double orientation = robot.getOrientation();
		double[] x = new double[2];
		double[] y = new double[2];
		x[0] = robot.getPosition().getNewRoundPosition(orientation, MAZECONSTANT).getX();
		y[0] = robot.getPosition().getNewRoundPosition(orientation, MAZECONSTANT).getY();
		x[1] = robot.getPosition().getNewRoundPosition(orientation, MAZECONSTANT*2).getX();
		y[1] = robot.getPosition().getNewRoundPosition(orientation, MAZECONSTANT*2).getY();
		for(int i=0; i==1; i++){
			for(Direction d : Direction.values()){
				if(d.equals(Direction.LEFT)||d.equals(Direction.RIGHT)){
					calculateWall(x[i], y[i], orientation, d);
				}
			}
		}
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
		if(distances[0] < sideValuedDistance){
			if(maze.generateWallNodeAt(Orientation.WEST)){
				calculateWall(x,y,orientation,Direction.LEFT);
			}
		} else {
			maze.generateTileNodeAt(Orientation.WEST);
		}
			
		if(distances[1] < frontValuedDistance){
			if(maze.generateWallNodeAt(Orientation.NORTH)){
				calculateWall(x,y,orientation,Direction.FORWARD);
			}
		} else {
			maze.generateTileNodeAt(Orientation.NORTH);
		}
			
		if(distances[2] < sideValuedDistance){
			if(maze.generateWallNodeAt(Orientation.EAST)){
				calculateWall(x,y,orientation,Direction.RIGHT);
			}			
		} else {
			maze.generateTileNodeAt(Orientation.EAST);
		}
	}
	private void calculateWall(double x, double y, double orientation,Direction direction) {
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
			try {
				robot.move(-distanceBlocks/2);
				System.out.println("Bumped, recovering.");
			} catch (CannotMoveException e1) {}
			maze.recoverFromBump();
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
		else if(next == null){
			return null;
		}
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
	
	public MazePath getPathToFinish(){
		return maze.getShortestPath();
	}

	public void driveOverSeesaw() {
		if(maze.getCurrentTile().getNodeAt(maze.getCurrentRobotOrientation()).isAccessible()){
			maze.move();
			maze.move();
			maze.move();
			((SeesawNode)maze.getCurrentTile().getNodeAt(maze.getCurrentRobotOrientation().getBack())).setUp(false);
			System.out.println("The node after the seesaw is "+maze.getCurrentTile());
		}
	}

	public ArrayList<domain.maze.graph.TileNode> getFoundTilesList() {
		return maze.getFoundTilesList();
	}
	
	public void atBarcode(int barcodeNumber){
		maze.generateWallNodeAt(Orientation.EAST);
		maze.generateWallNodeAt(Orientation.WEST);
		calculateWall(robot.getPosition().getX(), robot.getPosition().getY(), robot.getOrientation(), Direction.RIGHT);
		calculateWall(robot.getPosition().getX(), robot.getPosition().getY(), robot.getOrientation(), Direction.LEFT);
		maze.setCurrentTileBarcode(barcodeNumber);
	}


	public void updateWithMap(String[][] resultMap) {
		maze.updateWithMap(resultMap);		
	}

	public void setPartnerPosition(int partnerX, int partnerY) {
		maze.setPartnerPosition(partnerX, partnerY);
	}

	public Position findMostNegativePosition() {
		int minX = 0;
		int minY = 0;
		for(TileNode t : getFoundTilesList()){
			if(t.getX()<minX){
				minX=t.getX();
			}
			if(t.getY()<minY){
				minY=t.getY();
			}
		}
		return new Position(minX,minY);
	}
}
