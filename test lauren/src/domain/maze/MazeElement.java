package domain.maze;

import domain.Position.Position;

public abstract class MazeElement {
	
	private final int MAZECONSTANT = 40; //zijdelengte van de rastervensters
	private Position pos1;
	private Position pos2;
	
	public MazeElement(Position position, double robotangle){
		generatePositions(position,robotangle);
	}
	
	public MazeElement(double x, double y, double angle){
		this(new Position(x,y), angle);
	}
	
	
	//ervan uitgaand dat 0,0 in de linkeronderhoek van het rastervenster van de beginpositie van de robot is.
	private void generatePositions(Position position, double robotangle){
		int x = (int) position.getX() / MAZECONSTANT;
		int y = (int) position.getY() / MAZECONSTANT;
		pos1 = new Position(MAZECONSTANT*x, MAZECONSTANT*y);
		// wall = vertical
		if(robotangle < 45){
			pos2 = new Position(MAZECONSTANT*x, MAZECONSTANT * (y+1));
		}
		else { // wall = horizontal
			pos2 = new Position(MAZECONSTANT*(x+1), MAZECONSTANT * y);
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
