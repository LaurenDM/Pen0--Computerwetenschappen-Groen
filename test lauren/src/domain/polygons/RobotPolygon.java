package domain.polygons;

import java.awt.Color;
import java.awt.Polygon;

import domain.Position.Position;
import domain.robots.RobotPilot;
import domain.util.ColorPolygon;

public class RobotPolygon implements ColorPolygon {
	Polygon polygonRobot;	
	private final RobotPilot robot;
	private Color robotColor=Color.black;
	
	public RobotPolygon(RobotPilot robot){
		this.robot=robot;
	}
	public Position getCenter(){
		return robot.getPosition();
	}
	public double getAngle(){
		return Math.toRadians(robot.getOrientation());
	}
	
	@Override
	public Color getColor() {
		return robotColor;
	}
	@Override
	public Polygon getPolygon() {
		Position center=getCenter();
		double angle= getAngle();
		double length=16;
		int[] xCos=calcXCoords(center, angle, length);
		int[] yCos=calcYCoords(center, angle, length);
		return new Polygon(xCos,yCos,3);
	}
	private int[] calcYCoords(Position center, double angle,double length) {
		int[] yCos= new int[3];
		yCos[0]=(int) Math.round((center.getY()+length*Math.sin(angle)));
		yCos[1]=(int) Math.round((center.getY()+(length/2)*Math.sin(angle-90)));
		yCos[2]=(int) Math.round((center.getY()+(length/2)*Math.sin(angle+90)));
		return yCos;
	}
	private int[] calcXCoords(Position center,double angle, double length) {
		int[] xCos= new int[3];
		xCos[0]=(int) Math.round((center.getX()+length*Math.cos(angle)));
		xCos[1]=(int) Math.round((center.getX()+(length/2)*Math.cos(angle-90)));
		xCos[2]=(int) Math.round((center.getX()+(length/2)*Math.cos(angle+90)));
		return xCos;
		
	}
	
	

}
