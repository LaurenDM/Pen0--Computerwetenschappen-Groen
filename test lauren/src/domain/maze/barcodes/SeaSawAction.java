package domain.maze.barcodes;

import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.SeaSaw;
import domain.robots.RobotPilot;

public class SeaSawAction implements Action {
	
	private int barcodeNb;
	private Position center;
	private Orientation orientation;
	
	public SeaSawAction(int barcodeNb, Position center, Orientation orientation){
		this.barcodeNb = barcodeNb;
		this.center = center;
	}

	@Override
	public void run(RobotPilot robot) {
		robot.getBoard().addFoundSeaSaw(center, orientation, barcodeNb);
		boolean open = robot.detectSeaSawInfrared();
		if(!open){
			robot.driveOverSeeSaw();
			//TODO iets in het maze zodat we aan de overkant de juiste positie hebben en dat de maze
			// weet dat er een wip is (joren)
		}
		else{
			// TODO wat doen we dan?
		}
	}

}
