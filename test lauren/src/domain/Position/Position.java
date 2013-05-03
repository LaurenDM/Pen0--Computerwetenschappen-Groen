package domain.Position;
import java.lang.Math;

import domain.maze.Orientation;
import domain.maze.graph.MazeGraph;
import domain.util.Angles;
import domain.util.Geometrics;

import lejos.geom.Point;

public class Position implements Cloneable {
	private static final int MAZECONSTANT = 40;

	// TODO waarom final in formele parameter?
	public Position(final double x, final double y){
		setX(x);
		setY(y);
	}
	public Position(Point point){
		setX(point.x);
		setY(point.y);
	}
	
	private double x;
	private double y;
	
	public double getX(){
		return x;}
	public double getY(){
		return y;}
	
	public void move(double orientation, double distance){
		setX(getX()+Math.cos(Math.toRadians(orientation))*distance);
		setY(getY()+Math.sin(Math.toRadians(orientation))*distance);
	}
	
	public Position getNewPosition(double orientation, double distance){
		double x = getX()+Math.cos(Math.toRadians(orientation))*distance;
		double y = getY()+Math.sin(Math.toRadians(orientation))*distance;
		return new Position(x,y);
	}
	
	//geeft een positie die sowieso in het midden van een tile staat.
	public Position getNewRoundPosition(double orientation, double distance){
		double x = Math.round( getX()+Math.cos(Math.toRadians(orientation))*distance);
		double y = Math.round(getY()+Math.sin(Math.toRadians(orientation))*distance);
		return new Position(x,y);
	}

	public void setX(double x){
		this.x= x;
	}
	public void setY(double y){
		this.y=y;
	}
	
	@Override 
	public Position clone(){
		return new Position(this.x, this.y);
		
	}
	
	public void snapTo(int mod, int xOffset, int yOffset){
		setX(snapDoubleTo(mod, xOffset, this.x));
		setY(snapDoubleTo(mod, yOffset, this.y));
	}
	
	private int snapDoubleTo(int mod, int offset, double notSnapped) {
		boolean positive=notSnapped>=0;
		notSnapped*=(positive?1:-1);

		int intNotSnapped=(int) notSnapped-offset;

		int snappedNumber=(intNotSnapped/mod)*mod;
		if(intNotSnapped-snappedNumber> mod/2){
			snappedNumber+=mod;
		}
		return (positive?1:-1)*(snappedNumber+offset);
	}

	
	
	public double getDistance(Position other){
		return Math.sqrt(Math.pow(other.getX()-getX(),2) + Math.pow(other.getY()-getY(),2));
	}
	
	@Override
	public boolean equals(Object other){
		return ((Position) other).getX() == this.getX() && ((Position) other).getY() == this.getY();
	}
	
	@Override
	public String toString(){
		return "(" + getX() + "," + getY() + ")";
	}
	
	@Override
	public int hashCode(){
		return 35*(int)getX() + 73*(int)getY() + 184*(int)getX()*(int)getY(); 
	}
	
	public double getAngleTo(Position toCheckPos){
		double toCheckYDiff = this.getY()-toCheckPos.getY();
		double toCheckXDiff = toCheckPos.getX() - this.getX();

		double tanToCheck = (toCheckYDiff) / (toCheckXDiff);
		final double checkPosAngleinRad;
		if(tanToCheck==0){
			if(toCheckXDiff>0){
				checkPosAngleinRad = Math.atan(tanToCheck);
			} else {
				checkPosAngleinRad = Math.atan(tanToCheck) + Math.PI;
			}
		}
		else if (tanToCheck < 0) {
			if (toCheckYDiff < 0) {
				checkPosAngleinRad = Math.atan(tanToCheck);
			} else {
				checkPosAngleinRad = Math.atan(tanToCheck) + Math.PI;
			}
		} else {// tanToCheck>0
			if (toCheckYDiff > 0) {
				checkPosAngleinRad = Math.atan(tanToCheck);
			} else {
				checkPosAngleinRad = Math.atan(tanToCheck) - Math.PI;
			}
		}
		return -checkPosAngleinRad*180/Math.PI;//De min is omdat het coordinatenstelsel linksBOVEN begint ipv linksonder
	}
	
	public static Pose getAbsolutePose(Pose initialPose, Pose relativePose){
		// everything with 'relative' means in the other's board
		// everything without 'relative' is in our board
		if(initialPose == null){
			return relativePose;
		}
		Orientation startOrient = initialPose.getOrientation();
		Orientation relativeStartOrient = MazeGraph.getInitialOrientation();
		
		//Find Orientation
		int offset = relativeStartOrient.getOffsetTo(relativePose.getOrientation());
		Orientation newOrient = startOrient.getOffset(offset);
		
		//Find position
		double deltaY = relativePose.getY();
		double deltaX = relativePose.getX();
		Position newPos = initialPose.getNewPosition(startOrient.getAngleToHorizontal(), deltaX);
		newPos = newPos.getNewPosition(startOrient.getOffset(-1).getAngleToHorizontal(), deltaY);
		return new Pose(newPos, newOrient);
		
		// Belangrijk: deze methode gaat er nu van uit dat relativeStartOrient = East
		// Indien niet moeten lijnen 145 en 146 veranderd worden
	}
	
	public static Pose getRelativePose(Pose initialPose, Pose absolutePose){
		if(initialPose == null){
			return absolutePose;
		}
		Orientation startOrient = initialPose.getOrientation();
		int offset = startOrient.getOffsetTo(MazeGraph.getInitialOrientation());
		Orientation xOrient = MazeGraph.getInitialOrientation().getOffset(offset);
		double deltaY = initialPose.getY()-absolutePose.getY();
		double deltaX = initialPose.getX()-absolutePose.getX();
		Pose relativeStart = new Pose(0,0,MazeGraph.getInitialOrientation());
		Position newPos = relativeStart.getNewPosition(xOrient.getAngleToHorizontal(), deltaX);
		newPos = newPos.getNewPosition(xOrient.getOffset(-1).getAngleToHorizontal(), deltaY);
		return new Pose(newPos, Orientation.EAST);
	}
	
	
	
}
