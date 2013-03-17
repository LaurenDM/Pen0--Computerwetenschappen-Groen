package domain.maze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RandomMazeGenerator {
	
	private MazeInterpreter MI;
	private ArrayList<String> allowedOnTopRow;
	private ArrayList<String> allowedOnFrontRow;
	private ArrayList<String> allowedOnBottomRow;
	private ArrayList<String> allowedOnRightRow;
	private ArrayList<String> allowedInCenter;
	private ArrayList<String> orientation;
//	private ArrayList<Integer> finishPositions;
	private boolean hasFinish;
	private boolean hasCheckpoint;
	private BufferedWriter bw;

		
	public RandomMazeGenerator(MazeInterpreter MI){
		this.MI = MI;
		fillAllowedOnTopRow();
		fillAllowedInCenter();
		fillAllowedOnBottomRow();
		fillAllowedOnFrontRow();
		fillAllowedOnRightRow();
		fillOrientation();
		startPrinter();
		generateMaze(8, 8);  // adjust dimensions here
		
	}
	
	private void generateMaze(int width, int height){
		String row ="Corner.N" + "	";
		//generate top row
		for(int i=1; i<width-1; i++){
			String sector = allowedOnTopRow.get((int) (Math.random() * allowedOnTopRow.size())); 
			row = row + sector + "	";
			if(sector.contains("Straight") && !hasFinish){
			row = row.substring(0, row.length()-1) +".55	";
			hasFinish = true;
			}
		}
		row = row + "Corner.E";
		try {bw.write(row+"\n");	} catch (IOException e) {System.out.println("error printing to file");}
		MI.readline(row, 0);
		
		for(int i=1; i<height; i++){
			row =allowedOnFrontRow.get((int) (Math.random() * allowedOnFrontRow.size()))+ "	";
			//generate middle rows
			for(int j=0; j<width-2; j++){
				String sector = allowedInCenter.get((int) (Math.random() * allowedInCenter.size()));
				row = row + sector +"."+ orientation.get((int) (Math.random() * orientation.size())) + "	";
				if(sector.contains("Straight") && !hasCheckpoint){
					row = row.substring(0, row.length()-1) +".13	";
					hasCheckpoint = true;
				}
			}
			row = row + allowedOnRightRow.get((int) (Math.random() * allowedOnRightRow.size()));
			try {bw.write(row+"\n");	} catch (IOException e) {System.out.println("error printing to file");}
			MI.readline(row, i);
		}
		row = "Corner.W" + "	";;
		for(int i=0; i<width-2; i++){
			String sector = allowedOnBottomRow.get((int) (Math.random() * allowedOnBottomRow.size()));
			row = row + sector + "	";
		}
		row = row + "Corner.S";
		MI.readline(row, height);
		try {bw.write(row+"\n");	bw.close();} catch (IOException e) {System.out.println("error printing to file");}
	}
	

	private void startPrinter(){
		File file = new File("generatedMaze.txt");
			try {
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	private void fillAllowedInCenter(){
		allowedInCenter = new ArrayList<String>();
		allowedInCenter.add("Straight");
		allowedInCenter.add("Corner");
		allowedInCenter.add("T");
//		allowedInCenter.add("DeadEnd");
		allowedInCenter.add("Cross");
	}
	
	private void fillAllowedOnTopRow(){
		allowedOnTopRow = new ArrayList<String>();
		allowedOnTopRow.add("Straight.E");
//		allowedOnTopRow.add("Straight.W");
		allowedOnTopRow.add("Corner.E");
		allowedOnTopRow.add("Corner.N");
		allowedOnTopRow.add("T.N");
//		allowedOnTopRow.add("DeadEnd.W");
//		allowedOnTopRow.add("DeadEnd.N");
//		allowedOnTopRow.add("DeadEnd.E");
	}
	
	private void fillAllowedOnBottomRow(){
		allowedOnBottomRow = new ArrayList<String>();
		allowedOnBottomRow.add("Straight.E");
//		allowedOnBottomRow.add("Straight.W");
		allowedOnBottomRow.add("Corner.S");
		allowedOnBottomRow.add("Corner.W");
		allowedOnBottomRow.add("T.S");
//		allowedOnBottomRow.add("DeadEnd.W");
//		allowedOnBottomRow.add("DeadEnd.S");
//		allowedOnBottomRow.add("DeadEnd.E");
	}
	
	private void fillAllowedOnFrontRow(){
		allowedOnFrontRow = new ArrayList<String>();
		allowedOnFrontRow.add("Straight.N");
//		allowedOnFrontRow.add("Straight.S");
		allowedOnFrontRow.add("Corner.N");
		allowedOnFrontRow.add("Corner.W");
		allowedOnFrontRow.add("T.W");
//		allowedOnFrontRow.add("DeadEnd.S");
//		allowedOnFrontRow.add("DeadEnd.N");
//		allowedOnFrontRow.add("DeadEnd.E");
	}
	
	private void fillAllowedOnRightRow(){
		allowedOnRightRow = new ArrayList<String>();
		allowedOnRightRow.add("Straight.N");
//		allowedOnRightRow.add("Straight.S");
		allowedOnRightRow.add("Corner.S");
		allowedOnRightRow.add("Corner.E");
		allowedOnRightRow.add("T.E");
//		allowedOnRightRow.add("DeadEnd.S");
//		allowedOnRightRow.add("DeadEnd.N");
//		allowedOnRightRow.add("DeadEnd.E");
	}
	
	private void fillOrientation(){
		orientation = new ArrayList<String>();
		orientation.add("N");
		orientation.add("S");
		orientation.add("E");
		orientation.add("W");
	}
	
	
	
}
