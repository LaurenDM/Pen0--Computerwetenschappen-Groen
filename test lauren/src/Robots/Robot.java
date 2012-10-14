package Robots;

public interface Robot {
	
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
	 * 
	 * @param speed in cm/s
	 */
	public void setMovingSpeed(double speed);
	
	/**
	 * 
	 * @param speed in degrees/s
	 */
	public void setTurningSpeed(double speed);
	
	
	
}
