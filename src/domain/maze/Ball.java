package domain.maze;

import domain.Position.Position;

public class Ball extends MazeElement{
	
	private Position position;
	private Number number;

	public Ball(Position pos, Number number){
		this.position = pos;
		this.number = number;
	}
	
	public Ball(int number){
		this.number = number;
	}
	
	public Ball(Position pos){
		this(pos, 0);
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
