package domain.maze.barcodes;


import domain.robots.RobotPilot;

public class SetCheckPointAction implements Action {

	@Override
	public void run(RobotPilot robot) {
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
