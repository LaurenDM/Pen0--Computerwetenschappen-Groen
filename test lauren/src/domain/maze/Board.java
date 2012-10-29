package domain.maze;

import java.util.List;
import java.util.ArrayList;

import domain.Position.Position;

public class Board {
	
	private final int MAZECONSTANT = 40;
	
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
	
	public boolean detectLineAt(Position position){
		final int MARGE = 1;
		double x_mod = position.getX()%MAZECONSTANT;
		if(Math.min(x_mod, MAZECONSTANT-x_mod) <MARGE) return true;
		double y_mod = position.getY()%MAZECONSTANT;
		if(Math.min(y_mod,  MAZECONSTANT - y_mod)<MARGE) return true;
		return false;
	}

}
