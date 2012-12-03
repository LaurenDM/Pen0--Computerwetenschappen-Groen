package domain.robotFunctions;
import domain.Position.Position;
import domain.robots.BTRobotPilot;
import domain.robots.CannotMoveException;
import domain.robots.Robot;
import domain.robots.SimRobotPilot;

/**
 * A class that contains functions for straightening a robot (real or simulated).
 * @author Joren
 */
public class Straightener extends RobotFunction {
	private static final int DISTANCE_BETWEEN_SENSOR_AND_WHEELS = 8;
	//The robot that needs to be straightened
	private Robot robot;
	
	/**
	 * Create a straightener for the given robot.
	 * @param robot
	 */
	public Straightener(Robot robot){
		this.robot = robot;
	}
	
	/**
	 * Position the robot perpendicular to a straight line using the light sensor.
	 * This method assumes that the robot's light sensor is already at a white line.
	 */
	public void straighten(){
		straighten(0);
		
	}
	
	private void turnUntil(boolean detect, boolean left, int wantedDetections) {
		robot.setTurningSpeed(1);
		if(robot.getRobotPilot().getClass() == SimRobotPilot.class)
			robot.setTurningSpeed(50);
		int consecutiveDetections = 0;
		robot.keepTurning(left);
		while(consecutiveDetections < wantedDetections){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(robot.detectWhiteLine()){
				consecutiveDetections++;
			
			} else {
				consecutiveDetections=0;
			}
		}
		//TODO needs to be looked at.
		//straightenOnLine();
		robot.stop();
		robot.setTurningSpeed(5);
	}
	
	//TODO needs to be implemented.
	private void straightenOnLine(){
		robot.turnSensorLeft();
		double left = robot.readUltrasonicValue() % 40;
		robot.turnSensorForward();
		if(left < 15 || left > 19){
			if(left < 17){
				robot.turnRight();
				try {
					robot.move(17 - left);
				} catch (CannotMoveException e) {
					e.printStackTrace();
				}
				robot.turnLeft();
			}
			if(left > 19){
				robot.turnLeft();
				try {
					robot.move(left - 17);
				} catch (CannotMoveException e) {
					e.printStackTrace();
				}
				robot.turnRight();
			}
		}
	}
	
	public void straighten(int angleCorrection) {
		boolean left = true;
		boolean detect = true;
		boolean wood = false;
		try {
			robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
		} catch (CannotMoveException e) {
			try { robot.move(-DISTANCE_BETWEEN_SENSOR_AND_WHEELS/2); } catch (CannotMoveException e1) {}
			turnUntil(detect, getTurnDirection(), 3);
			try {
				robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
			} catch (CannotMoveException e1) {
				//I don't really know if this is possible
				System.out.println("Apparently you were wrong.");
			}
		}
			turnUntil(detect, getTurnDirection(), 3);
			//Now the robot is aligned with the line
			if(angleCorrection!=0){
			robot.turn(angleCorrection);
			}
		robot.resetToDefaultSpeeds();
		robot.turnRight();	
		//TODO added
		straightenOnLine();
		//robot.setPose(0,20,0);
	}
	
	// Methode geeft true indien left
	private boolean getTurnDirection(){
		robot.turnSensorLeft();
		double leftValue = robot.readUltrasonicValue();
		robot.turnSensorRight();
		double rightValue = robot.readUltrasonicValue();
		robot.turnSensorForward();
		if(leftValue < rightValue)
			return false;
		else
			return true;
	}
	
	}
