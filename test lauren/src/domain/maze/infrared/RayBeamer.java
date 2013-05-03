package domain.maze.infrared;

import domain.Position.Position;
import domain.maze.Board;
import domain.maze.MazeElement;
import domain.maze.Orientation;
import domain.maze.Seesaw;
import domain.maze.Wall;
import domain.util.Geometrics;
/**
 * TODO de decay rate zou experimenteel bepaald moeten worden en er zou hier met een exponentiele decay rate gewerkt moeten worden
 * @author duvimint
 *
 */
public abstract class RayBeamer extends MazeElement{
	private int standardBeamAngle=150;
	protected final Board simWorldBoard;

	public RayBeamer(Board simWorldBoard){
		this.simWorldBoard=simWorldBoard;
	}
	protected abstract int getReach();
	protected int getBeamAngle(){
		return standardBeamAngle;
	}
	public abstract double getOrientation();

	//kijkt of de infraroodstralen van deze beamer een bepaalde positie kunnen bereiken, rekening houdend met muren en al de rest
	private boolean beamsAt(Position checkPos){
		//eerst checken we de reach
		double distance=this.getCenterPosition().getDistance(checkPos);
		double reach=this.getReach();
		if(distance>reach){
			return false;
		}
		//dan checken we de beamAngle tov de positie
		if(!Geometrics.fallsInCoveredArea(checkPos, this.getCenterPosition(), this.getOrientation(), this.getBeamAngle())){
			return false;
		}
		//Daarna doen we de meest rekeninstensieve test: kijken  of er een muur tussen de beamer en de positie staat.
		for(Wall wall:simWorldBoard.getWalls()){
			if(Geometrics.linesIntersect(wall.getPos1(), wall.getPos2(), this.getCenterPosition(), checkPos)){
					return false;
				}
		}
		if(isBlockedByAnySeesawMid(checkPos)){
			return false;
		}


		return true;

	}
	//Berekent of er een seesawmidden of paneel is die het signaal blokkeert
	protected abstract boolean isBlockedByAnySeesawMid(Position checkPos);
	@Override
	public boolean hasPosition(Position position) {
		return position.equals(getCenterPosition());
	}

	@Override
	public abstract Position getCenterPosition();

	//return how many infrared is beamed from this infrared-beamer at a certain position, taking walls and seesaws into account.
	public int getBeamedInfraredAt(Position checkPos) {
		if(!beamsAt(checkPos)){
			return 0;
		}
		double distance=this.getCenterPosition().getDistance(checkPos);
		return (int) Math.max(0,(getMaxIrValue()-getMaxIrValue()*distance/getReach()));
	}
	protected abstract double getMaxIrValue();

	protected boolean isBlockedBySeesawMiddle(Position checkPos, Seesaw seesaw) {
		Position middlePivotPos1=seesaw.getCenterPosition().getNewRoundPosition(seesaw.getOrientation().getLeft().getAngleToHorizontal(), MAZECONSTANT/2);
		Position middlePivotPos2=seesaw.getCenterPosition().getNewRoundPosition(seesaw.getOrientation().getRight().getAngleToHorizontal(), MAZECONSTANT/2);
		return Geometrics.linesIntersect(middlePivotPos1, middlePivotPos2, this.getCenterPosition(), checkPos);	
	}
}