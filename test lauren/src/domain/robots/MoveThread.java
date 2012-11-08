package domain.robots;

public class MoveThread extends Thread {
	Movement moveType;
	SimRobotPilot simRobotPilot;
	public MoveThread(Movement moveType, SimRobotPilot simRobotPilot){
		this.moveType= moveType;
		this.simRobotPilot=simRobotPilot;
	}
	
	@Override
	public void run(){
		try{
			moveType.execute(simRobotPilot);
		} catch(RuntimeException e){
			throw e;
		}
	}
	
	
}
