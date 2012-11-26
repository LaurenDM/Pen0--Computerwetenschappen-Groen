package domain.robots;

import java.io.IOException;
import java.math.BigInteger;

import bluetooth.BTCommPC;
import bluetooth.CMD;

import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.OutputState;
import lejos.robotics.navigation.Move.MoveType;
import domain.Position.Position;

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


private final int[] rSpeed=new int[2];

private Position position= new Position(0,0);
private double rotation=0;
private long[] prevTachoCount={0,0,0};
final int[] zeros={0,0,0};
private final BTCommPC btComm;
private MoveType previousType;


private boolean prevMovingBool;
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
    this.leftPort = leftPort;
    _leftWheelDiameter = (float)leftWheelDiameter;
    _leftTurnRatio = (float)(trackWidth / leftWheelDiameter);
    _leftDegPerDistance = (float) (360 / (Math.PI * leftWheelDiameter));
    // right
    this.rightPort= rightPort;
    _rightWheelDiameter = (float)rightWheelDiameter;
    _rightTurnRatio = (float)(trackWidth / rightWheelDiameter);
    _rightDegPerDistance = (float)(360 / (Math.PI * rightWheelDiameter));
    // both
    _trackWidth = (float)trackWidth;
    setTravelSpeed(15);
    setRotateSpeed(45);
   setMoveType(MoveType.STOP);
   setMoveType(MoveType.STOP);
    
  }

  public Position getPosition(){
	  return position.clone();
  }
  
  public double getRotation(){
	  return rotation;
  }
  /*
   * Returns the left motor.
   * @return left motor.
   */
  public int getLeftPort()
  {
    return leftPort;
  }
  public void interrupt(){
		shouldStop=true;

//			if(!_type.equals(MoveType.STOP)){
//				if(poseUpdateThread!=null)
//					poseUpdateThread.interrupt();
//	  		while(poseUpdateRunnable==null);
//			while(!poseUpdateRunnable.reallyStopped);
//			}

	  
	}

	public boolean isMoving() {
		updatePose(false);
		return prevMovingBool;
	}
	private void updatePose(boolean forced) {
		int[] poseValues = btComm.sendCommand(CMD.GETPOSE);
		position=new Position(poseValues[0],poseValues[1]);
		rotation=poseValues[2];
		prevMovingBool=poseValues[3]>0;
	}
	// "RunState":
	/** Output will be idle */
	public static final byte MOTOR_RUN_STATE_IDLE = 0x00;
	
  /*
   * returns the right motor.
   * @return right motor.
   */
  public int getRightPort()
  {
    return rightPort;
  }

//  /** TODO
//   * Returns the tachoCount of the left motor
//   * @return tachoCount of left motor. Positive value means motor has moved
//   *         the robot forward.
//   */
//  private long getTachoCount(int port)
//  {
//    try {
//    	return nxtCommand.getTachoCount(port);
//	} catch (IOException e) {
//		//TODO i3+
//		return  prevTachoCount[port];
//	}catch(ArrayIndexOutOfBoundsException e2){
//		return  prevTachoCount[port];
//
//	}
//    
//  }

 /**
  * set travel speed in cm per second
  * @param travelSpeed : speed in distance (wheel diameter)units/sec
  */
  public void setTravelSpeed(final double travelSpeed)
  {
//    _robotTravelSpeed = (float)travelSpeed;
//    _motorSpeed = (int)Math.round(0.5 * travelSpeed * (_leftDegPerDistance + _rightDegPerDistance));
    tMotorSpeed[0]= (int)Math.round(travelSpeed * _leftDegPerDistance /10.0);
    tMotorSpeed[1]=(int)Math.round(travelSpeed * _rightDegPerDistance /10.0);
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
    rSpeed[0]= (int)Math.round(rotateSpeed * _leftTurnRatio);
    rSpeed[1]= -(int)Math.round(rotateSpeed * _rightTurnRatio);
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
   setMoveType( MoveType.TRAVEL);
    _angle = 0;
    _distance = Double.POSITIVE_INFINITY;
	btComm.sendCommand(new int[]{CMD.KEEPTRAVELING,1});

  }
  
