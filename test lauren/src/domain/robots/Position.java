package domain.robots;
import java.lang.Math;

public class Position {
	
	public Position(final double x, final double y){
		setX(x);
		setY(y);
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
	
}
