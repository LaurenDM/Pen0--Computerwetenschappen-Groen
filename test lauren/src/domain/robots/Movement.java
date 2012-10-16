package domain.robots;

public enum Movement {

	FORWARD(1, " moving forward"){

		@Override
		public void execute(SimRobotPilot simRobotPilot) {
			while(true){
				double speed=simRobotPilot.getMovingSpeed();
				double moveAmount=1;
//				sleepTime=moveAmount/simRobotPilot.getMovingSpeed()
				simRobotPilot.getPosition().move(simRobotPilot.getOrientation(), moveAmount);
				int sleepTime=10;
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
	} ,BACKWARD(-1,"moving backward" ){

		@Override
		public void execute(SimRobotPilot simRobotPilot) {
			//TODO
		}
		
	}
	, STOPPED(0, "stopped"){

		@Override
		public void execute(SimRobotPilot simRobotPilot) {
		}
		
	};
	private int speedMultiplier;
	private String description;
	Movement(int speedMultiplier, String description){
		this.speedMultiplier=speedMultiplier;
		this.description=description;
	}
	public int getSpeedMultiplier(){
		return speedMultiplier;
	}
	@Override
	public String toString(){
		return "the robot is "+ description;
	}
	public abstract void execute(SimRobotPilot simRobotPilot);
	
	
	
}
