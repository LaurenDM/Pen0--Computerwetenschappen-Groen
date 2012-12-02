package domain.maze.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import domain.maze.Orientation;

public class MazePath implements Comparable<MazePath>, Iterable<TileNode> {
	
	ArrayList<TileNode> nodeList = new ArrayList<TileNode>();
	TileNode goal;

	public MazePath(TileNode startingTile, TileNode goalTile){
		nodeList.add(startingTile);
		goal = goalTile;
	}
	
	public MazePath(MazePath pathToCopy){
		for(TileNode node: pathToCopy){
			nodeList.add(node);
		}
		goal = pathToCopy.getGoalTile();
	}
	
	public MazePath(MazePath pathToExtend, TileNode extension){
		this(pathToExtend);
		nodeList.add(extension);
	}
	
	public int getFScore(){
		return getCurrentLength()+getHeuristic();
	}
	
	public int getCurrentLength(){
		return nodeList.size()-1;
	}
	
	public int getHeuristic(){
		return getCurrentEndTile().manhattanDistanceTo(getGoalTile());
	}
	
	public TileNode getGoalTile(){
		return goal;
	}
	
	public TileNode getCurrentEndTile(){
		return nodeList.get(nodeList.size()-1);
	}
	
	public TileNode getStartingTile(){
		return nodeList.get(0);
	}
	
	public boolean contains(MazeNode node){
		return node.getClass().equals(TileNode.class)?nodeList.contains(node):false;
	}
	
	@Override
	public int compareTo(MazePath otherPath) {
		return getFScore()<otherPath.getFScore()?-1:(getFScore()>otherPath.getFScore()?1:0);
	}
	
	@Override
	public Iterator<TileNode> iterator() {
		return nodeList.iterator();
	}

	public Collection<MazePath> expand() {
		ArrayList<MazePath> returnList = new ArrayList<MazePath>();
		for(Orientation o:Orientation.values()){
			MazeNode neighbourNode = getCurrentEndTile().getNodeAt(o);
			if(neighbourNode!=null && neighbourNode.getClass().equals(TileNode.class) && !this.contains(neighbourNode)){
				returnList.add(new MazePath(this,(TileNode) neighbourNode));
			}
		}
		return returnList;
	}
	
	@Override
	public String toString(){
		String ret = "";
		for(TileNode node:nodeList){
			ret+="("+node.getX()+","+node.getY()+")";
		}
		return ret;
	}

	public MazePath append(MazePath otherPath) {
		MazePath ret = new MazePath(this);
		if(otherPath!=null){
			Iterator<TileNode> otherPathIt = otherPath.iterator();
			if(otherPath.getStartingTile().equals(this.getCurrentEndTile())){
				otherPathIt.next();
			}
			if(otherPathIt.hasNext()){
				boolean valid = false;
				TileNode otherFirst = otherPathIt.next();
				for(Orientation o: Orientation.values()){
					valid |= getCurrentEndTile().getNodeAt(o).equals(otherFirst);
				}
				if(!valid) throw new IllegalArgumentException();
				ret = new MazePath(ret,otherFirst);
				while(otherPathIt.hasNext()){
					ret = new MazePath(ret,otherPathIt.next());
				}
			}
		}
		return ret;
	}
	
	@Override
	public boolean equals(Object arg0){
		if(arg0==null || !arg0.getClass().equals(this.getClass())){
			return false;
		} else {
			Iterator<TileNode> thisIt = this.iterator();
			Iterator<TileNode> otherIt = ((MazePath) arg0).iterator();
			while(thisIt.hasNext() && otherIt.hasNext()){
				if(!thisIt.next().equals(otherIt.next())) return false;
			}
		}
		return this.getGoalTile().equals(((MazePath) arg0).getGoalTile());
	}

}