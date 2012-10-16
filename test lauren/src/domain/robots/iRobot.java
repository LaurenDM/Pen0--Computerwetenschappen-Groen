package domain.robots;

import domain.TimeStamp;
import domain.Position.Position;

public interface iRobot {
	public void UpdateUntil(TimeStamp timestamp);
	
	public Movement getMovementStatus();
	
	public double getMovingSpeed();
	
	public double getTurningSpeed();
	/**
	 * Can be used to get a clone of the current position of the robot
	 * @return a clone of the position
	 */
	public Position getPosition();
	
	public void forward();
	
	public void backward();
	
	public void stop();

	/**
	 * 
	 * @param distance in cm
	 */
	public void move(double distance);
	
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
	
	public boolean canMove();
	
	
	
}
