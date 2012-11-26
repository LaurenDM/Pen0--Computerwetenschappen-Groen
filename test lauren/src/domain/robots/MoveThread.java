package domain.robots;

public class MoveThread extends Thread {
	Movement moveType;
	SimRobotPilot simRobotPilot;
	boolean whiteLine = false;

	public MoveThread(Movement moveType, SimRobotPilot simRobotPilot, boolean whiteLine){
		this.moveType= moveType;
		this.simRobotPilot=simRobotPilot;
		this.whiteLine= whiteLine;
		
		Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
		    public void uncaughtException(Thread th, Throwable ex) {
		    	ex.printStackTrace();
		    }
		};
		Thread.setDefaultUncaughtExceptionHandler(h);
	}

	public MoveThread(Movement moveType, SimRobotPilot simRobotPilot){
		this(moveType, simRobotPilot, false);
	}
	
	@Override
	public void run(){
		moveType.execute(simRobotPilot, whiteLine);
	}
	
	public Movement getMovement(){
		return this.moveType;
	}
	
	
	
	
}
