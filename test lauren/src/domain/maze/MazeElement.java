package domain.maze;

import domain.Position.Position;

public abstract class MazeElement {
	
	private Position position;
	private double angle;
	
	public MazeElement(Position position, double angle){
		this.position = position;
		this.angle = angle;
	}
	
	public MazeElement(double x, double y, double angle){
		this(new Position(x,y), angle);
	}
	
	public Position getPosition(){
		return position;
	}
	
	public double getAngle(){
		return angle;
	}
	

}
