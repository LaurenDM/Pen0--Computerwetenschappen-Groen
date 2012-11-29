package domain.maze.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import domain.maze.Orientation;
import domain.robots.CannotMoveException;
import domain.robots.Robot;
import domain.robots.RobotPilot;

public class MazeGraph {
	private ArrayList<TileNode> nodes;
	private Orientation currentRobotOrientation;
	private TileNode startNode;
	private TileNode currentNode;
	
	/**
	 * A new MazeGraph is initialized with a starting node that represents the robot's current position.
	 * The current orientation of the robot is assumed to be North. (It doesn't matter what it's actual orientation
	 * is because everything is handled relative to the starting orientation)
	 */
	public MazeGraph(){
		nodes = new ArrayList<TileNode>();
		startNode = new TileNode(null,null);
		setCurrentNode(startNode);
		nodes.add(startNode);
		setCurrentRobotOrientation(Orientation.NORTH);
		System.out.println("");
	}
	
	public void driveToFinish(RobotPilot robotPilot){
		MazePath shortestPath = findShortestPathToFinish();
		Iterator<TileNode> tileIt = shortestPath.iterator();
		if(tileIt.hasNext()){
			tileIt.next();
			while(tileIt.hasNext()){
				TileNode nextNode = tileIt.next();
				System.out.println(nextNode);
				Orientation nextOrientation = null;
				for(Orientation o:Orientation.values()){
					if(getCurrentNode().getNodeAt(o).equals(nextNode)){
						nextOrientation = o;
					}
				}
				System.out.println("Turning towards the " + nextOrientation.toString().toLowerCase());
				turnToNextOrientation(robotPilot, getCurrentRobotOrientation(), nextOrientation);
				try {
					robotPilot.move(40);
					this.move();
					System.out.println(getCurrentNode());
				} catch (Throwable e) {
					System.out.println("Could not move");
				}
			}
		} else {
			throw new NullPointerException("Finish has not yet been found or no path can be found.");
		}
		System.out.println("Finish (or starting point) reached!");
	}
	
	private void turnToNextOrientation(RobotPilot robotPilot, Orientation currentOrientation, Orientation nextOrientation) {
		if(currentOrientation.equals(nextOrientation)){
			//Don't turn
		} else if(nextOrientation.equals(currentOrientation.getLeft())){
			robotPilot.turnLeft();
			this.turnLeft();
		} else if(nextOrientation.equals(currentOrientation.getRight())){
			robotPilot.turnRight();
			this.turnRight();
		} else if(nextOrientation.equals(currentOrientation.getBack())){
			robotPilot.turnLeft();
			robotPilot.turnLeft();
			this.turnBack();
		} else {
			throw new IllegalArgumentException("Couldn't find a direction to turn in!");
		}
	}

	/**
	 * If a MazeGraph has been completed that means that all it's nodes have been fully expanded.
	 * A fully expanded node has the maximum amount of connections for that type of node to other valid nodes.
	 * @return Whether this is a completed MazeGraph
	 */
	public boolean isComplete(){
		boolean isComplete = true;
		for(TileNode tileNode:nodes){
			if(!tileNode.isFullyExpanded()) { isComplete = false;}
		}
		System.out.print(isComplete?"Maze completed. ":"");
		return isComplete;
	}
	
	/**
	 * @return Whether the starting node has been fully expanded.
	 */
	public boolean isStartNodeComplete(){
		return startNode.isFullyExpanded();
	}
	
	/**
	 * Move the robot forward in the direction it's currently facing.
	 * Throws RuntimeException if the node in that direction is a WallNode or if there isn't any node in that direction.
	 */
	public void move(){
		MazeNode nextNode = getCurrentNode().getNodeAt(getCurrentRobotOrientation());
		if(nextNode != null && nextNode.getClass().equals(TileNode.class)){
			setCurrentNode((TileNode) nextNode);
		} else {
			throw new RuntimeException("There is no node there or it's a WallNode.");
		}
	}
	
	/**
	 * Make a new TileNode at the given orientation relative to the robot's current orientation.
	 * @param orientation
	 */
	public void generateTileNodeAt(Orientation orientation){
		Orientation absoluteOrientation = getRelativeOrientation(getCurrentRobotOrientation(),orientation);
		Orientation orientationToCurrent = absoluteOrientation.getBack();
		TileNode newNode = new TileNode(getCurrentNode(),orientationToCurrent);
		boolean thisNodeAlreadyExists = false;
		for(TileNode node:nodes){
			if(((TileNode) node).getX()==newNode.getX() && ((TileNode)node).getY()==newNode.getY()){
				getCurrentNode().setNodeAt(absoluteOrientation, node);
				node.setNodeAt(orientationToCurrent, getCurrentNode());
				thisNodeAlreadyExists = true;
				break;
			}
		}
		if(!thisNodeAlreadyExists){
			getCurrentNode().setNodeAt(absoluteOrientation, newNode);
			nodes.add(newNode);
		}
	}
	
	/**
	 * Make a new WallNode at the given orientation relative to the robot's current orientation.
	 * @param orientation
	 */
	public void generateWallNodeAt(Orientation orientation){
		Orientation absoluteOrientation = getRelativeOrientation(getCurrentRobotOrientation(),orientation);
		Orientation orientationToCurrent = absoluteOrientation.getBack();
		WallNode newNode = new WallNode(getCurrentNode(),orientationToCurrent);
		getCurrentNode().setNodeAt(absoluteOrientation, newNode);
	}
	
