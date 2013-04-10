package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.Orientation;
import domain.robots.RobotPilot;

public class RobotIBeamer extends InfraredBeamer {
	private final int reach=40;
	private domain.robots.RobotPilot robot;
	public RobotIBeamer(RobotPilot robot){
		this.robot=robot;
	}
	@Override
	public double getOrientation() {
		return robot.getOrientation();
	}

	@Override
	public Position getCenterPosition() {
		//TODO infrared checken of het midden ok is
		return robot.getPosition();
	}
	@Override
	protected int getReach() {
		return reach;
	}
}
