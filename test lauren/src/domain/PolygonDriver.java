package domain;
//TODO uitmaken of deze klasse wel bruikbaar is.
import domain.robots.Robot;

public class PolygonDriver extends Thread {
	Robot robot;
	
	public PolygonDriver(Robot robot) {
	this.robot= robot;	
	}
	
	public void drive(int nbVertices, double edgeLength){
		(new DriveThread(nbVertices, edgeLength)).run();
	}
	private class DriveThread extends Thread{
		
		private int nbVertices;
		private double edgeLength;
		public DriveThread(int nbVertices, double edgeLength){
			this.nbVertices=nbVertices;
			this.edgeLength=edgeLength;
		}

		private double calcPolygonAngles(int nbVertices) {
			return 180-((double)((nbVertices-2)*180))/((double)nbVertices);
			}
		@Override
		public void run(){
			double turnAngle=calcPolygonAngles(nbVertices);
			for(int i=0; i<nbVertices;i++){
				robot.move(edgeLength);
				robot.turn(turnAngle);
			}
		}
	}
	
}
