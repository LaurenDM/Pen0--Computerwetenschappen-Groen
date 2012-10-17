package domain.robots;
import java.io.IOException;

import domain.TimeStamp;
import domain.Position.Position;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.PilotProps;



public class BTRobotPilot implements RobotPilot  {
	
	private DifferentialPilot pilot;
	private RegulatedMotor leftMotor;
	private RegulatedMotor rightMotor;
	
	//The poseProvider is an instantiation of a class that "LeJOS" provides for determining the pose of a NXT Robot.
	private OdometryPoseProvider poseProvider;
	
	
	public BTRobotPilot(){
		
		PilotProps pp = new PilotProps();
    	try {
			pp.loadPersistentValues();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    	float wheelDiameter = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, "5.6"));
    	float trackWidth = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, "11.72"));
    	leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "C"));
    	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "B"));
    	
    	pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor);
    	poseProvider= new OdometryPoseProvider(pilot);
	}
	

	@Override
	public void turn(double amount) {
		pilot.rotate(amount);
	}

	@Override
	public void setMovingSpeed(double speed) {
		pilot.setTravelSpeed(speed);
	}

	@Override
	public void setTurningSpeed(double speed) {
		pilot.setRotateSpeed(speed);
	}

	@Override
	public void forward() {
		pilot.forward();
	}

	@Override
	public void backward() {
		pilot.backward();
	}

	@Override
	public void stop() {
		pilot.stop();
	}

	@Override
	public Position getPosition() {
		return new Position(poseProvider.getPose().getLocation());
	}
	
	//TODO checken of de getheading waarden tussen 0 en 180 graden teruggeeft
	@Override
	public double getOrientation() {
		return poseProvider.getPose().getHeading();
	}

	public boolean isMoving(){
		return pilot.isMoving();
	}


	@Override
	public void UpdateUntil(TimeStamp timestamp) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public double getMovingSpeed() {
		return pilot.getTravelSpeed();
	}


	@Override
	public double getTurningSpeed() {
		return pilot.getRotateSpeed();
	}


	@Override
	public void move(double distance) {
		pilot.travel(distance);
	}

	@Override
	public void turnRight() {
		pilot.rotateRight();
	}

	@Override
	public void turnLeft() {
		pilot.rotateLeft();
	}
	
	public boolean canMove(){
		//TODO
		return true;
		
	}

}
