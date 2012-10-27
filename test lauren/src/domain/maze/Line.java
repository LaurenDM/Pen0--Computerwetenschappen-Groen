package domain.maze;

import domain.Position.Position;

public class Line extends MazeElement {
	
	
	public Line(double x, double y, double angle){
		super(x,y,angle);
	}

	public Line(Position position,double angle) {
		super(position,angle);
	}

	

}
