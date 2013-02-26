package domain.maze.barcodes;

import bluetooth.CMD;
import domain.robots.Robot;

public class Wait5Action extends Action {

	@Override
	public void run(Robot robot) {
		robot.wait5Seconds();
	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.WAIT5;
//	}

	@Override
	public String toString() {
		return "Wait 5";
	}

}
