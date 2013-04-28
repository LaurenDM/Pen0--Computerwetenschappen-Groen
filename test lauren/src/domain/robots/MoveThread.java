package domain.robots;

public class MoveThread extends DriveThread {

	boolean whiteLine = false;

	public MoveThread(MoveType moveType, SimRobotPilot simRobotPilot, boolean whiteLine){
		super(moveType,simRobotPilot);
		this.whiteLine= whiteLine;
		
		Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
		    @Override
			public void uncaughtException(Thread th, Throwable ex) {
		    	ex.printStackTrace();
		    }
		};
		Thread.setDefaultUncaughtExceptionHandler(h);
	}

	public MoveThread(MoveType moveType, SimRobotPilot simRobotPilot){
		this(moveType, simRobotPilot, false);
	}
	
	@Override
	public void run(){
		double speed=simRobotPilot.getMovingSpeed();
		double moveAmount = 0.5* moveType.getSpeedMultiplier();
		int sleepTime= Math.abs((int) Math.round((1000* moveAmount/speed)));
		boolean running = true;
		while(running){
			if(!simRobotPilot.canMove() && !whiteLine) {
				throw new RuntimeMoveException();
			}
			if(whiteLine){
				while(!simRobotPilot.canMove()){
					simRobotPilot.fixWall();
				}
			}
			//System.out.println(whiteLine + "          " + simRobotPilot.detectWhiteLine());
//			if(whiteLine && simRobotPilot.detectWhiteLine()){
//				running = false;
//				break;
//			}
			simRobotPilot.getPosition().move(simRobotPilot.getOrientation(), moveAmount);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				running = false;
				break;
			}
		}
	}
	
	public MoveType getMovement(){
		return this.moveType;
	}
	
	
	
}
