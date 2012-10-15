package domain.pixels;

import domain.Position.Position;
/**
 * Deze klasse stelt een pixel voor, deze heeft momenteel alleen integer coordinaten maar in de toekomst zou dit kunnen uitgebreid worden met een kleur bijvoorbeeld.
 * @author P&O Groen
 *
 */
public class Pixel {
	Position position;
	public Pixel(Position position){
		this.position=position;
	}
	public int getX(){
		return (int) this.position.getX();
	}
	public int getY(){
		return (int) this.position.getY();
	}
}
