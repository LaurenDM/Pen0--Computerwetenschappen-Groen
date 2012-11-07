package domain.robots;



import java.io.IOException;

import lejos.nxt.remote.NXTCommand;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.Move;

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

  private NXTCommand nxtCommand;
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
  public DifferentialPilot(final double wheelDiameter, final double trackWidth,NXTCommand nxtCommand)
  {
    this(wheelDiameter,wheelDiameter, trackWidth, nxtCommand, 1, 2);
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
          final double rightWheelDiameter, final double trackWidth, NXTCommand nxtCommand, int leftPort, int rightPort)
  {
	this.nxtCommand= nxtCommand;
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
    setAcceleration((int)(_robotTravelSpeed * 2));
  }

  /*
   * Returns the left motor.
   * @return left motor.
   */
  public int getLeftPort()
  {
    return leftPort;
  }

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
//  private int getLeftCount()
//  {
//    return _left.getTachoCount();
//  }

//  /** TODO
//   * Returns the tachoCount of the right motor
//   * @return tachoCount of the right motor. Positive value means motor has
//   *         moved the robot forward.
//   */
//  private int getRightCount()
//  {
//    return _right.getTachoCount();
//  }

  /*
   * Returns the actual speed of the left motor
   * @return actual speed of left motor in degrees per second. A negative
   *         value if motor is rotating backwards.
   **/
  /*public int getLeftActualSpeed()
  {
    return _left.getRotationSpeed();
  }*/

  /*
   * Returns the actual speed of right motor
   * @return actual speed of right motor in degrees per second. A negative
   *         value if motor is rotating backwards.
   **/
  /*public int getRightActualSpeed()
  {
    return _right.getRotationSpeed();
  }*/

//  private void setSpeed(final int leftSpeed, final int rightSpeed)
//  {
//    _left.setSpeed(leftSpeed);
//    _right.setSpeed(rightSpeed);
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
   * Sets the normal acceleration of the robot in distance/second/second  where
   * distance is in the units of wheel diameter. The default value is 4 times the 
   * maximum travel speed.  
   * @param acceleration
   */
  public void setAcceleration(int acceleration)
  {
   
  _acceleration = acceleration;
//   setMotorAccel(_acceleration);
  }
// /**
//   * helper method for setAcceleration and quickStop
//   * @param acceleration 
//   */
//  private void setMotorAccel(int acceleration)         
//  {
//       int motorAccel  = (int)Math.round(0.5 * acceleration * (_leftDegPerDistance + _rightDegPerDistance));
//    _left.setAcceleration(motorAccel);
//    _right.setAcceleration(motorAccel); 
//  }

  /**
   * sets the rotation speed of the vehicle, degrees per second
   * @param rotateSpeed
   */
  public void setRotateSpeed(double rotateSpeed)
  {
    _robotRotateSpeed = (float)rotateSpeed;
//    int[] speed= {(int)Math.round(rotateSpeed * _leftTurnRatio), (int)Math.round(rotateSpeed * _rightTurnRatio)};
  }


  public double getRotateSpeed()
  {
    return _robotRotateSpeed;
  }


//  public float getMaxRotateSpeed()
//  {
//    return Math.min(_left.getMaxSpeed(), _right.getMaxSpeed()) / Math.max(_leftTurnRatio, _rightTurnRatio);
//    // max degree/second divided by degree/unit = unit/second
//  }