	/**
	 * 
	 * @return Whether the node the robot is currently on is fully expanded
	 */
	public boolean isCurrentNodeFullyExpanded(){
		return getCurrentNode().isFullyExpanded();
	}
	
	/**
	 * Orientation is relative to the robot's current orientation, not the grid.
	 * @param orientation
	 * @return Whether the node at the given orientation with respect to the robot is fully expanded
	 */
	public boolean isNodeThereFullyExpanded(Orientation orientation){
		return getCurrentNode().getNodeAt(getRelativeOrientation(getCurrentRobotOrientation(),orientation)).isFullyExpanded();
	}
	
	/**
	 * Orientation is relative to the grid!
	 * @param orientation
	 * @return Whether the node at the given orientation with respect to the robot's current position is fully expanded
	 */
	public boolean isNodeThereFullyExpandedRelativeToGrid(Orientation orientation){
		return getCurrentNode().getNodeAt(orientation).isFullyExpanded();
	}
	
	/**
	 * A conceptually easier way of dealing with the isFullyExpanded methods than the isNodeThereFully...
	 * @return Whether the node in front of the robot is fully expanded
	 */
	public boolean isNodeInFrontFullyExpanded(){
		return isNodeThereFullyExpanded(Orientation.NORTH);
	}
	
	/**
	 * Call when the robot turns to the left.
	 */
	public void turnLeft(){
		setCurrentRobotOrientation(getCurrentRobotOrientation().getLeft());
	}
	
	/**
	 * Call when the robot turns to the right.
	 */
	public void turnRight(){
		setCurrentRobotOrientation(getCurrentRobotOrientation().getRight());
	}
	
	/**
	 * Call when the robot turns 180 degrees.
	 */
	public void turnBack(){
		setCurrentRobotOrientation(getCurrentRobotOrientation().getBack());
	}

	public Orientation getCurrentRobotOrientation() {
		return currentRobotOrientation;
	}

	private void setCurrentRobotOrientation(Orientation orientation) {
		this.currentRobotOrientation = orientation;
	}
	
	/**
	 * Sets the current Node as the Finish node.
	 */
	public void setCurrentTileToFinish(){
		boolean finishAlreadyExists=false;
		for(TileNode node:nodes){
			if(node.isFinish() && !node.equals(getCurrentNode())){
				System.out.println("The finish node already exists! Cannot have 2 finish nodes!");
				finishAlreadyExists=true;
			}
		}
		if(!finishAlreadyExists){
			getCurrentNode().setFinish();
		}
	}
	
	public void setCurrentTileToCheckpoint(){
		getCurrentNode().setCheckpoint(true);
	}
	
	/**
	 * Implements the A* shortest path algorithm for this MazeGraph to find the shortest path to the Finish node, which is marked
	 * by the "Finish" barcode in the real maze.
	 * @return Null if the finish hasn't been found yet.
	 */
	public MazePath findShortestPathToFinish(){
		ArrayList<TileNode> checkpoints = getCheckpoints();
		//TODO find a way of finding a path through multiple checkpoints
		TileNode checkpoint = checkpoints.size()!=0?checkpoints.get(0):null;
		if(checkpoint == null){
			System.out.println("No checkpoint found, driving straight to finish.");
			checkpoint = getCurrentNode();
		}
		TileNode finishNode = getFinishNode();
		if(finishNode == null){
			System.out.println("No finish barcode found, driving to starting node instead.");
			finishNode = startNode;
		}
		return findShortestPathFromTo(getCurrentNode(),checkpoint).append(findShortestPathFromTo(checkpoint,finishNode));
	}
	
	public ArrayList<TileNode> getCheckpoints(){
		ArrayList<TileNode> checkpoints = new ArrayList<TileNode>();
		for(TileNode node: nodes){
			if(node.isCheckpoint()) checkpoints.add(node);
		}
		return checkpoints;
	}

	/**
	 * @param finishNode
	 * @return
	 */
	public MazePath findShortestPathFromTo(TileNode startNode, TileNode finishNode) {
		SortedPathSet searchSet = new SortedPathSet(new MazePath(startNode,finishNode));
		int e = 0;
		while(!searchSet.isEmpty() && !searchSet.firstPathReachesGoal()){
			searchSet.expand();
			System.out.println("Expansion number "+e+" current queue: "+(searchSet.isEmpty()?"none":searchSet.toString()));
			e++;
		}
		if(searchSet.isEmpty()){
			System.out.println("No path found...");
			return new MazePath(getCurrentNode(),finishNode);
		} else {
			System.out.println(searchSet.first());
			return searchSet.first();
		}
	}
	
	private TileNode getFinishNode() {
		for(TileNode node: nodes){
			if(node.isFinish()) return node;
		}
		return null;
	}

	private Orientation getRelativeOrientation(Orientation original, Orientation relative){
		if(relative == null){
			System.out.println("Got relative orientation null!");
			return null;
		}
		switch(relative){
		case NORTH: return original;
		case EAST: return original.getRight();
		case SOUTH: return original.getBack();
		case WEST: return original.getLeft();
		default: return null;
		}
	}

	private boolean isCurrentNode(TileNode node){
		//Not .equals(), because it needs to be the same object
		return node==currentNode;
	}

	private void setCurrentNode(TileNode currentNode){
		this.currentNode = currentNode;
	}
	
	private TileNode getCurrentNode(){
		return currentNode;
	}
}