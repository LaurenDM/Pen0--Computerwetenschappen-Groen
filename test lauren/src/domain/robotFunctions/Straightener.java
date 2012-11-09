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
		boolean rightOrLeft = true;
		boolean detect = true;
		boolean wood = false;
		try {
			robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
		} catch (CannotMoveException e) {
			try { robot.move(-DISTANCE_BETWEEN_SENSOR_AND_WHEELS/2); } catch (CannotMoveException e1) {}
			turnUntil(detect, rightOrLeft);
			try {
				robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
			} catch (CannotMoveException e1) {
				//I don't really know if this is possible
				System.out.println("Apparently you were wrong.");
			}
		}
		turnUntil(detect, rightOrLeft);
		//Now the robot is aligned with the line
		robot.turn(-90);
		robot.setMovingSpeed(15);
		robot.setTurningSpeed(10);
		
	}
	
	private void turnUntil(boolean detect, boolean rightOrLeft) {
		robot.setTurningSpeed(1);
		int consecutiveDetections = 0;
		boolean onLine = false;
		robot.keepTurning(true);
		while(consecutiveDetections < 2){
			//TODO does not work after testing? -Koen.
			//Temp fix with 
			//robot.turn(1);
			if(robot.detectWhiteLine()){
				if(onLine){
					consecutiveDetections++;
				} else {
					onLine=true;
				}
			} else {
				onLine=false;
				consecutiveDetections=0;
			}
		}
		robot.stop();
	}

}
