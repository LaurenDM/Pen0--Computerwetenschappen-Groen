package domain.robotFunctions;
import domain.robots.CannotMoveException;
import domain.robots.RobotPilot;
import domain.robots.SimRobotPilot;

/**
 * A class that contains functions for straightening a robot (real or simulated).
 * @author Joren
 */
public class Straightener {
	private static final int DISTANCE_BETWEEN_SENSOR_AND_WHEELS =9;
	//The robot that needs to be straightened
	private RobotPilot robot;

	/**
	 * Create a straightener for the given robot.
	 * @param robot
	 */
	public Straightener(RobotPilot robot){
		this.robot = robot;
	}

	/**
	 * Position the robot perpendicular to a straight line using the light sensor.
	 * This method assumes that the robot's light sensor is already at a white line.
	 */
	public void straighten(boolean white){
		straighten(0, white);

	}
	
	public void findLine(boolean white){
		boolean detected = false;
		boolean lastDetection = false;
		try {
			if(white){
			robot.forward();}
			else{
				robot.backward();
			}
		} catch (CannotMoveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(!detected){
			if((white&&robot.detectWhiteLine())||(!white&&robot.detectBlackLine()) ){
				lastDetection = true;
				if(lastDetection){
					detected=true;
					}
				}
			else{
				lastDetection = false;
			}
		}
		//We move 1 cm because otherwise we are standing in the beginnen and not the middle of the white line 
		try {
			robot.move(1);
		} catch (CannotMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void turnUntil(boolean white, boolean detect, boolean left, int wantedDetections) {
		double turnSpeed = robot.getTurningSpeed();
		robot.setTurningSpeed(1);
		if(robot.getClass() == SimRobotPilot.class){
			robot.setTurningSpeed(100);
		}
		int consecutiveDetections = 0;
		robot.keepTurning(left);
		while(consecutiveDetections < wantedDetections){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if((white&&robot.detectWhiteLine())||(!white&&robot.detectBlackLine())){
				consecutiveDetections++;

			} else {
				consecutiveDetections=0;
			}
		}
		//TODO needs to be looked at.
		//straightenOnLine();
		robot.stop();
		robot.setTurningSpeed(turnSpeed);
//		robot.setTurningSpeed(5);
	}

	/**
	 * Assumes that the robot is turned towards the left!
	 * The robot also doesn't return to it's original orientation afterwards!
	 */
	public void straightenOnLine(){
		robot.turnSensorForward();
		double dist = robot.readUltrasonicValue() % 40;
		if(dist < 18 || dist > 22){
			try {
				robot.move(dist-20);
			} catch (CannotMoveException e) {
				e.printStackTrace();
			}
		}
	}

	public void straighten(int angleCorrection, boolean white) {
		boolean left = true;
		boolean detect = true;
		boolean wood = false;
		boolean turnDirection;
		double turnSpeed = robot.getTurningSpeed();
		double moveSpeed = robot.getMovingSpeed();
		findLine(white);
		try {
			robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
		} catch (CannotMoveException e) {
//			turnDirection = getTurnDirection();
//			try {
//				robot.move(-((double)DISTANCE_BETWEEN_SENSOR_AND_WHEELS)*1.1);
//				//Nu staat de robot zeker terug achter de witte lijn.
//			} catch (CannotMoveException e1) {
//				//Should be impossible
//			}
//			if(turnDirection){
//				//links draaien
//				robot.turn(-20);
//			}
//			else { //rechts
//				robot.turn(20);
//			}
//			robot.findWhiteLine();
//			try { 
//				robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
//			}
//			catch (CannotMoveException e1) {
//				e.printStackTrace();
//			}
			e.printStackTrace();
		}
		turnDirection = getTurnDirection();
		turnUntil(white,detect, turnDirection, 3);
		//Now the robot is aligned with the line
		int turnAmount = 90;
		turnAmount = (90-angleCorrection)*(turnDirection?1:-1);
//		robot.resetToDefaultSpeeds();
		robot.setMovingSpeed(moveSpeed);
		robot.setTurningSpeed(turnSpeed);
		straightenOnLine();
		robot.turn(turnAmount);
		
		//robot.setPose(0,20,0);
	}

	private void findBlackline() {
		// TODO Auto-generated method stub
		
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