//  private void setOppOutputState(int[] power, int[] limit){
//	  int[] oppPower= {-power[0],-power[1]};
//		setOutputState(oppPower, limit);
//	 }

//	private void setOutputState(int[] power, int[] limit) {
//		//System.out.println("from " + previousType + " to " + _type);
//		if (poseUpdateRunnable != null) {
//			poseUpdateRunnable.stop();
//			try {
//				while (!poseUpdateRunnable.reallyStopped);
//			} catch (NullPointerException e) {
//				//This means another thread set the poseUpdateRunnable to null during the whil loop
//			}
//		}
//
//		if (poseUpdateThread != null) {
//			poseUpdateThread.interrupt();
//			while(poseUpdateThread.isAlive());
//		}
//		if (!previousType.equals(MoveType.STOP)&& !_type.equals(MoveType.STOP)) {
//			try {
//				lowLevelSetOutputStates(zeros, zeros);
//				previousType = MoveType.STOP;
//			} catch (IOException e) {
//				// TODO i3+;
//			}
//
//		}
//		runPoseUpdater(true);
//		try {
//
//			lowLevelSetOutputStates(power, limit);
//			
//			if (power[0] != 0 && limit[0] == 0) {
//				poseUpdateRunnable = new PoseUpdater();
//				poseUpdateThread = new Thread(poseUpdateRunnable);
//				poseUpdateThread.start();
//			} else {
//				// power[0]==0: this make sure that the pose-updater runs only
//				// once if the robot is stopped
//
//				runPoseUpdater(false);
//			}
//			
//		} catch (IOException e) {
//			// TODO i3+
//		}
//	}

//	public void lowLevelSetOutputStates(int[] power, int[] limit)
//			throws IOException {
//		if(power[leftPort]==0){
//			nxtCommand.setOutputState(0xFF,(byte) 0, 0, 0, 0, 0, 0);
//		}
//		else{
//			
//		
//		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
//		RobotChecker.disAllowInterruption();
//		nxtCommand.setOutputState(leftPort, (byte) power[0], 0, 0, 0, 0,
//				limit[0]);
//		nxtCommand.setOutputState(rightPort, (byte) power[1], 0, 0, 0, 0,
//				limit[1]);
//		RobotChecker.allowInterruption();
//		Thread.currentThread().setPriority(
//				(Thread.MAX_PRIORITY - Thread.MIN_PRIORITY) / 2);
//		}
//	}