//  public double getRotateMaxSpeed()
//  {
//    return getMaxRotateSpeed();
//  }

  /**
   * Starts the NXT robot moving forward.
   */
  public void forward()
  {
//  // _type = Move.MoveType.TRAVEL;
    _angle = 0;
   // _distance = Double.POSITIVE_INFINITY;
    try {
  		nxtCommand.setOutputState(leftPort, (byte) tMotorSpeed[0], 0, 0, 0, 0, 0);
  		nxtCommand.setOutputState(rightPort, (byte) tMotorSpeed[1], 0, 0, 0, 0, 0);
  	} catch (IOException e) {
  		//TODO i3+
  	}
  
  }

  /**
   *  Starts the NXT robot moving backward.
   */
	public void backward() {
//		_type = Move.MoveType.TRAVEL;
//		_distance = Double.NEGATIVE_INFINITY;
		_angle = 0;

		  try {
				nxtCommand.setOutputState(leftPort, (byte) -tMotorSpeed[0], 0, 0, 0, 0, 0);
				nxtCommand.setOutputState(rightPort, (byte) -tMotorSpeed[1], 0, 0, 0, 0, 0);
			} catch (IOException e) {
				//TODO i3+
			}
  }

  public void rotateLeft()
  {
	  // _type = Move.MoveType.ROTATE;
	   // _distance = 0;
	    _angle = Double.NEGATIVE_INFINITY;
	    int[] speed= {-Math.round(_robotRotateSpeed * _leftTurnRatio), Math.round(_robotRotateSpeed * _rightTurnRatio)};
	    try {
	  		nxtCommand.setOutputState(leftPort, (byte) speed[0], 0, 0, 0, 0, 0);
	  		nxtCommand.setOutputState(rightPort, (byte) speed[1], 0, 0, 0, 0, 0);
	  	} catch (IOException e) {
	  		//TODO i3+
	  	}
  }

  public void rotateRight()
  {
  // _type = Move.MoveType.ROTATE;
   // _distance = 0;
    _angle = Double.NEGATIVE_INFINITY;
    int[] speed= {Math.round(_robotRotateSpeed * _leftTurnRatio), -Math.round(_robotRotateSpeed * _rightTurnRatio)};
    try {
  		nxtCommand.setOutputState(leftPort, (byte) speed[0], 0, 0, 0, 0, 0);
  		nxtCommand.setOutputState(rightPort, (byte) speed[1], 0, 0, 0, 0, 0);
  	} catch (IOException e) {
  		//TODO i3+
  	}
   
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
  public void rotate(final double angle)
  {
//  // _type = Move.MoveType.ROTATE;
   // _distance = 0;
    _angle = angle;
    int[] speed= {Math.round(_robotRotateSpeed * _leftTurnRatio), Math.round(_robotRotateSpeed * _rightTurnRatio)};
    int rotateAngleLeft = (int) (angle * _leftTurnRatio);
    int rotateAngleRight = (int) (angle * _rightTurnRatio);
    int[] lim={-rotateAngleLeft,rotateAngleRight};
    try {
  		nxtCommand.setOutputState(leftPort, (byte) speed[0], 0, 0, 0, 0, lim[0]);
  		nxtCommand.setOutputState(rightPort, (byte) speed[1], 0, 0, 0, 0, lim[1]);
		Thread.sleep(Math.abs((long) ( angle/_robotRotateSpeed+0.5)*1000));
  	} catch (IOException e) {
  		//TODO i3+
  	} catch (InterruptedException e) {
		// TODO i3+
	}
    
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
  public void stop()
 {
		try {
			nxtCommand.setOutputState(leftPort, (byte) 0, 0, 0, 0, 0, 0);
			nxtCommand.setOutputState(rightPort, (byte) 0, 0, 0, 0, 0, 0);
		} catch (IOException e) {
			// TODO i3+
		}
//		setMotorAccel(_acceleration); // restror acceleration value
	}

//  /**
//   * Stops the robot almost immediately.   Use this method if the normal {@link #stop()}
//   * is too slow;
//   */ 
//  public void quickStop()
//  {
//     setMotorAccel(_quickAcceleration);
//     stop();
//     setMotorAccel(_acceleration);
//  }
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
//  // _type = Move.MoveType.TRAVEL;
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
    }    int[] lim= {(int) (distance * _leftDegPerDistance), (int) (distance * _rightDegPerDistance)};

    
    try {
		nxtCommand.setOutputState(leftPort, (byte) tMotorSpeed[0], 0, 0, 0, 0, lim[0]);
		nxtCommand.setOutputState(rightPort, (byte) tMotorSpeed[1], 0, 0, 0, 0, lim[1]);
		Thread.sleep(Math.abs((long) (distance/_robotTravelSpeed+0.5)*1000));

	} catch (IOException e) {
		//TODO i3+
	} catch (InterruptedException e) {
		// TODO i3+
	}

  }

