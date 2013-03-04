package domain.maze.barcodes;


import domain.robots.RobotPilot;

public class SetFinishAction implements Action {

	@Override
	public void run(RobotPilot robot) {
		robot.setFinish();
	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.INVALID;
//	}

	@Override
	public String toString() {
		return "Set finish";
	}
	
	

}
