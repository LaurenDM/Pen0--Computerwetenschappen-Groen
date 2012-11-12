package domain.robotFunctions;
import domain.Position.Position;
import domain.robots.BTRobotPilot;
import domain.robots.CannotMoveException;
import domain.robots.Robot;

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
		robot.setTurningSpeed(10);
		int consecutiveDetections = 0;
//		long previousTime=System.currentTimeMillis();
//		int i=0; //TODO remove
		robot.keepTurning(left);
		while(consecutiveDetections < wantedDetections){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			long timediff=System.currentTimeMillis()-previousTime;//TODO remove
//			previousTime=System.currentTimeMillis(); //TODO remove
			if(robot.detectWhiteLine()){
//				System.out.println(i+ ": white line detected " + timediff +" "+ consecutiveDetections); //TODO Remove
				consecutiveDetections++;
			
			} else {
//				System.out.println(i+ ": white line not detected -reset " + timediff  +" "+ consecutiveDetections);
				consecutiveDetections=0;
			}
			
//			i++;
		
		}
		robot.stop();
		robot.setTurningSpeed(5);
	}

	public void straighten(int angleCorrection) {
		boolean left = true;
		boolean detect = true;
		boolean wood = false;
		try {
			robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
		} catch (CannotMoveException e) {
			try { robot.move(-DISTANCE_BETWEEN_SENSOR_AND_WHEELS/2); } catch (CannotMoveException e1) {}
			turnUntil(detect, left, 3);
			try {
				robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
			} catch (CannotMoveException e1) {
				//I don't really know if this is possible
				System.out.println("Apparently you were wrong.");
			}
		}
			turnUntil(detect, left, 3);
			//Now the robot is aligned with the line
			if(angleCorrection!=0)
			robot.turn(angleCorrection);
		robot.setTurningSpeed(robot.getDefaultTurningSpeed());
		robot.turnRight();
		robot.resetToDefaultSpeeds();		
	}
	}
