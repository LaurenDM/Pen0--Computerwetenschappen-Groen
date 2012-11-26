package bluetooth;

//Although these are fixed constants we don't use an enum because we need to send integer values via bluetooth.
public class CMD {
	/**
	 * Go forward for a certain distance.
	 */
	public static final int TRAVEL=0;

	/**
	 * Turn the whole robot a certain angle
	 */
	public static final int TURN=1;

	/**
	 * Turn the ultrasonic sensor a certain angle
	 * So if the sensor is at 90  degrees and the second argument of the bluetooth-command is 90, the sensor will turn to 180 degrees.
	 */
	public static final int TURNSENSOR=2;

	
	/**
	 * Turn the ultrasonic sensor to a certain angle
	 * So if the sensor is at 90  degrees and the second argument of the bluetooth-command is 90, the sensor will not turn at all.
	 */
	public static final int TURNSENSORTO=3;

	
	/**
	 * Stop immediately
	 */
	public static final int STOP=4;

	/**
	 * just give a status-update with position and rotation and whether the robot is moving or not.
	 */
	public static final int GETPOSE=5;
	
	/**
	 * just give a status-update with sensor values: light-value, UltrasonicValue, touchValue, and the angle of the ultrasonic sensor.
	 */
	public static final int GETSENSORVALUES=6;

	/**
	 * just give a the battery level.
	 */
	public static final int BATTERY=7;
	
	/**
	 * calibrate the lightsensor for the highest value
	 */
	public static final int CALIBRATELSHIGH=8;
	
	/**
	 * calibrate the lightsensor for the lowest value
	 */
	public static final int CALIBRATELSLOW=9;
	
	/**
	 * Make the robot start turning to the left(1) or right(-1).
	 */
	public static final int KEEPTURNING=10;
	
	/**
	 * keep going forward(1) or backward(-1)
	 */
	public static final int KEEPTRAVELING=11;
	
	/**
	 * ends the bluetooth connection TODO
	 */
	public static final int DISCONNECT = 12;

	

}
