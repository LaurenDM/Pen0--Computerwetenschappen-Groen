package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.Board;
import domain.maze.Orientation;
import domain.maze.Wall;
import domain.robots.SimRobotPilot;
import domain.util.Geometrics;

public class InfraredDetector {
private SimRobotPilot simRobot;
private Board simWorldBoard;
private final int viewAngle=180;

public InfraredDetector(Board simWorldBoard, SimRobotPilot simRobot){
	this.simRobot=simRobot;
	this.simWorldBoard=simWorldBoard;
}
//We gaan voor alle infraroodzenders af of wij er zelf naartoe kijken en dan hoeveel infrarood van hun tot bij deze detector geraakt.
public int getInfraredValue(){
	int sumOfInfrared=0;
	for(InfraredBeamer iBeamer:simWorldBoard.getInfraredBeamers()){
		if(this.coversPosition(iBeamer.getCenterPosition())){
			sumOfInfrared+=iBeamer.getBeamedInfraredAt(getPosition());
		}
	}
	return sumOfInfrared; //TODO infrared
}
//Bepaalt of de positie, kijkhoek en orientatie toelaten om een bepaalde positie te zien (houdt geen rekening met muren)
private boolean coversPosition(Position toCheckPos){
	Geometrics.fallsInCoveredArea(toCheckPos,this.getPosition(),getOrientation(), this.viewAngle );
	return false; //TODO infrared
}

public Position getPosition(){
	return simRobot.getPosition().clone();
}

public double getOrientation(){
	return simRobot.getOrientation();
}
}
