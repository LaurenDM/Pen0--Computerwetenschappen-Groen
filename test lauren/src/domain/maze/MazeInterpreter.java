package domain.maze;

import java.io.BufferedReader;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import domain.Position.Position;

public class MazeInterpreter {
	
	private Board board;
	private int firstLine; //number of line with first command
	
	
	public MazeInterpreter(Board board){
		this.board = board;
	}
	
	public void readFile(String fileLocation){
	try{
		firstLine = -1;
		  FileInputStream fstream = new FileInputStream(fileLocation);
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  int lineCounter = 0;

		  while ((strLine = br.readLine()) != null)   {
//		  System.out.println (strLine);
		  readline(strLine, lineCounter);
		  lineCounter++;
		  }
		  in.close();
		    }
	catch (Exception e){
		  System.err.println("Error reading maze.");
		  }
	}
	
	public void readline(String strLine, int numberOfLine){
		if((strLine.startsWith("DeadEnd") || strLine.startsWith("Corner")
		|| strLine.startsWith("Straight") || strLine.startsWith("T."))){
			if(firstLine==-1){
				firstLine = numberOfLine; 
			}
			String[] splitList = strLine.split("	");
			List<String> strList = new ArrayList<String>();
			for(String s: splitList){
				if(s.length()>0){
					strList.add(s);
				}
			}
			for(int i=0; i<strList.size(); i++){
				readCommand(strList.get(i), i, numberOfLine);
			}
		}
	}
	
	public void readCommand(String command, int XCoo, int YCoo){
		YCoo = YCoo - firstLine;
		String[] commandSplit = command.split("\\.");
		
		if(commandSplit[0].equals("DeadEnd")){
			createDeadEnd(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
		else if(commandSplit[0].equals("T")){
			createT(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
		else if(commandSplit[0].equals("Corner")){
			createCorner(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
		else if(commandSplit[0].equals("Straight")){
			createStraight(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
	}
	
	
	public void createDeadEnd(int XCoo, int YCoo, Orientation orientation){
		Position pos = new Position(XCoo, YCoo);
		switch (orientation) {
        case NORTH: 
        	//   ||
        		 makeWall(pos, Orientation.NORTH);
        		 makeWall(pos, Orientation.EAST);
        		 makeWall(pos, Orientation.WEST);
                 break;
		case EAST:
			//	=|
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.SOUTH);
				makeWall(pos, Orientation.EAST);
				break;
		case SOUTH:
			//	|_|
				makeWall(pos, Orientation.EAST);
				makeWall(pos, Orientation.SOUTH);
				makeWall(pos, Orientation.WEST);
				break;		
		case WEST:
			//	|=
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.SOUTH);
				makeWall(pos, Orientation.WEST);
				break;	
		}
	}
	
	public void createCross(){
		//do nothing
	}
	
	public void createCorner(int XCoo, int YCoo, Orientation orientation){
		Position pos = new Position(XCoo, YCoo);
		switch (orientation) {
        case NORTH: 
        	//   |
        		 makeWall(pos, Orientation.NORTH);
        		 makeWall(pos, Orientation.WEST);
                 break;
		case EAST:
			//	|
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.EAST);
				break;
		case SOUTH:
			//	_|
				makeWall(pos, Orientation.EAST);
				makeWall(pos, Orientation.SOUTH);
				break;		
		case WEST:
			//	|_
				makeWall(pos, Orientation.SOUTH);
				makeWall(pos, Orientation.WEST);
				break;	
		}
	}
	
	public void createT(int XCoo, int YCoo, Orientation orientation){
		Position pos = new Position(XCoo, YCoo);
		switch (orientation) {
        case NORTH: 
        		 makeWall(pos, Orientation.NORTH);
                 break;
		case EAST:
				makeWall(pos, Orientation.EAST);
				break;
		case SOUTH:
				makeWall(pos, Orientation.SOUTH);
				break;		
		case WEST:
				makeWall(pos, Orientation.WEST);
				break;	
		}
	}
	
	public void createStraight(int XCoo, int YCoo, Orientation orientation){
		Position pos = new Position(XCoo, YCoo);
		switch (orientation) {
        case NORTH: 
        	//   | |
        		 makeWall(pos, Orientation.EAST);
        		 makeWall(pos, Orientation.WEST);
                 break;
		case EAST:
			//	=
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.SOUTH);
				break;
		case SOUTH:
			//	| |
				makeWall(pos, Orientation.EAST);
				makeWall(pos, Orientation.WEST);
				break;		
		case WEST:
			//	=
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.SOUTH);
				break;	
		}
	}
	
	public void makeWall(Position pos, Orientation orientation){
			// pos is positie van vakje
			// voorbeeld pos 11 met orientation EAST is de oostzijde van vakje 0,0
			// dit zou dan x=40 en y = 0..40
			Position position1 = null;
			Position position2 = null;
			Position posConflict = null; //checkt of er al muren staan op de positie halverwege
			int MAZECONSTANT = MazeElement.getMazeConstant();
			switch (orientation) {
	        case NORTH: 
	        	 position1 = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	        	 position2 = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	        	 posConflict = new Position((pos.getX()+0.5)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	             break;
	        case EAST: 
	        	 position1 = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	        	 position2 = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT); 
	        	 posConflict = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+0.5)*MAZECONSTANT);
	             break;
	        case SOUTH: 
	        	 position1 = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT);
	        	 position2 = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT); 
	        	 posConflict = new Position((pos.getX()+0.5)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT);
	             break;
	        case WEST: 
	        	 position1 = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	        	 position2 = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT);
	        	 posConflict = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+0.5)*MAZECONSTANT);
	             break;
			}
			if(posConflict!=null){
			if(board.detectWallAt(posConflict)==false){
				board.addWall(new Wall(position1, position2));
			}
			}
	}
	
}
