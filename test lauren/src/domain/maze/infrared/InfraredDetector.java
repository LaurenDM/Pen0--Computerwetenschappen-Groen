package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.Board;
import domain.maze.Orientation;
import domain.maze.Wall;
import domain.robots.SimRobotPilot;
import domain.util.Geometrics;

public class InfraredDetector {
private final SimRobotPilot simRobot;
private final Board simWorldBoard;
private final int viewAngle=180;

public InfraredDetector(SimRobotPilot simRobot){
	this.simRobot=simRobot;
	this.simWorldBoard=simRobot.getWorldSimulator().getBoard();
}
//We gaan voor alle infraroodzenders af of wij er zelf naartoe kijken en dan hoeveel infrarood van hun tot bij deze detector geraakt.
public int getInfraredValue(){
	int sumOfInfrared=0;
	for(InfraredBeamer iBeamer:simWorldBoard.getInfraredBeamers()){
		if(this.coversPosition(iBeamer.getCenterPosition())){
			sumOfInfrared+=iBeamer.getBeamedInfraredAt(getPosition());
		}
	}
	return sumOfInfrared;
}
//Bepaalt of de positie, kijkhoek en orientatie toelaten om een bepaalde positie te zien (houdt geen rekening met muren)
private boolean coversPosition(Position toCheckPos){
	Position thisPos=this.getPosition();
	double thisOrientation=this.getOrientation();
	
	return Geometrics.fallsInCoveredArea(toCheckPos,thisPos,thisOrientation, this.viewAngle );
}

public Position getPosition(){
	return simRobot.getPosition().getNewRoundPosition(simRobot.getOrientation(), 8);
}

//De detector zit vast op de robot en heeft dezelfde orientatie
public double getOrientation(){
	return simRobot.getOrientation();
}
}
