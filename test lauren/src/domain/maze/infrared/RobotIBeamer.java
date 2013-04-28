package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.Seesaw;
import domain.robots.RobotPilot;
import domain.util.Geometrics;

public class RobotIBeamer extends InfraredBeamer {
	private final int reach=80;
	private domain.robots.RobotPilot robot;
	public RobotIBeamer(RobotPilot robot){
		super(robot.getWorldSimulator().getBoard());
		this.robot=robot;
	}
	@Override
	public double getOrientation() {
		return robot.getOrientation();
	}

	@Override
	public Position getCenterPosition() {
		return robot.getPosition();
	}
	@Override
	protected int getReach() {
		return reach;
	}
	@Override
	protected boolean isBlockedByAnySeesawMid(Position checkPos) {
		//nu nemen we ook nog seesaws in rekening
				for(Seesaw seesaw:simWorldBoard.getSeesaws()){
					//We berekenen de posities van het lijnstuk gevorm door het midden dan de seesaw
					if(isBlockedBySeesawMiddle(checkPos, seesaw)){
						return true;
					}
				}
				return false;
	}
	@Override
	protected double getMaxIrValue() {
		return 80;
	}

}
