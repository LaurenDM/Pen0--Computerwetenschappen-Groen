package domain.maze.barcodes;

import java.util.List;

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
		this.orientation=orientation;
	}

	@Override
	public void run(RobotPilot robot) {
		Seesaw foundSeesaw = new Seesaw(center, orientation, barcodeNb);
		robot.getBoard().addFoundSeesaw(foundSeesaw);
		robot.handleSeesaw(foundSeesaw);
		
		//TODO Francis S2D2 zorgen dat barcode toegevoegd word op einde
		
	}

}
