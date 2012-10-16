package domain.robots;

import domain.TimeStamp;
import domain.Position.Position;


public class SimRobot extends Robot implements iRobot {
	
	private double orientation; // Degrees to horizontal
	private Position position; 
	
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
	public void move(double distance) {
//		if (distance > 0)
//			movement = Movement.FORWARD;
//		else if (distance < 0)
//			movement = Movement.BACKWARD;
		Position pos1 = getPosition();
		boolean running = true;
//		movement = Movement.STOPPED;
	}

	@Override
	public void UpdateUntil(TimeStamp timestamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canMove() {
		return true;
	}

}
