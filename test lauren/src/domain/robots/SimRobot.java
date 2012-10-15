package domain.robots;


public class SimRobot implements Robot {
	
	private double orientation; // Degrees to horizontal
	private Position position; 
	//The wanted rotation Speed of the robot.
	private double rotateSpeed;
	//The wanted travel Speed of the robot.
	private double travelSpeed;

	//The current state of movement: forward, backward or stopped.
	private Movement movement;
	/**
	 * Assenstelsel wordt geinitialiseerd met oorsprong waar de robot begint
	 */
	public SimRobot(){
		this(0, new Position(0,0));
	}

	public SimRobot(double orientation, Position position){
		setOrientation(orientation);
		setPosition(position);
	}
	
	private void setOrientation(double orientation) {
		this.orientation=orientation;
	}
	
	public Position getPosition(){
		return position.clone();
	}
	
	public void setPosition(Position position){
		this.position = position;
	}
	
	@Override
	public void forward() {
		movement=Movement.FORWARD;
	}

	@Override
	public void backward() {
		movement=Movement.BACKWARD;

	}

	@Override
	public void stop() {
		movement=Movement.STOPPED;

	}

	@Override
	public void move(double distance) {
		if (distance > 0)
			movement = Movement.FORWARD;
		else if (distance < 0)
			movement = Movement.BACKWARD;
		//TODO dit is niet goed want we moeten in de simulator kleine stapjes doen
		position.move(orientation, distance);
		movement = Movement.STOPPED;

	}
	
	//TODO bedenken wat we willen doen met de orientation ivm met over 360 graden en negatieve hoeken.
	@Override
	public void turn(double amount) {
		setOrientation(getOrientation()+amount);
	}

	@Override
	public void turnRight() {
		turn(90);
	}

	@Override
	public void turnLeft() {
		turn(-90);
	}

	@Override
	public void setMovingSpeed(double speed) {
		this.travelSpeed=speed;	
	}

	@Override
	public void setTurningSpeed(double speed) {
		this.rotateSpeed=speed;
	}

	@Override
	public double getMovingSpeed() {
		return travelSpeed;
	}

	@Override
	public double getTurningSpeed() {
		return rotateSpeed;
	}

	@Override
	public double getOrientation() {
		return orientation;
	}
	
	@Override
	public Movement getMovementStatus() {
	return movement;	
	}

}
