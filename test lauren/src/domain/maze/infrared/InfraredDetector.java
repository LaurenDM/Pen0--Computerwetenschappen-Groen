package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.Board;
import domain.maze.Orientation;
import domain.maze.Wall;
import domain.robots.SimRobotPilot;

public class InfraredDetector {
private SimRobotPilot simRobot;
private Board simWorldBoard;
private final int viewAngle=30;

public InfraredDetector(Board simWorldBoard, SimRobotPilot simRobot){
	this.simRobot=simRobot;
	this.simWorldBoard=simWorldBoard;
}

public int getInfraredValue(){
	int sumOfInfrared=0;
	for(InfraredBeamer iBeamer:simWorldBoard.getInfraredBeamers()){
		if(this.seesInfraredBeamer(iBeamer)){
			sumOfInfrared+=iBeamer.getBeamedInfraredAt(getPosition());
		}
	}
	return sumOfInfrared; //TODO infrared
}
private boolean seesPosition(Position pos){
	return false; //TODO infrared
}
private boolean seesInfraredBeamer(InfraredBeamer IBeamer){
	return false; //TODO infrared
}
public Position getPosition(){
	return simRobot.getPosition().clone();
}

public double getOrientation(){
	return simRobot.getOrientation();
}
}
