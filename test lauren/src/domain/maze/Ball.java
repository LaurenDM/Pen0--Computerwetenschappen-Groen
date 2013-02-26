package domain.maze;

import domain.Position.Position;

public class Ball extends MazeElement{
	
	private Position position;

	public Ball(Position pos){
		this.position = pos;
	}
	
	public Ball(){
		super(); 
	}
	
	public Position getPosition(){
		return position;
	}

	@Override
	public boolean hasPosition(Position position) {
		return position.equals(this.position);
	}

	@Override
	public Position getCenterPosition() {
		return getPosition();
	}
	
}
