package domain.robots;
import java.lang.Math;

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
		setX(getX()+Math.cos(orientation)*distance);
		setY(getY()+Math.sin(orientation)*distance);
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
	
}
