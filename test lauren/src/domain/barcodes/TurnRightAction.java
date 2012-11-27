package domain.barcodes;

import domain.robots.Robot;

public class TurnRightAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.turn(360);
	}

	@Override
	public int[] getActionNb() {
		return new int[]{1, 360};
	}

	@Override
	public String toString() {
		return "Turn right";
	}

}
