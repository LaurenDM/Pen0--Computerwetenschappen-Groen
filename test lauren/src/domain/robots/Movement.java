package domain.robots;

public enum Movement {

	FORWARD(1, " moving forward") ,BACKWARD(-1,"moving backward" ), STOPPED(0, "stopped");
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
}
