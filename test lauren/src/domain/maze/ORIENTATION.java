package domain.maze;

public enum ORIENTATION {
	   NORTH, EAST, SOUTH, WEST;
	   
	   public static ORIENTATION getOrientation(String str){
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
	   
	}

	