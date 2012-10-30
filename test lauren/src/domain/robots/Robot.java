package domain.robots;

import domain.Position.Position;
import domain.polygons.RobotPolygon;
import domain.util.TimeStamp;

public class Robot {
	RobotPilot robotPilot;
	RobotPolygon robotPolygon;
	public Robot(RobotPilot robotPilot){
		this.robotPilot=robotPilot;
		this.movement=Movement.STOPPED;
		this.robotPolygon=new RobotPolygon(this);
	}
	private Movement movement;
	public RobotPolygon getRobotPolygon(){
		return robotPolygon;
	}

	
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


	public void forward() throws CannotMoveException{
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


	public void move(double distance) throws CannotMoveException {
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
	
	public boolean isTouching(){
		return robotPilot.isTouching();
	}
	
	public double readLightValue(){
		return robotPilot.readLightValue();
	}
	
	public double readUltrasonicValue(){
		return robotPilot.readUltrasonicValue();
	}
	
	public void calibrateLightHigh(){
		robotPilot.calibrateLightHigh();
	}
	
	public void calibrateLightLow(){
		robotPilot.calibrateLightLow();
	}
	
	public void turnUltrasonicSensor(int angle){
		robotPilot.turnUltrasonicSensor(angle);
	}
	
	public void turnSensorRight(){
		robotPilot.turnSensorRight();
	}
	
	public void turnSensorLeft(){
		robotPilot.turnSensorLeft();
	}
	
	public void turnSensorForward(){
		robotPilot.turnSensorForward();
	}

	public boolean detectWhiteLine(){
		return robotPilot.detectWhiteLine();
	}
	
//	public void makeWall(){
//		robotPilot.makeWall();
//	}
}
