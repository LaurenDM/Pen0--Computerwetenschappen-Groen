package domain.robots;

public class MoveThread extends Thread {
	Movement moveType;
	SimRobotPilot simRobotPilot;
	public MoveThread(Movement moveType, SimRobotPilot simRobotPilot){
		this.moveType= moveType;
		this.simRobotPilot=simRobotPilot;
		Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
		    public void uncaughtException(Thread th, Throwable ex) {
		    }
		};
		Thread.setDefaultUncaughtExceptionHandler(h);
	}
	
	@Override
	public void run(){
		
			moveType.execute(simRobotPilot);
	}
	
	public Movement getMovement(){
		return this.moveType;
	}
	
	
	
	
}