//	public void runPoseUpdater(boolean once) {
//		poseUpdateRunnable = (new PoseUpdater(once));
//		poseUpdateThread= new Thread(poseUpdateRunnable);
//		poseUpdateThread.start();
//		try {
//			poseUpdateThread.join();
//		} catch (InterruptedException e) {
//		
//		}
//		poseUpdateRunnable = null;
//	}
  /**
   *  Starts the NXT robot moving backward.
   */
	public void backward() {
		setMoveType( MoveType.TRAVEL);
		_distance = Double.NEGATIVE_INFINITY;
		_angle = 0;
		btComm.sendCommand(new int[]{CMD.KEEPTRAVELING,-1});

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
		setMoveType(MoveType.ROTATE);
		_distance = 0;
		_angle = angle;
		//TODO Francis zien wat te doen met de double value van angle
		btComm.sendCommand(new int[]{CMD.TURN,(int)angle});
		// setMoveType( MoveType.STOP);

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
   *  side effect: inform listeners of end of movement
   */
	public void stop() {
		shouldStop = true;
		setMoveType(MoveType.STOP);
		btComm.sendCommand(CMD.STOP);
		shouldStop = false;
		// We change the movetype at the end so that the update can update
		// accordingly to the movement that was going on before the stop
		// setMoveType(MoveType.STOP);
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
   setMoveType( MoveType.TRAVEL);
   // _distance = distance;
    _angle = 0;
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
    //TODO Francis:  zien wat te doen met die double waarde van distance
	btComm.sendCommand(new int[]{CMD.TRAVEL,(int)distance});	
// setMoveType( MoveType.STOP);


  }



  private void setMoveType(MoveType type) {
	  previousType=_type;
	  _type=type;
}
private float _turnRadius = 0;
  /**
   * Left motor port
   */
  protected final int leftPort;
  /**
   * Right motor port
   */
  protected final int rightPort;
  /**
   * The motor at the inside of the turn. set by steer(turnRate)
   * used by other steer methodsl
   */
  private int insidePort;
  /**
   * The motor at the outside of the turn. set by steer(turnRate)
   * used by other steer methodsl
   */
  protected int outsidePort;
  /**
   * ratio of inside/outside motor speeds
   * set by steer(turnRate)
   * used by other steer methods;
   */
  private float _steerRatio;
  private boolean _steering = false;
  /**
   * Left motor degrees per unit of travel.
   */
 protected final float _leftDegPerDistance;
  /**
   * Right motor degrees per unit of travel.

   */
   protected final float _rightDegPerDistance;
  /**
   * Left motor revolutions for 360 degree rotation of robot (motors running
   * in opposite directions). Calculated from wheel diameter and track width.
   * Used by rotate() and steer() methods.
   **/
  private final float _leftTurnRatio;
  /**
   * Right motor revolutions for 360 degree rotation of robot (motors running
   * in opposite directions). Calculated from wheel diameter and track width.
   * Used by rotate() and steer() methods.
   **/
  private final float _rightTurnRatio;
  /**
   * Speed of robot for moving in wheel diameter units per seconds. Set by
   * setSpeed(), setTravelSpeed()
   */
  private float _robotTravelSpeed;
  /**
   * Speed of robot for turning in degree per seconds.
   */
  private float _robotRotateSpeed;
  /**
   * Motor speed degrees per second. Used by forward(),backward() and steer().
   */
  private int _motorSpeed;
  /**
   * Distance between wheels. Used in steer() and rotate().
   */
  private final float _trackWidth;
  /**
   * Diameter of left wheel.
   */
  private final float _leftWheelDiameter;
  /**
   * Diameter of right wheel.
   */
  private final float _rightWheelDiameter;


  
  /**
   * type of movement the robot is doing;
   */
   protected MoveType _type;
   
   /**
    * Distance about to travel - used by movementStarted
    */
   private double _distance;
   
   /**
    * Angle about to turn - used by movementStopped
    */
   private double _angle;
  private int _acceleration;
   private int  _quickAcceleration; // used for quick stop.
	private boolean shouldStop=false;

//   Thread poseUpdateThread;
//   PoseUpdater poseUpdateRunnable;
//	private class PoseUpdater implements Runnable {
//		boolean once;
//		private MoveType applicableType;
//		private MoveType otherType;
//		public boolean reallyStopped=false;
//		public PoseUpdater(){
//			this(false);
//			}
//		
//		public void stop() {
//			shouldStop=true;
//		}
//
//		public PoseUpdater(boolean once) {
//			this.once=once;
//		}
//		// previous tachoCount of left[0] and right[1] motor
//		@Override
//		public void run() {
//			reallyStopped=false;
//			long[] diffTacho = new long[3];
//						
//			diffTacho[leftPort] =(BigInteger.valueOf(- prevTachoCount[leftPort]).add(BigInteger.valueOf(getTachoCount(leftPort)))).longValue();
//			diffTacho[rightPort] =(BigInteger.valueOf(- prevTachoCount[rightPort]).add(BigInteger.valueOf(getTachoCount(rightPort)))).longValue();
//	
//			
//			if (once){
//					applicableType=previousType;
//					otherType=_type;
//					update(diffTacho);
//
//				}
//			else {
//				applicableType=_type;
//				otherType=previousType;
//
//				while ( (diffTacho[leftPort]!=0|| diffTacho[leftPort]!=0|| isMoving())) {
//					if(Thread.interrupted()||shouldStop){
//						try {
//							lowLevelSetOutputStates(zeros, zeros);
//						} catch (IOException e) {
//							// TODO i3+;
//						}
//						update(diffTacho);
//						diffTacho[leftPort] =(BigInteger.valueOf(- prevTachoCount[leftPort]).add(BigInteger.valueOf(getTachoCount(leftPort)))).longValue();
//						diffTacho[rightPort] =(BigInteger.valueOf(- prevTachoCount[rightPort]).add(BigInteger.valueOf(getTachoCount(rightPort)))).longValue();
//						reallyStopped=true;
//						break;
//					}
//					
//					update(diffTacho);
//					diffTacho[leftPort] =(BigInteger.valueOf(- prevTachoCount[leftPort]).add(BigInteger.valueOf(getTachoCount(leftPort)))).longValue();
//					diffTacho[rightPort] =(BigInteger.valueOf(- prevTachoCount[rightPort]).add(BigInteger.valueOf(getTachoCount(rightPort)))).longValue();
//					try {
//						if(!_type.equals(MoveType.STOP)){
//						long sleepStart=System.currentTimeMillis();	
//						while(System.currentTimeMillis()-sleepStart<300)
//							if(!shouldStop)
//							Thread.sleep(30);
//							else 
//								throw new InterruptedException("an interrupt signal has been sent by the program");
//						}
//					} catch (InterruptedException e) {
//						
//							try {
//								lowLevelSetOutputStates(zeros, zeros);
//							} catch (IOException e2) {
//								// TODO i3+;
//							}
//							update(diffTacho);
//							diffTacho[leftPort] =(BigInteger.valueOf(- prevTachoCount[leftPort]).add(BigInteger.valueOf(getTachoCount(leftPort)))).longValue();
//							diffTacho[rightPort] =(BigInteger.valueOf(- prevTachoCount[rightPort]).add(BigInteger.valueOf(getTachoCount(rightPort)))).longValue();
//							reallyStopped=true;
//							break;
//					}
//					}
//				shouldStop=false;
//
//			}
//			reallyStopped=true;
//
//		}
//	
//		private void update(long[] diffTacho) {
//			if (applicableType.equals(MoveType.TRAVEL)) {
//				position.move(rotation,
//						((float) diffTacho[leftPort]) /
//						_leftDegPerDistance);
//			} else if (applicableType.equals(MoveType.ROTATE)) {
//				rotation =calcNewOrientation( ((float)diffTacho[leftPort]) / _leftTurnRatio /2.0);
//				
//				rotation=calcNewOrientation( -((float)diffTacho[rightPort]) / _rightTurnRatio/ 2.0);
//				
//			}
//			else if(applicableType.equals(MoveType.STOP)){
//				if (otherType.equals(MoveType.TRAVEL)) {
//					position.move(rotation,
//							((float) diffTacho[leftPort]) /
//							_leftDegPerDistance);
//				} else if (otherType.equals(MoveType.ROTATE)) {
//					rotation =calcNewOrientation( ((float)diffTacho[leftPort]) / _leftTurnRatio /2.0);
//					
//					rotation= calcNewOrientation( -((float)diffTacho[rightPort]) / _rightTurnRatio/ 2.0);
//				}
//			}
//			prevTachoCount[leftPort] += diffTacho[leftPort];
//			prevTachoCount[rightPort] += diffTacho[rightPort];
//
//		}					
//		private double calcNewOrientation(double turnAmount) {
//			double newOrientation = rotation+turnAmount;
//			while (newOrientation < -179) {
//				newOrientation += 360;
//			}
//			while (newOrientation > 180) {
//				newOrientation -= 360;
//			}
//			return newOrientation;
//		}
//	}
	

public void keepTurning(boolean left) {
		setMoveType(MoveType.ROTATE);
		//TODO adjusted -By koen (Left) -> (!Left)
		btComm.sendCommand(new int[]{CMD.KEEPTURNING,(left?1:-1)});	
		
	}

public void setPose(double orientation, int x, int y) {
	rotation=orientation;
	position=new Position(x,y);
	}

}