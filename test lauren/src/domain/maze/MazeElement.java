package domain.maze;

import domain.Position.Position;

public abstract class MazeElement {
	
	private final int MAZECONSTANT = 40; //zijdelengte van de rastervensters
	private Position pos1;
	private Position pos2;
	
	public MazeElement(Position pos1, Position pos2, Position pos3){
		final double MARGE = 1;
		if(Math.abs(pos1.getX() - pos2.getX())<=MARGE && Math.abs(pos1.getX() - pos3.getX())<=MARGE){
			//wall = vertical
			int x =(int) (Math.floor((pos1.getX())/MAZECONSTANT))*MAZECONSTANT;
			int lowy = (int) (Math.floor((pos1.getY())/MAZECONSTANT))*MAZECONSTANT;
			pos1 = new Position(x,lowy);
			pos2 = new Position(x,lowy+MAZECONSTANT);
		}
		else if(Math.abs(pos1.getY() - pos2.getY())<=MARGE && Math.abs(pos1.getY() - pos3.getY())<=MARGE){
			//wall = horizontal
			int y =(int) (Math.floor((pos1.getY())/MAZECONSTANT))*MAZECONSTANT;
			int lowx = (int) (Math.floor((pos1.getX())/MAZECONSTANT))*MAZECONSTANT;
			pos1 = new Position(lowx,y);
			pos2 = new Position(lowx + MAZECONSTANT,y);
		}
	}
	
	

	public Position getPos1(){
		return pos1;
	}
	
	public Position getPos2(){
		return pos2;
	}
	
	public boolean hasPosition(Position position){
		final double MARGE = 0.5;
		if(pos1.getX() == pos2.getX()){
			//wall = vertical
			if(Math.abs(pos1.getX() - position.getX()) > MARGE) return false;
			if((pos1.getY() - MARGE<=position.getY() && pos2.getY() + MARGE >= position.getY()) ||
					(pos1.getY() + MARGE >=position.getY() && pos2.getY() - MARGE <=position.getY())){
				return true;
			}
			else return false;
		}
		else if(pos1.getY() == pos2.getY()){
			//wall = horizontal
			if(Math.abs(pos1.getY() - position.getY()) > MARGE) return false;
			if((pos1.getX() - MARGE <=position.getX() && pos2.getX() + MARGE >= position.getX()) ||
					(pos1.getX() + MARGE >=position.getX() && pos2.getX() - MARGE <=position.getX())){
				return true;
			}
			else return false;
		}
		else return false;
	}
	
}
