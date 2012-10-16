package domain.pixels;

import java.awt.Color;

import domain.Position.Position;
/**
 * Deze klasse stelt een pixel voor, deze heeft momenteel alleen integer coordinaten maar in de toekomst zou dit kunnen uitgebreid worden met een kleur bijvoorbeeld.
 * @author P&O Groen
 *
 */
public class Pixel {
	Position position;
	Color color = Color.BLACK;
	public Pixel(Position position){
		this.position=position;
	}
	public Pixel(int x, int y){
		Position coords = new Position(x, y);
		this.position = coords;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return this.color;
	}
	public int getX(){
		return (int) this.position.getX();
	}
	public int getY(){
		return (int) this.position.getY();
	}
}
