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
		System.out.println("Expanding: "+this);
		ArrayList<MazePath> returnList = new ArrayList<MazePath>();
		for(Orientation o:Orientation.values()){
			MazeNode neighbourNode = getCurrentEndTile().getNodeAt(o);
			System.out.println(o+" neighbour: "+neighbourNode);
			if(neighbourNode.getClass().equals(TileNode.class) && !this.contains(neighbourNode)){
				returnList.add(new MazePath(this,(TileNode) neighbourNode));
			}
		}
		System.out.println(returnList);
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

}