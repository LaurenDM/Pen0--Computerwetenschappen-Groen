package domain.maze.barcodes;

import bluetooth.CMD;
import domain.robots.Robot;

public class SetCheckPointAction implements Action {

	@Override
	public void run(Robot robot) {
		robot.setCheckpoint();
	}

//	@Override
//	public int getActionCommandNb() {
//		robot.setCheckpoint();
//		return CMD.INVALID;
//	}

	@Override
	public String toString() {
		return "Set checkpoint";
	}

}
