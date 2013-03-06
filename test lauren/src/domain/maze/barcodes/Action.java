package domain.maze.barcodes;

import domain.robots.RobotPilot;

public interface Action {
	
	public abstract void run(RobotPilot robot);

	public abstract String toString();	
	
}
