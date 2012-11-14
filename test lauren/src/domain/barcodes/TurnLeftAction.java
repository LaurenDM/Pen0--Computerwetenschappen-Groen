package domain.barcodes;

import domain.robots.Robot;

public class TurnLeftAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.turn(-360); //TODO checken of dit links is
	}

}
