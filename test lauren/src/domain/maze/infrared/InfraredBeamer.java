package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.MazeElement;
import domain.maze.Orientation;

public abstract class InfraredBeamer extends MazeElement{
	private int standardBeamAngle=150;

	protected abstract int getReach();
	protected int getBeamAngle(){
		return standardBeamAngle;
	}

	public abstract double getOrientation();

	private boolean beamsAt(Position pos){
		return false; //TODO infrared
	}
	@Override
	public boolean hasPosition(Position position) {
		return position.equals(getCenterPosition());
	}

	@Override
	public abstract Position getCenterPosition();
	
	//return how many infrared is beamed from this infrared-beamer at a certain position, taking walls and seesaws into account.
	public int getBeamedInfraredAt(Position position) {
		// TODO infrared
		return 0;
	}
}
