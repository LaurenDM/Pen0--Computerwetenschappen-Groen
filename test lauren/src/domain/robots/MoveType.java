package domain.robots;

public enum MoveType {

	FORWARD(1, " moving forward"),BACKWARD(-1,"moving backward" ), STOPPED(0, "stopped"), TURNLEFT(-1, "turning left"), TURNRIGHT(1, "turning right");
	private int speedMultiplier;
	private String description;
	MoveType(int speedMultiplier, String description){
		this.speedMultiplier=speedMultiplier;
		this.description=description;
	}

	@Override
	public String toString(){
		return "the robot is "+ description;
	}
	public int getSpeedMultiplier(){
		return speedMultiplier;
	}
	
	
}
