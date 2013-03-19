package domain.maze.graph;

import gui.ContentPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import domain.maze.Orientation;
import domain.robots.RobotPilot;

public class MazeGraph {
	private ArrayList<TileNode> nodes;
	private Orientation currentRobotOrientation;
	private TileNode startNode;
	private TileNode currentNode;
	private int tileCounter;
	private MazePath shortestPath;
	
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
	
	/**
	 * Calculates the shortest path to the finish and makes the given RobotPilot drive there.
	 * @param robotPilot
	 */
	public void driveToFinish(RobotPilot robotPilot){
		shortestPath = findShortestPathToFinish();
		Iterator<TileNode> tileIt = shortestPath.iterator();
		if(tileIt.hasNext()){
			tileIt.next();
			while(tileIt.hasNext()){
				TileNode nextNode = tileIt.next();
				//System.out.println("NEXTNODE"+nextNode.getX()+" "+nextNode.getY());
				Orientation nextOrientation = null;
				for(Orientation o:Orientation.values()){
					if(getCurrentNode().getNodeAt(o)!=null && getCurrentNode().getNodeAt(o).equals(nextNode)){
						nextOrientation = o;
					}
				}
				turnToNextOrientation(robotPilot, getCurrentRobotOrientation(), nextOrientation);
				try {
					if(tileCounter%2==0){
						robotPilot.straighten();
						robotPilot.move(20);
					} else {
						double distance = robotPilot.readUltrasonicValue();
						if(distance!=255 &&(distance< 17 || distance%40 > 22)){
							robotPilot.straighten();
							robotPilot.move(20);
						}
						else{
							robotPilot.move(40);
						}
					}
					tileCounter++;
					this.move();
				} catch (Throwable e) {
					System.out.println("Could not move");
				}
			}
		} else {
			throw new NullPointerException("Finish has not yet been found or no path can be found.");
		}
		ContentPanel.writeToDebug("Finish (or starting point) reached!");
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
	 * Returns the orientation to move in to continue exploring. 
	 * This is accomplished by calculating the shortest path to the next unexplored node and 
	 * finding out in which direction the robot needs to turn in order to get there.
	 * If multiple paths of equal length exist the path will be chosen that makes it turn, in order of priority:
	 * Left, Not, Right, Back
	 * If a path can not be found, it returns the orientation to the front/back of the current orientation,
	 * depending on whether there's a wall in front of the robot.
	 * @return
	 */
	public Orientation getNextMoveOrientation(){
		ArrayList<TileNode> unexpanded = new ArrayList<TileNode>();
		for(TileNode node : nodes){
			if(!node.isFullyExpanded()){
				unexpanded.add(node);
			}
		}
		if(unexpanded.size()!=0){
			SortedPathSet possiblePaths = new SortedPathSet();
			for(TileNode node : unexpanded){
				possiblePaths.add(new MazePath(getCurrentNode(),node));
			}
			int shortestFound = Integer.MAX_VALUE;
			ArrayList<MazePath> candidates = new ArrayList<MazePath>();
			do{
				while(!possiblePaths.firstPathReachesGoal()){
					possiblePaths.expand();
				}
				while(possiblePaths.firstPathReachesGoal() && possiblePaths.size()>0){
					candidates.add(possiblePaths.first());
					possiblePaths.remove(possiblePaths.first());
					int currentLength = candidates.get(candidates.size()-1).getCurrentLength();
					shortestFound = currentLength<shortestFound?currentLength:shortestFound;
				}
				//In the while condition the path will only get added if it's valid but it'll always be removed from possiblePaths.
			}while( possiblePaths.size()>0 && possiblePaths.first().getCurrentLength()<=shortestFound);
			Iterator<MazePath> it = candidates.iterator();
			while(it.hasNext()){
				MazePath path = it.next();
				if(path.getCurrentLength()>shortestFound){
					it.remove();
				}
			}
			ArrayList<Orientation> orderedOrientations = new ArrayList<Orientation>();
			orderedOrientations.add(Orientation.WEST);
			orderedOrientations.add(Orientation.NORTH);
			orderedOrientations.add(Orientation.EAST);
			orderedOrientations.add(Orientation.SOUTH);
			for(Orientation o : orderedOrientations){
				for(MazePath path : candidates){
					if(path.contains(getCurrentNode().getNodeAt(getRelativeOrientation(getCurrentRobotOrientation(),o))))
						return o;
				}
			}
		}
		MazeNode frontNode = getCurrentNode().getNodeAt(getCurrentRobotOrientation());
		return frontNode!=null?(frontNode.getClass()!=WallNode.class?Orientation.NORTH:Orientation.SOUTH):null;
	}
	
	/**
	 * Make a new TileNode at the given orientation relative to the robot's current orientation.
	 * @param orientation
	 */
	public void generateTileNodeAt(Orientation orientation){
		Orientation absoluteOrientation = getRelativeOrientation(getCurrentRobotOrientation(),orientation);
		generateTileNodeAt(getCurrentNode(), absoluteOrientation);
	}
	
	private void generateTileNodeAt(TileNode tile, Orientation orientation){
		Orientation absoluteOrientation = orientation;
		Orientation orientationToCurrent = absoluteOrientation.getBack();
		TileNode newNode = new TileNode(tile,orientationToCurrent);
		boolean thisNodeAlreadyExists = false;
		for(TileNode node:nodes){
			if((node).getX()==newNode.getX() && (node).getY()==newNode.getY()){
				tile.setNodeAt(absoluteOrientation, node);
				node.setNodeAt(orientationToCurrent, tile);
				thisNodeAlreadyExists = true;
				break;
			}
			for(Orientation o: Orientation.values()){
				if(node.getX()==newNode.getX()+o.getXValue()&&node.getY()==newNode.getY()+o.getYValue()){
					MazeNode nodeAtOBack = node.getNodeAt(o.getBack());
					if(nodeAtOBack!=null && nodeAtOBack.getClass().equals(WallNode.class)){
						newNode.setNodeAt(o, new WallNode(node,o));

					}
				}
			}
		}
		if(!thisNodeAlreadyExists){
			tile.setNodeAt(absoluteOrientation, newNode);
			nodes.add(newNode);
		}
	}
	
	/**
	 * Make a new WallNode at the given orientation relative to the robot's current orientation.
	 * @param orientation
	 * @return true if the wall has been added, false if it's clear that there can be no wall at that location.
	 */
	public boolean generateWallNodeAt(Orientation orientation){
		Orientation absoluteOrientation = getRelativeOrientation(getCurrentRobotOrientation(),orientation);
		return generateWallNodeAt(getCurrentNode(),absoluteOrientation);
	}
	
	/**
	 * Make a new WallNode at the given orientation connected to the given TileNode.
	 * @param orientation
	 * @return true if the wall has been added, false if it's clear that there can be no wall at that location.
	 */
	private boolean generateWallNodeAt(TileNode tile, Orientation orientation){
		Orientation absoluteOrientation = orientation;
		Orientation orientationToCurrent = absoluteOrientation.getBack();
		WallNode newNode = new WallNode(tile,orientationToCurrent);
		
		//If this tile has a wall at a certain orientation it's neighbouring tile will have one in the opposite orientation
		//A new tile isn't generated there because then we would generate nodes outside of the maze's edge
		TileNode otherSide = null;
		for(TileNode node : nodes){
			if(node.getX()==tile.getX()+absoluteOrientation.getXValue() && node.getY()==tile.getY()+absoluteOrientation.getYValue()){
				otherSide = node;
				break;
			}
		}
		if(otherSide != null){
			otherSide.setNodeAt(orientationToCurrent, newNode);
		}
		
		//If a node already has a connection to a (semi-)completed node at a certain orientation we can be sure of the fact that there
		//is no wall there so we won't add it
		MazeNode previousNode = tile.getNodeAt(absoluteOrientation);
		if(previousNode!=null && previousNode.getClass().equals(TileNode.class) && (((TileNode) previousNode).isVisited() || ((TileNode)previousNode).numberOfConnections()>=2)){
			System.out.println("Wrong measurement. There can be no wall there.");
			return false;
		} else {
			tile.setNodeAt(absoluteOrientation, newNode);
			return true;
		}
	}
	
	/**
	 * 
	 * @return Whether the node the robot is currently on is fully expanded
	 */
	public boolean isCurrentNodeFullyExpanded(){
		return getCurrentNode().isFullyExpanded();
	}
	
	/**
	 * Use isNodeInFrontFullyExpanded() when applicable.
	 * Orientation is relative to the robot's current orientation, not the grid.
	 * @param orientation
	 * @return Whether the node at the given orientation with respect to the robot is fully expanded
	 */
	public boolean isNodeThereFullyExpanded(Orientation orientation){
		return getCurrentNode().getNodeAt(getRelativeOrientation(getCurrentRobotOrientation(),orientation)).isFullyExpanded();
	}
	
	/**
	 * Use isNodeInFrontFullyExpanded() when applicable.
	 * Orientation is relative to the grid! (So to the robot's orientation when it started exploring (NORTH))
	 * @param orientation
	 * @return Whether the node at the given orientation with respect to the robot's current position is fully expanded
	 */
	public boolean isNodeThereFullyExpandedRelativeToGrid(Orientation orientation){
		return getCurrentNode().getNodeAt(orientation).isFullyExpanded();
	}
	
	/**
	 * Returns if the node in front of the robot is fully expanded.
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

	/**
	 * Returns the robot's current orientation relative to it's starting orientation (NORTH)
	 * @return
	 */
	public Orientation getCurrentRobotOrientation() {
		return currentRobotOrientation;
	}

	
	private void setCurrentRobotOrientation(Orientation orientation) {
		this.currentRobotOrientation = orientation;
	}
	
	/**
	 * Sets the current TileNode as the Finish node.
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
	
	/**
	 * Sets the current TileNode as a Checkpoint node.
	 */
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
			ContentPanel.writeToDebug("No checkpoint found, driving straight to finish.");
			checkpoint = getCurrentNode();
		}
		TileNode finishNode = getFinishNode();
		if(finishNode == null){
			ContentPanel.writeToDebug("No finish barcode found, driving to starting node instead.");
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
	 * Returns the MazePath representation of the shortest path from startNode to finishNode.
	 * @param finishNode
	 * @return
	 */
	public MazePath findShortestPathFromTo(TileNode startNode, TileNode finishNode) {
		SortedPathSet searchSet = new SortedPathSet(new MazePath(startNode,finishNode));
		int e = 0;
		while(!searchSet.isEmpty() && !searchSet.firstPathReachesGoal()){
			searchSet.expand();
			//System.out.println("Expansion number "+e+" current queue: "+(searchSet.isEmpty()?"none":searchSet.toString()));
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
	
	/**
	 * Returns the TileNode the robot's currently on.
	 * @return
	 */
	public TileNode getCurrentNode(){
		return currentNode;
	}

	/**
	 * Assume the current coordinates are the given x and y and that the robot is in Orientation o.
	 * @param x
	 * @param y
	 * @param o
	 */
	public void continueExploring(int x, int y, Orientation o) {
		if(o!=null){
			for(TileNode node : nodes){
				if(node.getX()==x && node.getY()==y){
					setCurrentNode(node);
				}
			}
			setCurrentRobotOrientation(o);
			ContentPanel.writeToDebug("Starting exploration from "+"("+x+","+y+")"+" facing "+o);
		} else {
			ContentPanel.writeToDebug("Continuing exploration.");
		}
		
	}

	/**
	 * Turn back, move a tile and make a wallNode where the robot bumped.
	 */
	public void recoverFromBump() {
		turnBack();
		move();
		generateWallNodeAt(Orientation.SOUTH);
	}
	
	/**
	 * Return the latest calculated path to the finish.
	 * @return
	 */
	public MazePath getShortestPath(){
		return shortestPath;
	}

	/**
	 * Sets the tile in front of the robot to a dead end, meaning that walls are added in the North, West and East orientations.
	 * As with the generateWallNodeAt method, if we know that there can't be any walls there they will not be added.
	 * A robot will never explore a known dead end.
	 */
	public void setNextTileToDeadEnd() {
		if(getCurrentNode().getNodeAt(getCurrentRobotOrientation())==null){
			generateTileNodeAt(Orientation.NORTH); //This is a relative orientation.
		}
		if(getCurrentNode().getNodeAt(getCurrentRobotOrientation())!=null && getCurrentNode().getNodeAt(getCurrentRobotOrientation()).getClass().equals(TileNode.class)){
			TileNode deadEndNode = (TileNode)getCurrentNode().getNodeAt(getCurrentRobotOrientation());
			generateWallNodeAt(deadEndNode, getRelativeOrientation(getCurrentRobotOrientation(),Orientation.WEST));
			generateWallNodeAt(deadEndNode, getRelativeOrientation(getCurrentRobotOrientation(),Orientation.NORTH));
			generateWallNodeAt(deadEndNode, getRelativeOrientation(getCurrentRobotOrientation(),Orientation.EAST));
		} else {
			ContentPanel.writeToDebug("Couldn't create a dead end at the position in front of the robot.");
		}
		
	}
}