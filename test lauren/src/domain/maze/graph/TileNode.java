package domain.maze.graph;

import java.util.HashMap;

import domain.maze.Orientation;
import domain.maze.Token;

public class TileNode extends MazeNode {
	private HashMap<Orientation,MazeNode> connectedNodes;
	private boolean fullyExpanded = false;
	private boolean finish = false;
	private int x;
	private int y;
	private boolean checkpoint = false;
	private boolean visited = false;
	private int barcodeNumber = -1;
	private boolean hasBarcode = false;
	private int blockNavigationCount = 0;
	
	public TileNode(TileNode currentNode, Orientation orientationToCurrentNode){
		connectedNodes = new HashMap<Orientation,MazeNode>();
		for(Orientation o : Orientation.values()){
			connectedNodes.put(o, null);
		}
		if(currentNode==null || orientationToCurrentNode==null){
			x=0;
			y=0;
		} else {
			setNodeAt(orientationToCurrentNode, currentNode);
			x=currentNode.getX()-orientationToCurrentNode.getXValue();
			y=currentNode.getY()-orientationToCurrentNode.getYValue();
		}
	}
	
	@Override
	public boolean isFullyExpanded() {
		if(fullyExpanded){
			return true;
		} else {
			fullyExpanded=true;
			for(Orientation o:getConnectedNodes().keySet()){
				if(o!= null && getConnectedNodes().get(o) == null){
					fullyExpanded=false;
				}
			}
			return fullyExpanded;
		}
	}

	public MazeNode getNodeAt(Orientation currentRobotOrientation) {
		return getConnectedNodes().get(currentRobotOrientation);
	}
	
	public void setNodeAt(Orientation currentRobotOrientation, MazeNode mazeNode) {
		getConnectedNodes().put(currentRobotOrientation, mazeNode);
	}
	
	private HashMap<Orientation, MazeNode> getConnectedNodes() {
		return connectedNodes;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	@Override
	public boolean isFinish() {
		return finish;
	}

	public void setCheckpoint(boolean arg) {
		checkpoint=arg;
	}

	public boolean isCheckpoint(){
		return checkpoint;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public String toString(){
		return "Tile("+x+","+y+","+(connectedNodes.get(Orientation.NORTH)!=null)+","+(connectedNodes.get(Orientation.EAST)!=null)+","+(connectedNodes.get(Orientation.SOUTH)!=null)+","+(connectedNodes.get(Orientation.WEST)!=null)+")";
	}
	
	@Override
	public String toShortString(){
		return "("+x+","+y+")";
	}

	public int manhattanDistanceTo(TileNode otherTile) {
		return Math.abs(getX()-otherTile.getX())+Math.abs(getY()-otherTile.getY());
	}

	public void setVisited(){
		visited = true;
	}
	
	public boolean isVisited() {
		return visited ;
	}

	public int numberOfConnections() {
		int ret = 0;
		for(MazeNode node : connectedNodes.values()){
			if(node!=null) ret++;
		}
		return ret;
	}
	
	public Orientation relativeOrientationTo(TileNode otherTile) {
		Orientation rOrientation = null;
		if(otherTile==null){
			System.out.println("Warning! Tried to calculate relative orientation to null!");
		} else if(otherTile.getX()!=this.getX()&&otherTile.getY()!=otherTile.getY()){
			throw new IllegalArgumentException("Invalid relative tile location.");
		} else if(otherTile.getX()==this.getX()) {
			int yDist = otherTile.getY()-this.getY();
			for(Orientation o : Orientation.values()){
				if(yDist/Math.abs(yDist)==o.getYValue()){
					rOrientation = o;
				}
			}
		} else if(otherTile.getY()==this.getY()) {
			int xDist = otherTile.getX()-this.getX();
			for(Orientation o : Orientation.values()){
				if(xDist/Math.abs(xDist)==o.getYValue()){
					rOrientation = o;
				}
			}
		}
		return rOrientation;
	}
	
	public boolean isAccessible() {
		return getBlockNavigationCount()==0;
	}
	
	public void setAccessible(boolean accessible) {
		if(accessible){
			this.setBlockNavigationCount(0);
		} else {
			this.setBlockNavigationCount(3); //TODO put a less arbitrary value here.
		}
	}

	public int getBlockNavigationCount() {
		return blockNavigationCount;
	}

	protected void setBlockNavigationCount(int blockNavigationCount) {
		this.blockNavigationCount = blockNavigationCount;
	}
	
	public void decreaseBlockNavigationCount() {
		if(getBlockNavigationCount()>0){
			setBlockNavigationCount(getBlockNavigationCount()-1);
		}
	}

	public String getToken() {
		return Token.match(this);
	}

	public void setBarcode(int barcodeNumber) {
		setHasBarcode(true);
		setBarcodeNumber(barcodeNumber);
	}

	private void setHasBarcode(boolean b) {
		this.hasBarcode=b;
	}
	
	public boolean hasBarcode(){
		return this.hasBarcode;
	}

	private void setBarcodeNumber(int barcodeNumber) {
		this.barcodeNumber = barcodeNumber;
	}
	
	public int getBarcodeNumber() {
		return this.barcodeNumber;
	}
	
	@Override
	public boolean equals(Object o){
		if(o==null){
			return false;
		} else {
			if(this.getClass().isAssignableFrom(o.getClass())){
				if((((TileNode) o).getX())==getX() && (((TileNode)o).getY())==getY()){
					return true;
				}
			}
		}
		return false;
	}
	
}
