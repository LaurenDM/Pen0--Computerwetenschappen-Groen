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
		boolean open = robot.checkForSeaSawInfrared();
		//TODO: iets toevoegen met het resultaat
	}

}
