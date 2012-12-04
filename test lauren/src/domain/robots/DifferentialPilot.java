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
public class DifferentialPilot {
	/**
	 * The speed of left and right motor when robot needs to travel straight
	 */

	private long lastPoseUpdateTime;
	private Position position = new Position(0, 0);
	private double rotation = 0;
	private final BTCommPC btComm;

	private boolean prevMovingBool;

	private double _robotRotateSpeed;

	private double _robotTravelSpeed;

	// This object is only used so that we can use wait and notify without
	// locking the differentialPilotObject
	private WaitObject moveWaiter;

	/**
	 * Allocates a DifferentialPilot object, and sets the physical parameters of
	 * the NXT robot.<br>
	 * 
	 * @param wheelDiameter
	 *            diameter of the wheels
	 * @param trackWidth
	 *            Distance between center of right tire and center of left tire,
	 *            in same units as wheelDiameter.
	 */
	public DifferentialPilot(double wheelDiameter, final double trackWidth,
			BTCommPC btComm) {
		this.btComm = btComm;

		setWheelDiameter(wheelDiameter);
		setTrackWidth(trackWidth);

	}

	private void setTrackWidth(double trackWidth) {
		btComm.sendCommand(CMD.SETTRACKWIDTH, trackWidth);
	}

	private void setWheelDiameter(double wheelDiameter) {
		btComm.sendCommand(CMD.SETWHEELDIAMETER, wheelDiameter );

	}

	public Position getPosition() {
		updatePose(false);
		return position.clone();
	}

	public double getRotation() {
		updatePose(false);
		return rotation;
	}

	/*
	 * Returns the left motor.
	 * 
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
					try {
						synchronized (moveWaiter) {
							moveWaiter.setNotifiedTrue();
							moveWaiter.notifyAll();
						}
					} catch (NullPointerException e) {
						// this is when there is no movewaiter and this is
						// normal for endless moves.
					}
				}
				lastPoseUpdateTime = System.currentTimeMillis();
			}
		}
	}

	/**
	 * set travel speed in cm per second
	 * 
	 * @param travelSpeed
	 *            : speed in distance (wheel diameter)units/sec
	 */
	public void setTravelSpeed(final double travelSpeed) {
		_robotTravelSpeed = (float) travelSpeed;
		btComm.sendCommand(CMD.SETTRAVELSPEED,travelSpeed);
	}

	public double getTravelSpeed() {
		return _robotTravelSpeed;
	}

	/**
	 * sets the rotation speed of the vehicle, degrees per second
	 * 
	 * @param rotateSpeed
	 */
	public void setRotateSpeed(double rotateSpeed) {
		_robotRotateSpeed = (float) rotateSpeed;
		btComm.sendCommand(CMD.SETTURNSPEED,rotateSpeed);
	}

	public double getRotateSpeed() {
		return _robotRotateSpeed;
	}

	/**
	 * Starts the NXT robot moving forward.
	 */
	public void forward() {
		btComm.sendCommand(CMD.KEEPTRAVELLING, 1);

	}

	/**
	 * Starts the NXT robot moving backward.
	 */
	public void backward() {
		btComm.sendCommand(CMD.KEEPTRAVELLING, -1);

	}

	/**
	 * Rotates the NXT robot through a specific angle. Returns when angle is
	 * reached. Wheels turn in opposite directions producing a zero radius turn.<br>
	 * Note: Requires correct values for wheel diameter and track width. calls
	 * rotate(angle,false)
	 * 
	 * @param angle
	 *            The wanted angle of rotation in degrees. Positive angle rotate
	 *            left (anti-clockwise), negative right.
	 */
	public void rotate(final double angle) {
		prevMovingBool = true;
		// TODO Francis zien wat te doen met de double value van angle
		btComm.sendCommand(CMD.TURN, angle);
		waitUntilMovingStops();
	}

	/*
	 * This method can be overridden by subclasses to stop the robot if a hazard
	 * is detected
	 */
	// protected void continueMoving()
	// {
	// }

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
	 * will be applied to the left motor. calls travel(distance, false)
	 * 
	 * @param distance
	 *            The distance to move. Unit of measure for distance must be
	 *            same as wheelDiameter and trackWidth.
	 **/
	public void travel(final double distance) {

		if (distance == Double.POSITIVE_INFINITY) {
			forward();
			return;
		}
		if ((distance == Double.NEGATIVE_INFINITY)) {
			backward();
			return;
		}
		prevMovingBool = true;
		// TODO Francis: zien wat te doen met die double waarde van distance
		btComm.sendCommand(CMD.TRAVEL, distance);
		// setMoveType( MoveType.STOP);
		waitUntilMovingStops();
	}

	private void waitUntilMovingStops() {
		if (moveWaiter != null) {
			System.out.println("moveWaiter was niet null");
		}
		moveWaiter = new WaitObject();
		while (!moveWaiter.hasReallyBeenNotified() && !Thread.interrupted()) {
			try {
				synchronized (moveWaiter) {
					moveWaiter.wait();
				}
			} catch (InterruptedException e) {
				moveWaiter = null;
				break;
			}
		}
		moveWaiter = null;
	}

	public void keepTurning(boolean left) {
		btComm.sendCommand(CMD.KEEPTURNING, (left ? 1 : -1));
	}

	public void setPose(double rotation, int x, int y) {
		this.rotation = rotation;
		position = new Position(x, y);
		btComm.sendCommand(CMD.SETROTATION, rotation);
		btComm.sendCommand(CMD.SETX, x);
		btComm.sendCommand(CMD.SETY, y);
		
	}

	public void interrupt() {
		stop();
		prevMovingBool = false;
		try {
			synchronized (moveWaiter) {
				moveWaiter.setNotifiedTrue();
				moveWaiter.notifyAll();
			}
		} catch (NullPointerException e) {
			// This is no problem
		}
	}
}
