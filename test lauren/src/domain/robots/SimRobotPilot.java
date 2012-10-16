package domain.robots;

import domain.TimeStamp;
import domain.Position.Position;


public class SimRobotPilot implements RobotPilot {
	private MoveThread moveThread;
	private double orientation; // Degrees to horizontal
	private Position position;

	//The wanted rotation Speed of the robot.
		private double rotateSpeed;

		//The wanted travel Speed of the robot.
		private double travelSpeed;

	
	/**
	 * Assenstelsel wordt geinitialiseerd met oorsprong waar de robot begint
	 */
	public SimRobotPilot(){
		this(0, new Position(0,0));
	}
	
	public SimRobotPilot(double orientation, Position position){
		setOrientation(orientation);
	}
	
	private void setOrientation(double orientation) {
		this.orientation=orientation;
	}
		@Override
	public Position getPosition(){
		return position.clone();
	}
	
	//TODO bedenken wat we willen doen met de orientation ivm met over 360 graden en negatieve hoeken.
	@Override
	public void turn(double amount) {
		setOrientation(getOrientation()+amount);
	}


	@Override
	public double getOrientation() {
		return orientation;
	}
	@Override
	public void UpdateUntil(TimeStamp timestamp) {
		// TODO Auto-generated method stub
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
	public void forward(){
		startMoveThread(Movement.FORWARD);
	}

	public void startMoveThread(Movement movement) {
		stop();
		moveThread= new MoveThread(movement, this);
		moveThread.run();
	}
	

	@Override
	public void backward(){
		startMoveThread(Movement.BACKWARD);

	}

	@Override
	public void stop() {
		if (moveThread != null) {
			moveThread.interrupt();
		}
	}

	@Override
	public void move(double distance) {
		Position pos1 = getPosition();
		boolean running = true;
		forward();
		while(running){
			if(getPosition().getDistance(pos1) == distance || !canMove()){
				running= false;
				stop();
			}
		}
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
		this.travelSpeed = speed;
	}

	@Override
	public void setTurningSpeed(double speed) {
		this.rotateSpeed = speed;
	}

	public boolean canMove(){
		//TODO
		return true;
		
	}


}