//  public void arcForward(final double radius)
//  {
//  // _type = Move.MoveType.ARC;
//    if (radius > 0)
//    {
//      _angle = Double.POSITIVE_INFINITY;
//     // _distance = Double.POSITIVE_INFINITY;
//    } else
//    {
//      _angle = Double.NEGATIVE_INFINITY;
//     // _distance = Double.NEGATIVE_INFINITY;
//    }
//    
//    double turnRate = turnRate(radius);
//    steerPrep(turnRate); // sets motor speeds
//    if(_parity >0)_outside.forward();
//    else _outside.backward();
//    if (_steerRatio > 0)  _inside.forward();
//    else _inside.backward();
//  }

//  public void arcBackward(final double radius)
//  {
//   // _type = Move.MoveType.ARC;
//    if (radius < 0)
//    {
//      _angle = Double.POSITIVE_INFINITY;
//    // _distance = Double.NEGATIVE_INFINITY;
//    } else
//    {
//     _angle = Double.NEGATIVE_INFINITY;
//     // _distance = Double.POSITIVE_INFINITY;
//    }
//    
//    double turnRate = turnRate(radius);
//    steerPrep(turnRate);// sets motor speeds
//    if(_parity > 0)_outside.backward();
//    else _outside.forward();
//    if (_steerRatio > 0)_inside.backward();
//     else _inside.forward();
//  }

//  public void arc(final double radius, final double angle)
//  {
//     arc(radius, angle, false);
//  }
//
//  public void  arc(final double radius, final double angle,
//          final boolean immediateReturn)
//  {
//    if (radius == Double.POSITIVE_INFINITY || radius == Double.NEGATIVE_INFINITY)
//    {
//      forward();
//      return;
//    }
//    steer(turnRate(radius), angle, immediateReturn);// type and move started called by steer()
//    // if (!immediateReturn) waitComplete(); redundant I think - BB
//  }

//  public  void  travelArc(double radius, double distance)
//  {
//     travelArc(radius, distance, false);
//  }
//
//  public void travelArc(double radius, double distance, boolean immediateReturn)
//  {
//    if (radius == Double.POSITIVE_INFINITY || radius == Double.NEGATIVE_INFINITY)
//    {
//      travel(distance, immediateReturn);
//      return;
//    }
////  // _type = Move.MoveType.ARC;
//    if (radius == 0)
//    {
//      throw new IllegalArgumentException("Zero arc radius");
//    }
//    double angle = (distance * 180) / ((float) Math.PI * radius);
//    arc(radius, angle, immediateReturn);
//  }

//  /**
//   * Calculates the turn rate corresponding to the turn radius; <br>
//   * use as the parameter for steer() negative argument means center of turn
//   * is on right, so angle of turn is negative
//   * @param radius
//   * @return turnRate to be used in steer()
//   */
//  private double turnRate(final double radius)
//  {
//    int direction;
//    double radiusToUse;
//    if (radius < 0)
//    {
//      direction = -1;
//      radiusToUse = -radius;
//    } else
//    {
//      direction = 1;
//      radiusToUse = radius;
//    }
//    double ratio = (2 * radiusToUse - _trackWidth) / (2 * radiusToUse + _trackWidth);
//    return (direction * 100 * (1 - ratio));
//  }
  
//  
//  /**
//   * Returns the radius of the turn made by steer(turnRate)
//   * Used in for planned distance at start of arc and steer moves.
//   * @param turnRate
//   * @return radius of the turn.    
//   */
//  private  double radius(double turnRate)
//  {
//    double radius = 100*_trackWidth / turnRate;
//    if(turnRate > 0 ) radius -= _trackWidth/2;
//    else radius += _trackWidth/2;  
//    return radius;  
//  }

