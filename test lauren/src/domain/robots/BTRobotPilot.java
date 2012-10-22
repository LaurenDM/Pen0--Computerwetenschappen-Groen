package domain.robots;
import java.io.IOException;

import domain.Position.Position;
import domain.util.TimeStamp;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
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
	
	private final double defaultTravelSpeed = 15;
	private final double defaultTurnSpeed = 70;
	
	private TouchSensor touchSensor;
	private UltrasonicSensor ultrasonicSensor;
	private LightSensor lightSensor;
	
	public BTRobotPilot(){
		
		PilotProps pp = new PilotProps();
    	try {
			pp.loadPersistentValues();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    	float wheelDiameter = 5.55F;
    	float trackWidth = 11.22F;
    	leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "C"));
    	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "B"));
    	
    	pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor);
    	setMovingSpeed(defaultTravelSpeed);
    	setTurningSpeed(defaultTurnSpeed);
    	poseProvider= new OdometryPoseProvider(pilot);
    	touchSensor = new TouchSensor(SensorPort.S1);
    	ultrasonicSensor = new UltrasonicSensor(SensorPort.S2);
    	lightSensor = new LightSensor(SensorPort.S3);
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
		pilot.rotate(90);
	}

	@Override
	public void turnLeft() {
		pilot.rotate(90);
	}
	
	public boolean canMove(){
		int distance = ultrasonicSensor.getDistance();	
		int testDistance = 10; 
		if(distance < testDistance)
			return false;
		else
			return true;
	}

}
