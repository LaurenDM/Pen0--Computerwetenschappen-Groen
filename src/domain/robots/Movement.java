package domain.robots;

public enum Movement {

	FORWARD(1, " moving forward"),BACKWARD(-1,"moving backward" ), STOPPED(0, "stopped");
	private int speedMultiplier;
	private String description;
	private boolean running;
	Movement(int speedMultiplier, String description){
		this.speedMultiplier=speedMultiplier;
		this.description=description;
		this.running=  true;
	}
	public int getSpeedMultiplier(){
		return speedMultiplier;
	}
	@Override
	public String toString(){
		return "the robot is "+ description;
	}
	
	public boolean isRunning(){
		return this.running;
	}
	
	
	public synchronized void execute(SimRobotPilot simRobotPilot, boolean whiteLine)  {
		double speed=simRobotPilot.getMovingSpeed();
		double moveAmount = 0.5* speedMultiplier;
		int sleepTime= Math.abs((int) Math.round((1000* moveAmount/speed)));
		running = true;
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
}
