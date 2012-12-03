package domain.robots;

import bluetooth.BTCommPC;
import bluetooth.CMD;

import lejos.robotics.navigation.Move.MoveType;
import domain.Position.Position;
import domain.util.WaitObject;

/*
 /**
  * This is our own version of differentialPilot which should work faster
  */
public class DifferentialPilot
       {
	/**
	 * The speed of left and right motor when robot needs to travel straight
	 */
	int[] tMotorSpeed=new int[2];

private long lastPoseUpdateTime;
private Position position= new Position(0,0);
private double rotation=0;
private final BTCommPC btComm;


private boolean prevMovingBool;


private double _robotRotateSpeed;


private double _robotTravelSpeed;


private float _rightWheelDiameter;


private float _trackWidth;


private float _leftWheelDiameter;

//This object is only used so that we can use wait and notify without locking the differentialPilotObject
private WaitObject moveWaiter;
/**
   * Allocates a DifferentialPilot object, and sets the physical parameters of the
   * NXT robot.<br>
   * Assumes Motor.forward() causes the robot to move forward.
   *
   * @param wheelDiameter
   *            Diameter of the tire, in any convenient units (diameter in mm
   *            is usually printed on the tire).
   * @param trackWidth
   *            Distance between center of right tire and center of left tire,
   *            in same units as wheelDiameter.
   * @param leftMotor
   *            The left Motor (e.g., Motor.C).
   * @param rightMotor
   *            The right Motor (e.g., Motor.A).
   */
  public DifferentialPilot(final double wheelDiameter, final double trackWidth,BTCommPC btComm)
  {
    this(wheelDiameter,wheelDiameter, trackWidth, btComm, 1, 2);
  }
  
  /**
   * Allocates a DifferentialPilot object, and sets the physical parameters of the
   * NXT robot.<br>
   *
   * @param leftWheelDiameter
   *            Diameter of the left wheel, in any convenient units (diameter
   *            in mm is usually printed on the tire).
   * @param rightWheelDiameter
   *            Diameter of the right wheel. You can actually fit
   *            intentionally wheels with different size to your robot. If you
   *            fitted wheels with the same size, but your robot is not going
   *            straight, try swapping the wheels and see if it deviates into
   *            the other direction. That would indicate a small difference in
   *            wheel size. Adjust wheel size accordingly. The minimum change
   *            in wheel size which will actually have an effect is given by
   *            minChange = A*wheelDiameter*wheelDiameter/(1-(A*wheelDiameter)
   *            where A = PI/(moveSpeed*360). Thus for a moveSpeed of 25
   *            cm/second and a wheelDiameter of 5,5 cm the minChange is about
   *            0,01058 cm. The reason for this is, that different while sizes
   *            will result in different motor speed. And that is given as an
   *            integer in degree per second.
   * @param trackWidth
   *            Distance between center of right tire and center of left tire,
   *            in same units as wheelDiameter.
   */
  public DifferentialPilot(final double leftWheelDiameter,
          final double rightWheelDiameter, final double trackWidth,BTCommPC btComm, int leftPort, int rightPort)
  {
	this.btComm=btComm;
	//TODO Francis Zorgen dat dit iets doet
    _leftWheelDiameter = (float)leftWheelDiameter;
    _rightWheelDiameter = (float)rightWheelDiameter;
     _trackWidth = (float)trackWidth;   
  }

  public Position getPosition(){
	  updatePose(false);
	  return position.clone();
  }
  
  public double getRotation(){
	  updatePose(false);
	  return rotation;
  }
  /*
   * Returns the left motor.
   * @return left motor.
   */

	public boolean isMoving() {
		updatePose(false);
		return prevMovingBool;
	}
	
	private void updatePose(boolean forced) {
		if (forced || lastPoseUpdateTime + 100 < System.currentTimeMillis()) {
			int[] poseValues = btComm.sendCommand(CMD.GETPOSE);
			if (poseValues != null) {
				position = new Position(poseValues[0], poseValues[1]);
				rotation = poseValues[2];
				prevMovingBool = poseValues[3] > 0;
				if (!prevMovingBool) {
					try{
					synchronized (moveWaiter) {
						moveWaiter.setNotifiedTrue();
						moveWaiter.notifyAll();
					}}catch(NullPointerException e){
//						this is when there is no movewaiter and this is normal for endless moves.
					}
				}
				lastPoseUpdateTime = System.currentTimeMillis();
			}
		}
	}

 /**
  * set travel speed in cm per second
  * @param travelSpeed : speed in distance (wheel diameter)units/sec
  */
  public void setTravelSpeed(final double travelSpeed)
  {
    _robotTravelSpeed = (float)travelSpeed;
    btComm.sendCommand(new int[]{CMD.SETTRAVELSPEED, (int) travelSpeed});
  }

  public double getTravelSpeed()
  {
    return _robotTravelSpeed;
  }

  
  /**
   * sets the rotation speed of the vehicle, degrees per second
   * @param rotateSpeed
   */
  public void setRotateSpeed(double rotateSpeed)
  {
    _robotRotateSpeed = (float)rotateSpeed;
    btComm.sendCommand(new int[]{CMD.SETTURNSPEED, (int)rotateSpeed});
  }


  public double getRotateSpeed()
  {
    return _robotRotateSpeed;
  }


  /**
   * Starts the NXT robot moving forward.
   */
  public void forward()
  {
	btComm.sendCommand(new int[]{CMD.KEEPTRAVELLING,1});

  }
  

  /**
   *  Starts the NXT robot moving backward.
   */
	public void backward() {
		btComm.sendCommand(new int[]{CMD.KEEPTRAVELLING,-1});

	}



  /**
   * Rotates the NXT robot through a specific angle. Returns when angle is
   * reached. Wheels turn in opposite directions producing a zero radius turn.<br>
   * Note: Requires correct values for wheel diameter and track width.
   * calls rotate(angle,false)
   * @param angle
   *            The wanted angle of rotation in degrees. Positive angle rotate
   *            left (anti-clockwise), negative right.
   */
	public void rotate(final double angle) {
		prevMovingBool=true;
		//TODO Francis zien wat te doen met de double value van angle
		btComm.sendCommand(new int[]{CMD.TURN,(int)angle});
		waitUntilMovingStops();
  }

  /*
   * This method can be overridden by subclasses to stop the robot if a hazard
   * is detected
   */
  //protected void  continueMoving()
  //{
  //}

  /**
   * Stops the NXT robot.
   */
	public void stop() {
		
		btComm.sendCommand(CMD.STOP);
	}

  /**
   * Moves the NXT robot a specific distance in an (hopefully) straight line.<br>
   * A positive distance causes forward motion, a negative distance moves
   * backward. If a drift correction has been specified in the constructor it
   * will be applied to the left motor.
   * calls travel(distance, false)
   * @param distance
   *            The distance to move. Unit of measure for distance must be
   *            same as wheelDiameter and trackWidth.
   **/
  public void travel(final double distance)
  {

    if (distance == Double.POSITIVE_INFINITY)
    {
      forward();
      return;
    }
    if ((distance == Double.NEGATIVE_INFINITY))
    {
      backward();
      return;
    }  
	prevMovingBool=true;
    //TODO Francis:  zien wat te doen met die double waarde van distance
	btComm.sendCommand(new int[]{CMD.TRAVEL,(int)distance});	
// setMoveType( MoveType.STOP);
	waitUntilMovingStops();
  }

private void waitUntilMovingStops() {
	if(moveWaiter!=null){
		System.out.println("moveWaiter was niet null");
	}
	moveWaiter=new WaitObject();
	while(!moveWaiter.hasReallyBeenNotified()&&!Thread.interrupted()){
		try {
			synchronized (moveWaiter) {
				moveWaiter.wait();
			}
		} catch (InterruptedException e) {
			moveWaiter=null;
			break;
		}
	}
	moveWaiter=null;
}

public void keepTurning(boolean left) {
		btComm.sendCommand(new int[]{CMD.KEEPTURNING,(left?1:-1)});	
	}

public void setPose(double orientation, int x, int y) {
	rotation=orientation;
	position=new Position(x,y);
	}

public void interrupt() {
	stop();
   prevMovingBool=false;
try{
   synchronized (moveWaiter) {
		moveWaiter.setNotifiedTrue();
		moveWaiter.notifyAll();
	}}catch(NullPointerException e){
		//This is no problem
	}
}

}