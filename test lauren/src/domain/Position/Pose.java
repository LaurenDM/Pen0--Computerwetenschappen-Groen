package domain.Position;

import domain.maze.Orientation;

public class Pose extends Position {
	
	private Orientation orientation;

	public Pose(double x, double y, Orientation orientation) {
		super(x, y);
		this.orientation = orientation;
	}
	
	public Pose(Position pos, Orientation orientation){
		this(pos.getX(), pos.getY(), orientation);
	}
	
	public Position getPosition(){
		return new Position(getX(),getY());
	}
	
	public Orientation getOrientation(){
		return this.orientation;
	}

}
