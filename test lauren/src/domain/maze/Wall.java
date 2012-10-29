package domain.maze;

import domain.Position.Position;

public class Wall extends MazeElement{
	
	

	public Wall(Position position,double angle) {
		super(position,angle);
	}
	
	public Wall(double x, double y, double angle){
		super(x,y,angle);
	}
	
}
