package domain.maze;

import java.util.List;
import java.util.ArrayList;

import domain.Position.Position;

public class Board {
	
	private List<Wall> walls;
	
	
	public Board(int width, int height){
		walls = new ArrayList<Wall>();
		}
	
	public void addWall(Wall wall){
		walls.add(wall);
	}
	
	public boolean detectWallAt(Position position){
		for(Wall wall: walls){
			if(wall.hasPosition(position)) return true;
		}
		return false;
	}
	
	

}
