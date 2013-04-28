package domain.robots;

public abstract class DriveThread extends Thread {
	protected MoveType moveType;
	protected SimRobotPilot simRobotPilot;
	public DriveThread(MoveType moveType, SimRobotPilot simRobotPilot){
		this.simRobotPilot=simRobotPilot;
		this.moveType=moveType;
	}
}
