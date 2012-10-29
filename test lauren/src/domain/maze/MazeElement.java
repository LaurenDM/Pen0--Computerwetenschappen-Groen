package domain.maze;

import domain.Position.Position;

public abstract class MazeElement {
	
	private final int MAZECONSTANT = 40;
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
}
