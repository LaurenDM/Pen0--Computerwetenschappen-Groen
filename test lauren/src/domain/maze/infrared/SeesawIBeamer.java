package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.Board;
import domain.maze.Orientation;
import domain.maze.Seesaw;

public class SeesawIBeamer extends RayBeamer{
	private final int reach=150;
	private final Seesaw seesaw;
	public SeesawIBeamer(Seesaw seesaw, Board simWorldBoard){
		super(simWorldBoard);
		simWorldBoard.addInfraBeamer(this);
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
		return seesaw.getCenterPosition();
	}
	@Override
	protected int getReach() {
		return reach;
	}
	
	//Hier checken we of het infrarood signaal uitgezonden door deze beamer opgehouden wordt door seesaws, en deze beamer zit ook onder een  bepaalde seesaw
	//Dus we checken tov de gegeven detectorPositie
	@Override
	protected boolean isBlockedByAnySeesawMid(Position iDetectorPos) {
		if(this.seesaw.hasPosition(iDetectorPos)){
			return true;//Als de detector op deze seesaw zit, dan kan die de infrarood eronder niet zien
		}
		for (Seesaw checkSeesaw : simWorldBoard.getSeesaws()) {
			if (!this.seesaw.equals(checkSeesaw)) {
				// We berekenen de posities van het lijnstuk gevorm door het
				// midden dan de seesaw
				if (isBlockedBySeesawMiddle(iDetectorPos, checkSeesaw)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected double getMaxIrValue() {
		return 160;
	}
	
}
