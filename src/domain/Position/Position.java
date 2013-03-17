package domain.Position;
import java.awt.geom.Point2D.Float;
import java.lang.Math;

import domain.maze.Orientation;

import lejos.geom.Point;

public class Position implements Cloneable {
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
	
	public double getAngleTo(Position other){
		int delta_x = (int) Math.abs(getX()-other.getX());
		int delta_y = (int) Math.abs(getY()-other.getY());
		return Math.atan(delta_y/delta_x);
	}
	
	
}
