package domain.pixels;

import java.util.Arrays;
import java.util.List;

import domain.Position.Position;
import domain.robots.Robot;

public class PixelRobot implements PixelObject {
	
	Pixel[] pixels;
	
	double angle;
	Robot robot;
	public PixelRobot(Robot robot){
		this.robot=robot;
		this.angle = robot.getOrientation();
		
	}
	public Pixel getCenter(){
		 return new Pixel(robot.getPosition());
	}
	@Override
	public List<Pixel> getPixels() {
		pixels = new Pixel[6];
		pixels[0] = getCenter();
		addTale();
		addHead();
		return Arrays.asList(pixels);
		
	}
	
	private void addTale(){
		for(int i = 1; i<4; i++){
			pixels[i] = new Pixel(new Position(getCenter().getX()-i*Math.cos(angle),getCenter().getY()-i*Math.sin(angle)));
		}
	}
	
	private void addHead(){
		 for(int i =1; i<3; i++){
			 pixels[i+3] = new Pixel(new Position(getCenter().getX()+Math.cos(angle+i*30),getCenter().getY()+Math.sin(angle+i*30)));
		 }
		 
	}
	
	

}
