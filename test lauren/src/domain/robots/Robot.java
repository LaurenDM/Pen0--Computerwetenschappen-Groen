package domain.robots;

import domain.TimeStamp;
import domain.Position.Position;

public abstract class Robot implements iRobot {
	
	private Movement movement;
	//The wanted rotation Speed of the robot.
	private double rotateSpeed;

	//The wanted travel Speed of the robot.
	private double travelSpeed;

	@Override
	public void UpdateUntil(TimeStamp timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public Movement getMovementStatus() {
		return movement;
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
		this.movement = Movement.FORWARD;
	}

	@Override
	public void backward(){
		this.movement = Movement.BACKWARD;
	}

	@Override
	public void stop(){
		this.movement = Movement.STOPPED;
	}


	@Override
	public abstract void turn(double amount);

	@Override
	public void turnRight() {
		turn(90);
	}

	@Override
	public void turnLeft() {
		turn(-90);
	}

	@Override
	public abstract double getOrientation();

	@Override
	public void setMovingSpeed(double speed) {
		this.travelSpeed = speed;
	}

	@Override
	public void setTurningSpeed(double speed) {
		this.rotateSpeed = speed;
	}

	@Override
	public abstract boolean canMove();

	@Override
	public Position getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
