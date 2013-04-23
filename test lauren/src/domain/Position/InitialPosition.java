package domain.Position;

import domain.maze.Orientation;

public class InitialPosition extends Position {
	
	private Orientation orientation;

	public InitialPosition(double x, double y, Orientation orientation) {
		super(x, y);
		this.orientation = orientation;
	}
	
	public InitialPosition(Position pos, Orientation orientation){
		this(pos.getX(), pos.getY(), orientation);
	}
	
	public Position getPosition(){
		return new Position(getX(),getY());
	}
	
	public Orientation getOrientation(){
		return this.orientation;
	}

}
