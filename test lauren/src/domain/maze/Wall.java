package domain.maze;

import domain.Position.Position;

public class Wall {

	private Position position;
	
	public Wall(Position position){
		this.position = position;
	}
	
	public Wall(double x, double y){
		this(new Position(x,y));
	}
	
	public Position getPosition(){
		return position;
	}
	
}
