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
		//De orientatie van een seesaw is altijd zo dat de lage barcode ervoor staat, dwz een positie terug tov het midden en de orientatie.
		if(seesaw.isUpAtLowBCSide()){
		return seesaw.getOrientation().getBack().getAngleToHorizontal();
		}
		else{
			return seesaw.getOrientation().getAngleToHorizontal();

		}
		
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
