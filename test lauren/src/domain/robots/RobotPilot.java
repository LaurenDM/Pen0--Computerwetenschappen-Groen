package domain.robots;

import domain.Position.Position;
import domain.util.TimeStamp;

public interface RobotPilot {
	public void UpdateUntil(TimeStamp timestamp);
		
	public double getMovingSpeed();
	
	public double getTurningSpeed();
	/**
	 * Can be used to get a clone of the current position of the robot
	 * @return a clone of the position
	 */
	public Position getPosition();
	
	public void forward() throws CannotMoveException;
	
	public void backward();
	
	public void stop();

	/**
	 * 
	 * @param distance in cm
	 */
	public void move(double distance) throws CannotMoveException;
	
	/**
	 * 
	 * @param amount in degrees
	 */
	public void turn(double amount);
	
	public void turnRight();
	
	public void turnLeft();
	
	/**
	 * returns the current Orientation relative to the original orientation.
	 */
	public double getOrientation();
	/**
	 * 
	 * @param speed in cm/s
	 */
	public void setMovingSpeed(double speed);
	
	/**
	 * 
	 * @param speed in degrees/s
	 */
	public void setTurningSpeed(double speed);
	
	//Method for testing whether the robot can move with takin into account the walls and otherobstructions.
	public boolean canMove();
	
	public boolean isTouching();
	
	public double readLightValue();
	
	public double readUltrasonicValue();
	
	public void calibrateLightHigh();
	
	public void calibrateLightLow();
	
	public void turnUltrasonicSensor(int angle);
	
	public void turnSensorRight();
	
	public void turnSensorLeft();
	
	public void turnSensorForward();
	
	public boolean detectWhiteLine();
	
	public void makeWall();
}
