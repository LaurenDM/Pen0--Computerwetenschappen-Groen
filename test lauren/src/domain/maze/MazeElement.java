package domain.maze;

import domain.Position.Position;

public abstract class MazeElement {
	
	private static final int MAZECONSTANT = 40; //zijdelengte van de rastervensters
	private Position position1;
	private Position position2;
	
	public MazeElement(Position p1, Position p2, Position p3) throws NotAWallException{
		final double MARGE = 1;
		if(Math.abs(p1.getX() - p2.getX())<=MARGE && Math.abs(p1.getX() - p3.getX())<=MARGE){
			//wall = vertical
			int x =(int) (Math.floor((p1.getX())/MAZECONSTANT))*MAZECONSTANT;
			int lowy = (int) (Math.floor((p1.getY())/MAZECONSTANT))*MAZECONSTANT;
			this.position1 = new Position(x,lowy);
			this.position2 = new Position(x,lowy+MAZECONSTANT);
		}
		else if(Math.abs(p1.getY() - p2.getY())<=MARGE && Math.abs(p1.getY() - p3.getY())<=MARGE){
			//wall = horizontal
			int y =(int) (Math.floor((p1.getY())/MAZECONSTANT))*MAZECONSTANT;
			int lowx = (int) (Math.floor((p1.getX())/MAZECONSTANT))*MAZECONSTANT;
			this.position1 = new Position(lowx,y);
			this.position2 = new Position(lowx + MAZECONSTANT,y);
		}
		else{
			throw new NotAWallException();
		}
		//System.out.println("New Wall added from " + this.position1 + " to " + this.position2);
	}
	
	public MazeElement(Position pos1, Position pos2){
		this.position1 = pos1;
		this.position2 = pos2;
		//System.out.println("New Wall added using 2nd method from " + this.position1 + " to " + this.position2);
	}
	
	//used for ball
	public MazeElement(Position pos){
		this.position1 = pos;
	}
	
	public static int getMazeConstant(){
		return MAZECONSTANT;
	}

	public Position getPos1(){
		return position1;
	}
	
	public Position getPos2(){
		return position2;
	}
	
	public boolean hasPosition(Position position){
		final double MARGE = 3; //TODO: hangt af van dikte muren
		if(position1.getX() == position2.getX()){
			//wall = vertical
			if(Math.abs(position1.getX() - position.getX()) > MARGE) return false;
			if((position1.getY() - MARGE<=position.getY() && position2.getY() + MARGE >= position.getY()) ||
					(position1.getY() + MARGE >=position.getY() && position2.getY() - MARGE <=position.getY())){
				return true;
			}
			else return false;
		}
		else if(position1.getY() == position2.getY()){
			//wall = horizontal
			if(Math.abs(position1.getY() - position.getY()) > MARGE) return false;
			if((position1.getX() - MARGE <=position.getX() && position2.getX() + MARGE >= position.getX()) ||
					(position1.getX() + MARGE >=position.getX() && position2.getX() - MARGE <=position.getX())){
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	
	public Position getCenterPosition(){
		if(position1.getX() == position2.getX()){
			//wall = vertical
			if(position1.getY()>position2.getY()){
				return new Position(position1.getX(), position2.getY()+20);
			}
			else if(position1.getY()<position2.getY()){
				return new Position(position1.getX(), position1.getY()+20);
			}
		}
		else if(position1.getY() == position2.getY()){
			//wall = horizontal
			if(position1.getX()>position2.getX()){
				return new Position(position1.getX()-20, position2.getY());
			}
			else if(position1.getX()<position2.getX()){
				return new Position(position1.getX()+20, position2.getY());
			}
		}
		return null;
	}
	
}
