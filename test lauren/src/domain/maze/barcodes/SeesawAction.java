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
		robot.getBoard().addFoundSeesaw(center, orientation, barcodeNb);
		robot.addSeesaw();
	}

}