///**
//   * Starts the robot moving forward along a curved path. This method is similar to the
//   * {@link #arcForward(double radius )} method except it uses the <code> turnRate</code> parameter
//   * do determine the curvature of the path and therefore has the ability to drive straight. This makes
//   * it useful for line following applications.
//   * <p>
//   * The <code>turnRate</code> specifies the sharpness of the turn.  Use values  between -200 and +200.<br>
//   * A positive value means that center of the turn is on the left.  If the
// * robot is traveling toward the top of the page the arc looks like this: <b>)</b>. <br>
//   * A negative  value means that center of the turn is on the  right so the arc looks  this: <b>(</b>. <br>.
//   *  In this class,  this parameter determines the  ratio of inner wheel speed to outer wheel speed <b>as a percent</b>.<br>
//   * <I>Formula:</I> <code>ratio = 100 - abs(turnRate)</code>.<br>
//   * When the ratio is negative, the outer and inner wheels rotate in
//   * opposite directions.
//   * Examples of how the formula works:
//   * <UL>
//   * <LI><code>steer(0)</code> -> inner and outer wheels turn at the same speed, travel  straight
//   * <LI><code>steer(25)</code> -> the inner wheel turns at 75% of the speed of the outer wheel, turn left
//   * <LI><code>steer(100)</code> -> the inner wheel stops and the outer wheel is at 100 percent, turn left
//   * <LI><code>steer(200)</code> -> the inner wheel turns at the same speed as the outer wheel - a zero radius turn.
//   * </UL>
//   * <p>
//   * Note: If you have specified a drift correction in the constructor it will not be applied in this method.
//   *
//   * @param turnRate If positive, the left side of the robot is on the inside of the turn. If negative,
//   * the left side is on the outside.
//   */
//  public void steer(double turnRate)
//  {
//    if (turnRate == 0)
//    {
//      forward();
//      return;
//    }
//    steerPrep(turnRate);
//    if(_parity >0)_outside.forward();
//    else _outside.backward();
//    if (!_steering)  //only call movement start if this is the most recent methoc called
//    {
//    // _type = Move.MoveType.ARC;
//         if(turnRate > 0 )
//     {
//       _angle = Double.POSITIVE_INFINITY;
//      // _distance = Double.POSITIVE_INFINITY;
//     }
//     else 
//     {
//       _angle = Double.NEGATIVE_INFINITY;
//      // _distance = Double.NEGATIVE_INFINITY;
//     }
//      
//      _steering = true;
//    }
//    if (_steerRatio > 0) _inside.forward();
//    else _inside.backward();
//  }

//  /**
//   * Starts the robot moving backward  along a curved path. This method is essentially
//   * the same as
//   * {@link #steer(double)} except that the robot moves backward instead of forward.
//   * @param turnRate
//   */
//  public void steerBackward(final double turnRate)
//  {
//   TODO i2
//  }



//  /** TODO i2
//   * steers the robot a certain angle, rotation-speed is accumulated with forward-speed
//   */
//  public void steer(final double angle)
//  {
//    if (angle == 0)
//    {
//      return;
//    }
//   _angle = angle;
//  // _distance = 2*Math.toRadians(angle)*radius(turnRate); 
//    steerPrep(turnRate);
//    int side = (int) Math.signum(turnRate);
//    int rotAngle = (int) (angle * _trackWidth * 2 / (_leftWheelDiameter * (1 - _steerRatio)));
//    _inside.rotate((int) (side * rotAngle * _steerRatio), true);
//    _outside.rotate(side * rotAngle, immediateReturn);
//    setMotorAccel(_acceleration);
//    if (immediateReturn)
//    {
//      return;
//    }
//     waitComplete();
//    _inside.setSpeed(_outside.getSpeed());
//  }

