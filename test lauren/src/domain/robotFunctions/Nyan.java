package domain.robotFunctions;

import domain.robots.Robot;
import domain.robots.RobotPilot;

public class Nyan extends RobotFunction {
	
	private RobotPilot robot;

	/**
	 * Nyaaaaaa!
	 * @param newRobot Nya?
	 */
	public Nyan(RobotPilot newRobotPilot){
		this.robot = newRobotPilot;
	}
	
	/**
	 * Nyanyanyaa nyanyanyanyanyanyanyanya nyanya nyanyanyaa nyanyanyanyaanyaa
	 */
	public void nya(){
		robot.playSong("nyan.wav");
	}
	
}
