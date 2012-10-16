package domain.pixels;

import domain.Position.Position;
import domain.robots.Robot;

public class PixelRobot implements PixelObject {
	
	Pixel[] pixels;
	Pixel center;
	
	double angle;

	public PixelRobot(Robot robot){
		this.angle = robot.getOrientation();
		this.center = new Pixel(robot.getPosition());
	}
	@Override
	public PixelCollection getPixels() {
		pixels = new Pixel[6];
		pixels[0] = center;
		addTale();
		addHead();
		return new PixelCollection(pixels);
		
	}
	
	private void addTale(){
		for(int i = 1; i<4; i++){
			pixels[i] = new Pixel(new Position(center.getX()-i*Math.cos(angle),center.getY()-i*Math.sin(angle)));
		}
	}
	
	private void addHead(){
		 for(int i =1; i<3; i++){
			 pixels[i+3] = new Pixel(new Position(center.getX()+Math.cos(angle+i*30),center.getY()+Math.sin(angle+i*30)));
		 }
		 
	}
	
	

}