//  /**
//   * helper method used by steer(float) and steer(float,float,boolean)
//   * sets _outsideSpeed, _insideSpeed, _steerRatio
//   * @param turnRate
//   * .
//   */
//    void steerPrep(final double turnRate)
//  {
//
//    double rate = turnRate;
//    if (rate < -200) rate = -200;
//    if (rate > 200) rate = 200;
//
//    if (turnRate < 0)
//    {
//      insidePort = rightPort;
//      outsidePort = leftPort;
//      rate = -rate;
//    } else
//    {
//      insidePort = leftPort;
//      outsidePort = rightPort;
//    }
//    _outside.setSpeed(_motorSpeed);
//    _steerRatio = (float)(1 - rate / 100.0);
//    _inside.setSpeed((int) (_motorSpeed * _steerRatio));
//     int insideAccel  = (int)Math.round(0.5 * _acceleration*_steerRatio * (_leftDegPerDistance + _rightDegPerDistance));
//    _inside.setAcceleration(insideAccel) ;
//  }


//  /**
//   * called by RegulatedMotor when a motor rotation is complete
//   * calls movementStop() after both motors stop;
//   * @param motor
//   * @param tachoCount
//   * @param  stall : true if motor is sealled
//   * @param ts  s time stamp
//   */
//  public synchronized void rotationStopped(RegulatedMotor motor, int tachoCount, boolean stall,long ts)
//  {
//   if(motor.isStalled())stop();
////   else if (!isMoving());// a motor has stopped
//  }

//  /**
//   * MotorListener interface method is called by RegulatedMotor when a motor rotation starts.
//   * 
//   * @param motor
//   * @param tachoCount
//   * @param stall    true of the motor is stalled
//   * @param ts  time stamp
//   */
//  public synchronized void rotationStarted(RegulatedMotor motor, int tachoCount, boolean stall,long ts)
//  { // Not used
//  }

  
//  /** TODO
//   * @return true if the NXT robot is moving.
//   **/
//  public boolean isMoving()
//  {
//    return _left.isMoving() || _right.isMoving();
//  }

//  /** TODO
//   * wait for the current operation on both motors to complete
//   */
//  private void waitComplete()
//  {
//    while(isMoving())
//    {
//      _left.waitComplete();
//      _right.waitComplete();
//    }
//  }

//  public boolean isStalled() TODO
//  {
//    return _left.isStalled() || _right.isStalled();
//  }
  
//  /** TODO
//   * Resets tacho count for both motors.
//   **/
//  public void reset()
//  { 
//    _leftTC = getLeftCount();
//    _rightTC = getRightCount();
//    _steering = false;
//  }

//  /** TODO
//   * Set the radius of the minimum turning circle.
//   * Note: A DifferentialPilot robot can simulate a SteeringPilot robot by calling DifferentialPilot.setMinRadius()
//   * and setting the value to something greater than zero (example: 15 cm).
//   * 
//   * @param radius  in degrees
//   */
//  public void setMinRadius(double radius)
//  {
//    _turnRadius = (float)radius;
//  }

//  public double getMinRadius() TODO
//  {
//    return _turnRadius;
//  }

//  /** TODO TODO
//   * @return The move distance since it last started moving
//   */
//  public float getMovementIncrement()
//  {
//    float left = (getLeftCount() - _leftTC)/ _leftDegPerDistance;
//    float right = (getRightCount() - _rightTC) / _rightDegPerDistance;
//    return /**/ (left + right) / 2.0f;
//  }

//  /** TODO TODO
//   * @return The angle rotated since rotation began.
//   * 
//   */
// public float getAngleIncrement()
//  {
//    return /**/(((getRightCount() - _rightTC) / _rightTurnRatio) -
//            ((getLeftCount()  - _leftTC) / _leftTurnRatio)) / 2.0f;
//  }

//  public Move getMovement()
//  {
//    return  new Move(_type, getMovementIncrement(), getAngleIncrement(), isMoving());
//  }

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

  private  int _leftTC; // left tacho count
  private  int _rightTC; //right tacho count

  
//  /**
//   */
//   protected Move.MoveType _type;
//   
   /**
    * Distance about to travel - used by movementStarted
    */
//   private double _distance;
   
   /**
    * Angle about to turn - used by movementStopped
    */
   private double _angle;
  private int _acceleration;
   private int  _quickAcceleration; // used for quick stop.
  

}