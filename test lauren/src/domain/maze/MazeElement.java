package domain.maze;

import domain.Position.Position;

public abstract class MazeElement {
	
	protected static final int MAZECONSTANT = 40; //zijdelengte van de rastervensters
	private Position position1;
	private Position position2;
	
	public MazeElement(){
		
	}
	
	
	public static int getMazeConstant(){
		return MAZECONSTANT;
	}
	
	public abstract boolean hasPosition(Position position);
	
	
	public abstract Position getCenterPosition();
	
}
