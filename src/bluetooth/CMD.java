package bluetooth;



import java.io.IOException;

//Although these are fixed constants we don't use an enum because we need to send integer values via bluetooth.
public interface CMD {
	//Zero is not a valid command
	public final int INVALID=0;
	
	public static final int TURN=1;
	public void turn(double angle);

	/**
	 * Turn the ultrasonic sensor a certain angle
	 * So if the sensor is at 90  degrees and the second argument of the bluetooth-command is 90, the sensor will turn to 180 degrees.
	 */
	public static final int TURNSENSOR=2;
	public void turnSensor(int angle);
	
	/**
	 * Turn the ultrasonic sensor to a certain angle
	 * So if the sensor is at 90  degrees and the second argument of the bluetooth-command is 90, the sensor will not turn at all.
	 */
	public static final int TURNSENSORTO=3;
	public void turnSensorTo(int angle);

	
	/**
	 * Stop immediately and return pose
	 */
	public static final int STOP=4;
	public void stop();
	/**
	 * just give a status-update with position and rotation and whether the robot is moving or not.
	 */
	public static final int GETPOSE=5;
	public int[] getPose();
	/** 
	 * just give a status-update with sensor values: light-value, UltrasonicValue, touchValue, and the angle of the ultrasonic sensor.
	 */
	public static final int GETSENSORVALUES=6;
	public int[] getSensorValues();
	/**
	 * just give a the battery level.
	 */
	public static final int BATTERY=7;
	public int getBattery();
	
	/**
	 * calibrate the lightsensor for the highest value
	 */
	public static final int CALIBRATELSHIGH=8;
	public void calibrateLightSensorHigh();
	
	/**
	 * calibrate the lightsensor for the lowest value
	 */
	public static final int CALIBRATELSLOW=9;
	public void calibrateLightSensorLow();

	/**
	 * Make the robot start turning to the left(1) or right(-1).
	 */
	public static final int KEEPTURNING=10;
	public void keepTurning(boolean left);
	/**
	 * keep going forward(1) or backward(-1)
	 */
	public static final int KEEPTRAVELLING=11;
	public void keepTraveling(boolean forward);
	/**
	 * ends the bluetooth connection TODO
	 */
	public static final int DISCONNECT = 12;
	public void disconnect();
	
	public static final int SCANBARCODE = 13;
	public int[] scanBarcode() throws IOException;
	
	public static final int DRIVESLOW = 14;
	public void driveSlow();
	
	public static final int DRIVEFAST = 15;
	public void driveFast();
	
	public static final int PLAYTUNE = 16;
	public void playTune();
	
	public static final int WAIT5 = 17;
	public void wait5();
	
	public static final int STRAIGHTEN = 18;
	public void straighten();

	/**
	 * 	 use the default travel speed
	 */
	public static final int SETTODEFAULTTRAVELSPEED = 19;
	public void setToDefaultTravelSpeed();
	 
	/**
	 * use the default turn speed
	 */
	public static final int SETTODEFAULTTURNSPEED = 20;
	public void setToDefaultTurnSpeed();
	
	/**
	 * Set the slowTravelSpeed to a certain speed (but do not use that speed).
	 * THE GIVEN SPEED WILL BE DIVIDED BY 100 (to make a double out of it)
	 */
	public static final int SETSLOWTRAVELSPEED = 21;
	public void setSlowTravelSpeed(double speed);
	
	/**
	 * Set the highTravelSpeed to a certain speed (but do not use that speed).
	 * THE GIVEN SPEED WILL BE DIVIDED BY 100 (to make a double out of it)
	 */
	public static final int SETHIGHTRAVELSPEED = 22;
	public void setHighTravelSpeed(double speed);
	
	/**
	 * Set the wheelDiameter in micrometer.
	 */
	public static final int SETWHEELDIAMETER   = 23;
	public void setWheelDiameter(double diameter);
	/**
	 * Set the trackwidth in micrometer.
	 */
	public static final int SETTRACKWIDTH = 24;
	public void setTrackWidth(double trackWidth);
	
	/**
	 * Go forward for a certain distance.
	 */
	public static final int TRAVEL=25;
	public void travel(double distance);
	
	/**
	 * Set the x-coordinate of this robot to a given value
	 * THE GIVEN VAlUE WILL BE DIVIDED BY 100 (to make a double out of it)
	 */
	public static final int SETX = 26;
	public void setX(double xCo);
	 
	/**
	 * Set the y-coordinate of this robot to a given value
	 * THE GIVEN VAlUE WILL BE DIVIDED BY 100 (to make a double out of it)
	 */
	public static final int SETY = 27;
	public void setY(double yCo);
	
	/**
	 * Set the rotation of this robot to a given value
	 * THE GIVEN VAlUE WILL BE DIVIDED BY 100 (to make a double out of it)
	 */
	public static final int SETROTATION = 28;
	public void setRotation(double x);
	 
	/**
	 * Set the defaultTurnSpeed to a certain speed.
	 * THE GIVEN SPEED WILL BE DIVIDED BY 100 (to make a double out of it)
	 */
	public static final int SETDEFAULTTURNSPEED = 29;
	public void setDefaultTurnSpeed(double speed);
	
	/**
	 * Set the defaultTravelSpeed to a certain speed.
	 * THE GIVEN SPEED WILL BE DIVIDED BY 100 (to make a double out of it)
	 */
	public static final int SETDEFAULTTRAVELSPEED = 30;
	public void setDefaultTravelSpeed(double speed);
	
	/**
	 * 	 use the given number as travel speed
	 */
	public static final int SETTRAVELSPEED = 31;
	public void setTravelSpeed(double speed);
	 
	/**
	 * use the given number as turn speed
	 */
	public static final int SETTURNSPEED = 32;
	public void setTurnSpeed(double speed);
	
	/**
	 * auto Calibrate lightsensor of the robot
	 */
	public static final int AUTOCALIBRATELS = 33;
	public void autoCalibrateLightSensor(int difference);
	
	public static final int FETCHBALL = 34;
	public void fetchBall();
	
}
