package domain.maze.barcodes;


import domain.robots.RobotPilot;

public class TurnLeftAction implements Action {

	@Override
	public void run(RobotPilot robot) {
		robot.turn(-360); //TODO checken of dit links is
	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.TURN;
//	}
//	
//	@Override
//	public int getActionCommandArg(){
//		return -360;
//	}
	
	@Override
	public String toString() {
		return "Turn left";
	}

}
