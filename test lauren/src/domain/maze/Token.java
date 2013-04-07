package domain.maze;

import java.util.HashMap;

import domain.maze.graph.SeesawNode;
import domain.maze.graph.TileNode;
import domain.maze.graph.WallNode;

public enum Token {
	//NESW
	STRAIGHT(false,true,false,true), CORNER(true,false,false,true), T(true,false,false,false), DEADEND(true,true,false,true), CROSS(false,false,false,false), CLOSED(true,true,true,true), SEESAW(false,true,false,true);
	
	private HashMap<Orientation,Boolean> walls = new HashMap<Orientation,Boolean>();

	private Token(boolean northWall, boolean eastWall, boolean southWall, boolean westWall){
		walls.put(Orientation.NORTH, northWall);
		walls.put(Orientation.EAST, eastWall);
		walls.put(Orientation.SOUTH, southWall);
		walls.put(Orientation.WEST, westWall);
	}
	
	/**
	 * Returns a mazefile-specification token for the given TileNode.
	 * Returns null if the node has unexplored tiles or if something went wrong.
	 * @param tile
	 * @return
	 */
	public static String match(TileNode tile){
		//Seesaws are a special case
		if(tile.getClass().equals(SeesawNode.class)){
			for(Orientation o : Orientation.values()){
				if(tile.getNodeAt(o).getClass().equals(SeesawNode.class)){ 
					//If the node at this side is a seesaw all we need to do is take the other side and we know the orientation.
					//Remember, this returns relative orientations not necessarily equal to those specified in the file!
					return "Seesaw."+o.getBack().toString().substring(0, 1).toUpperCase();
				}
			}
			return null;
		} else {
			//Tiles are matched based on where they have walls.
			boolean[] walls = new boolean[4];
			int i=0;
			for(Orientation o : Orientation.values()){
				if(tile.getNodeAt(o)!=null){
					walls[i]=tile.getNodeAt(o).getClass().equals(WallNode.class);
				} else {
					return null;
				}
				i++;
			}
			walls=rotate(walls,-1);
			int offset;
			for(offset=-1;offset<=3;offset++){
				for(Token t : Token.values()){
					Token token = getToken(walls[0],walls[1],walls[2],walls[3]);
					if(token!=null && token.equals(t) && !t.equals(SEESAW)){
						return t.getString()+"."+Orientation.NORTH.getOffset(offset).toString().substring(0, 1)+(tile.hasBarcode()?"."+tile.getBarcodeNumber():""); //That's right, getString, not toString.
					}
				}
				walls=rotate(walls,1);
			}
		}
		return null;
	}
	
	private static boolean[] rotate(boolean[] walls2, int increment) {
		boolean[] walls=walls2;
		if(increment>0){
			boolean save = walls[0];
			for(int i=0;i<walls.length-1;i++){
				walls[i]=walls[i+1];
			}
			walls[walls.length-1]=save;
		} else {
			boolean save = walls[walls.length-1];
			for(int i=walls.length-1;i>0;i--){
				walls[i]=walls[i-1];
			}
			walls[0]=save;
		}	
		return walls;
	}

	/**
	 * Check if it's a seesaw first! This method will always return straight for those values!
	 * Only returns a token if all the walls match in it's default orientation. Orientation must be done outside of this method!
	 */
	public static Token getToken(boolean N, boolean E, boolean S, boolean W){
		for(Token t : Token.values()){
			if(N!=t.hasWallAt(Orientation.NORTH) || E!=t.hasWallAt(Orientation.EAST) || S!=t.hasWallAt(Orientation.SOUTH) || W!=t.hasWallAt(Orientation.WEST)){
				//Keep looking
			} else {
				return t;
			}
		}
		return null;
	}

	private Boolean hasWallAt(Orientation orientation) {
		return walls.get(orientation);
	}
	
	public String getString(){
		if(this.equals(DEADEND)){
			return "DeadEnd";
		} else {
			return this.toString().substring(0, 1).toUpperCase()+this.toString().substring(1).toLowerCase();
		}
	}
}
