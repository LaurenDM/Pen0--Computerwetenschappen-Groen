package domain;
//TODO uitmaken of deze klasse wel bruikbaar is.
import domain.robots.Robot;

public class PolygonDriver {
	Robot robot;
	
	public PolygonDriver(Robot robot) {
	this.robot= robot;	
	}
	
	public void drive(int nbVertices, double edgeLength){
		double turnAngle=calcPolygonAngles(nbVertices);
		for(int i=0; i<nbVertices;i++){
			robot.move(edgeLength);
			robot.turn(turnAngle);
		}
	}

	private double calcPolygonAngles(int nbVertices) {
		return ((double)((nbVertices-2)*180))/((double)nbVertices);
	}
	
	
}
