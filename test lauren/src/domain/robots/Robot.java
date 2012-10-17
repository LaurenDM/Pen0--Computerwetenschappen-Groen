package domain.robots;

import domain.TimeStamp;
import domain.Position.Position;

public class Robot {
	RobotPilot robotPilot;
	public Robot(RobotPilot robotPilot){
		this.robotPilot=robotPilot;
		this.movement=Movement.STOPPED;
	}
	private Movement movement;

	public void UpdateUntil(TimeStamp timestamp) {
		// TODO Auto-generated method stub
	}


	public Movement getMovementStatus() {
		return movement;
	}


	public double getActualMovingSpeed() {
		return robotPilot.getMovingSpeed()*movement.getSpeedMultiplier();
	}
	
	public double getMovingSpeedSetting() {
		return robotPilot.getMovingSpeed();
	}

	public double getTurningSpeed() {
		return robotPilot.getTurningSpeed();
	}


	public void forward(){
		this.movement = Movement.FORWARD;
		robotPilot.forward();
	}


	public void backward(){
		this.movement = Movement.BACKWARD;
		robotPilot.backward();
	}


	public void stop(){
		this.movement = Movement.STOPPED;
		robotPilot.stop();
	}


	public void move(double distance) {
		if (distance > 0)
			movement = Movement.FORWARD;
		else if (distance < 0)
			movement = Movement.BACKWARD;
		robotPilot.move(distance);
		movement = Movement.STOPPED;
	}


	public void turn(double amount){
		robotPilot.turn(amount);
	}


	public void turnRight() {
		robotPilot.turnRight();
	}


	public void turnLeft() {
		robotPilot.turnLeft();
	}


	public double getOrientation(){
		return robotPilot.getOrientation();
	}


	public void setMovingSpeed(double speed) {
		robotPilot.setMovingSpeed(speed);
	}


	public void setTurningSpeed(double speed) {
		robotPilot.setTurningSpeed(speed);
	}

	public Position getPosition() {
		return robotPilot.getPosition();
	}
	//Method for testing whether the robot can move with takin into account the walls and otherobstructions.
	public boolean canMove(){
		//TODO
		return true;
		
	}

}
