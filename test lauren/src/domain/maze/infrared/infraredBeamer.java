package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.MazeElement;
import domain.maze.Orientation;

public class infraredBeamer extends MazeElement{
	private int reach=80; //standard reach is 80
	private Orientation orientation;
//	public final int frequency;
	private Position pos; //center of infraredBeamer
	
	public infraredBeamer(Orientation orientation, int frequency) {
		this.orientation = orientation;
//		this.frequency = frequency;
	}

	public int getReach() {
		return reach;
	}

	public void setReach(int reach) {
		this.reach = reach;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	@Override
	public boolean hasPosition(Position position) {
		return position.equals(position);
	}

	@Override
	public Position getCenterPosition() {
		return pos;
	}
}
