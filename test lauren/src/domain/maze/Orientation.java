package domain.maze;

import java.util.ArrayList;

public enum Orientation {
	   NORTH, EAST, SOUTH, WEST;
	   
	   public static Orientation getOrientation(String str){
		   if(str.equals("N")){
			   return NORTH;
		   }
		   else if(str.equals("S")){
			   return SOUTH;
		   }
		   else if(str.equals("E")){
			   return EAST;
		   }
		   else if(str.equals("W")){
			   return WEST;
		   }
		   else{
			   System.out.println("Error parsing orientation");
			   return NORTH;
		   }
		
		}
	   
	   public Orientation getLeft(){
		   return this.getOffset(-1);
	   }
	   
	   public Orientation getRight(){
		   return this.getOffset(1);
	   }
	   
	   public Orientation getBack(){
		   return this.getOffset(-2);
	   }
	   
	   private Orientation getOffset(int offset){
		   if(Math.abs(offset)>2) {
			   throw new IllegalArgumentException("Cannot return the orientation at this offset.");
		   } else {
			   ArrayList<Orientation> orderedList = new ArrayList<Orientation>();
			   int i = 0;
			   for(Orientation o : orderedArray){
				   orderedList.add(o);
				   if((offset <= 0) && orderedList.contains(this) && (orderedList.size()+offset>=0)){
					   return orderedList.get(i+offset);
				   }
				   i++;
			   }
			   return orderedList.get(orderedList.indexOf(this)+offset);
		   }
	   }
	   
	   private static final Orientation[] orderedArray = {NORTH, EAST, SOUTH, WEST, NORTH, EAST};
	   
	   
	}

	