package domain.maze.barcodes;

import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.Seesaw;
import domain.robots.RobotPilot;

public class SeesawAction implements Action {
	
	private int barcodeNb;
	private Position center;
	private Orientation orientation;
	
	public SeesawAction(int barcodeNb, Position center, Orientation orientation){
		this.barcodeNb = barcodeNb;
		this.center = center;
	}

	@Override
	public void run(RobotPilot robot) {
<<<<<<< HEAD
		robot.getBoard().addFoundSeesaw(center, orientation, barcodeNb);
		robot.addSeesaw();
=======
		Seesaw foundSeesaw = new Seesaw(center, orientation, barcodeNb);
		robot.getBoard().addFoundSeesaw(foundSeesaw);
		if(!foundSeesaw.isLocked()){
			robot.driveOverSeeSawIfPossible();
		}
>>>>>>> 4d7a7a476273068dea7fe731e145f63eea576b48
	}

}
