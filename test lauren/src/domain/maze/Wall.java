package domain.maze;

import domain.Position.Position;

public class Wall extends MazeElement{
	
	

	public Wall(Position pos1, Position pos2, Position pos3) throws NotAWallException{
		super(pos1,pos2,pos3);
	}
	
	//used in simulator for loading maze from file
	public Wall(Position pos1, Position pos2){
		super(pos1,pos2);
	}
	
}
