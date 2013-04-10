package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.Seesaw;

public class SeesawIBeamer extends InfraredBeamer{
	private final int reach=80;
	private Seesaw seesaw;
	public SeesawIBeamer(Seesaw seesaw){
		this.seesaw=seesaw;
	}
	@Override
	public double getOrientation() {
		// TODO infrared
		return 0;
	}

	@Override
	public Position getCenterPosition() {
		//TODO infrared checken of het midden ok is
		return seesaw.getCenterPosition();
	}
	@Override
	protected int getReach() {
		return reach;
	}

}
