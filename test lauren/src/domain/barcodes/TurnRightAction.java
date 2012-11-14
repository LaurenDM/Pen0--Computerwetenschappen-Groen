package domain.barcodes;

import domain.robots.Robot;

public class TurnRightAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.turn(360);
	}

}
