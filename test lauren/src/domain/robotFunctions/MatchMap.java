package domain.robotFunctions;
import java.util.ArrayList;
import domain.maze.*;
import peno.htttp.Tile;

public class MatchMap {
	
	public static void main(String[] args) {
		Tile tile1 = new Tile(0, 0, "Straight.N.3");
		Tile tile2 = new Tile(0, 1, "Seesaw.S");
		Tile tile3 = new Tile(0, 2, "Seesaw.N");
		Tile tile4 = new Tile(0, 3, "Straight.N.4");
		Tile tile5 = new Tile(0, 4, "Cross.N");
		Tile[] tileList = new Tile[5];
		tileList[0] = tile1;
		tileList[1] = tile2;
		tileList[2] = tile3;
		tileList[3] = tile4;
		tileList[4] = tile5;
		
		Tile _tile1 = new Tile(0, 0, "Straight.N");
		Tile _tile2 = new Tile(0, 1, "Cross.N");
		Tile _tile3 = new Tile(1, 1, "Straight.E.4");
		Tile _tile4 = new Tile(2, 1, "Seesaw.W");
		Tile _tile5 = new Tile(3, 1, "Seesaw.E");
		Tile _tile6 = new Tile(4, 1, "Straight.N.4");
		Tile[] _tileList = new Tile[6];
		_tileList[0] = _tile1;
		_tileList[1] = _tile2;
		_tileList[2] = _tile3;
		_tileList[3] = _tile4;
		_tileList[4] = _tile5;
		_tileList[5] = _tile6;
		setOurMazeTiles(tileList);
		setOriginalTiles(_tileList);
		merge();
		
    }
	
	public enum Permutation {
		ORIGINAL,DEGREES_90,DEGREES_180,DEGREES_270;
		
	}

	
	private final static String regex = "[.]";
	
	private static Permutation permutatedDirection = null;
	private static int ourMergePointX = 0;
	private static int ourMergePointY = 0;
	private static int otherMergePointX = 0;
	private static int otherMergePointY = 0;
	
	private static ArrayList<Integer> ourBarcodes = new ArrayList<Integer>();
	
	private static void setOurBarcodes(){
		String[][] Str = getOurMaze();
		for (int i = 0; i < Str.length; i++) {
			for (int j = 0; j < Str[0].length; j++) {
				String[] string = Str[i][j].split(regex);
				if(string[0].equals("Seesaw")){
					ourBarcodes.add(getSeeSawNumber(Str, i, j, string[1]));
				}
			}
		}
	}
	
	private static boolean ourHasGivenBarcode(int barcode){
		return ourBarcodes.contains(barcode);
	}
	
	/**
	 * Method that gets the getDirection
	 *
	 * @return the getDirection
	 */
	public static Permutation getPermutatedDirection() {
		return permutatedDirection;
	}
	/**
	 * Method that sets the getDirection
	 *
	 * @param getDirection the getDirection to set
	 */
	public static void setPermutatedDirection(Permutation getDirection) {
		permutatedDirection = getDirection;
	}
	
	private String[][] resultMap;
	/**
	 * Method that gets the ourMaze
	 *
	 * @return the ourMaze
	 */
	public static String[][] getOurMaze() {
		return ourMaze;
	}
	/**
	 * Method that sets the ourMaze
	 *
	 * @param ourMaze the ourMaze to set
	 */
	public static void setOurMaze(String[][] newMaze) {
		ourMaze = newMaze;
		setOurBarcodes();
	}
	public static void setOurMazeTiles(Tile[] tiles){
		int maxX = 0;
		int maxY = 0;
		for (Tile tile : tiles) {
			if(tile.getX() > maxX){
				maxX = (int) tile.getX();
			}
			if(tile.getY() > maxY){
				maxY = (int) tile.getY();
			}
		}
		maxX++;
		maxY++;
		String[][] originalList = new String[maxX][maxY];
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				originalList[i][j] = " ";
			}
		}
		for (Tile tile : tiles) {
			originalList[(int) tile.getX()][(int) tile.getY()] = tile.getToken();
		}
		setOurMaze(originalList);
	}
	private static String[][] original;  	//0
	private static String[][] degrees_90; 	//1
	private static String[][] degrees_180; //2
	private static String[][] degrees_270; //3
		
	private static String[][] ourMaze;
	
	
	/**
	 * Method that gets the resultMap
	 *
	 * @return the resultMap
	 */
	public String[][] getResultMap() {
		return resultMap;
	}
	/**
	 * Method that gets the transpose
	 *
	 * @return the transpose
	 */
	public static String[][] get90Degree() {
		return degrees_90;
	}
	/**
	 * Method that gets the horizontalPermutation
	 *
	 * @return the horizontalPermutation
	 */
	public static String[][] get180Degree() {
		return degrees_180;
	}
	/**
	 * Method that gets the verticalPermutation
	 *
	 * @return the verticalPermutation
	 */
	public static String[][] get270Degree() {
		return degrees_270;
	}
	/**
	 * Method that gets the original
	 *
	 * @return the original
	 */
	public static String[][] getOriginal() {
		return original;
	}
	/**
	 * Method that sets the resultMap
	 *
	 * @param resultMap the resultMap to set
	 */
	public void setResultMap(String[][] resultMap1) {
		resultMap = resultMap1;
	}
	/**
	 * Method that sets the transpose
	 *
	 * @param transpose the transpose to set
	 */
	public static void set90Degree(String[][] transpose) {
		degrees_90 = transpose;
	}
	/**
	 * Method that sets the horizontalPermutation
	 *
	 * @param horizontalPermutation the horizontalPermutation to set
	 */
	public static void set180Degree(String[][] horizontalPermutation) {
		degrees_180 = horizontalPermutation;
	}
	/**
	 * Method that sets the verticalPermutation
	 *
	 * @param verticalPermutation the verticalPermutation to set
	 */
	public static void set270Degree(String[][] verticalPermutation) {
		degrees_270 = verticalPermutation;
	}
	/**
	 * Method that sets the original
	 *
	 * @param original the original to set
	 */
	public void setOriginal(String[][] original1) {
		original = original1;
	}
	
	//___________________________________________
	//SETS ALL THE PERMUTATIONS
	//___________________________________________
	public static void setOriginalTiles(Tile[] tiles) {
		int maxX = 0;
		int maxY = 0;
		for (Tile tile : tiles) {
			if(tile.getX() > maxX){
				maxX = (int) tile.getX();
			}
			if(tile.getY() > maxY){
				maxY = (int) tile.getY();
			}
		}
		maxX++;
		maxY++;
		String[][] originalList = new String[maxX][maxY];
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				originalList[i][j] = "          ";
			}
		}
		matrixToString(originalList);
		for (Tile tile : tiles) {
			originalList[(int) tile.getX()][(int) tile.getY()] = tile.getToken();
		}
		original = originalList;
		set90Degree(get90Permutation(getOriginal()));
		set270Degree(get270Permutation(getOriginal()));
		set180Degree(get180Permutation(getOriginal()));
		
	}
	private static String[][] get180Permutation(String[][] original2) {
		return get90Permutation(get90Permutation(original2));
	}
	private static String[][] get270Permutation(String[][] original2) {
		return get180Permutation(get90Permutation(original2));
	}
	private static String[][] get90Permutation(String[][] original) {
		int xLength = original.length;
		int yLength = original[0].length;
		String[][] permList = new String[yLength][xLength];
		for (int x = 0; x < xLength; x++) {
			for (int y = 0; y < yLength ; y++) {
				String[] str = original[x][y].split("[.]");
				if(str.length > 1){
					str[1] = Orientation.getOrientation(str[1]).getRight().toString().substring(0, 1);
				}
				permList[y][xLength - 1 - x] = mergeList(str);
			}
		}
		return permList;
	}
	
	private static String mergeList(String[] str) {
		String string = "";
		for (String newSt : str) {
			string += newSt+".";
		}
		return string.substring(0, string.length() - 1);
	}
	//___________________________________________
	//END SETS ALL THE PERMUTATIONS
	//___________________________________________
	
	//___________________________________________
	//MUTUAL CHECKERS
	//___________________________________________
	public static String matrixToString(String[][] string){
		String printout = "";
		for (int i = string[0].length - 1; i >= 0; i--) {
			printout += "\n";
			for (int j = 0; j < string.length; j++) {
				printout += string[j][i] + "  		  ";
			}
		}
		return printout;
		
	}
	
	private static boolean hasMutualSeeSaw(){
		String[][] tmpList = getOriginal();
		if(hasMutualSeeSawPermutationChecker(tmpList))
			return true;
		return false;
	}

	private static boolean hasMutualSeeSawPermutationChecker(String[][] tmpList) {
		for (int i = 0; i < tmpList.length; i++) {
			for (int j = 0; j < tmpList[0].length; j++) {
				String[] string = tmpList[i][j].split(regex);
				if(string[0].equals("Seesaw")){
					int number = getSeeSawNumber(tmpList,i,j,string[1]);
					if(ourHasGivenBarcode(number)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	@SuppressWarnings("unused")
	private static boolean hasMutualBarcode(){
		String[][] tmpList = getOriginal();
		for (int i = 0; i < tmpList.length; i++) {
			for (int j = 0; j < tmpList[0].length; j++) {
				if(getBarcodeValue(tmpList[i][j]) != 0 && ourBarcodeNumbers().contains(getBarcodeValue(tmpList[i][j]))){
					return true;
				}
			}
		}
		return false;
	}
	
	//___________________________________________
	//END MUTUAL CHECKERS
	//___________________________________________
	
	//___________________________________________
	//OUR BARCODE/SEESAW CHECKERS
	//___________________________________________
	
	private static ArrayList<Integer> ourBarcodeNumbers(){
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < getOurMaze().length; i++) {
			for (int j = 0; j < getOurMaze()[0].length; j++) {
				if(getBarcodeValue(getOurMaze()[i][j]) != 0){
					numbers.add(getBarcodeValue(getOurMaze()[i][j]));
				}
			}
		}
 		return numbers;
	}
	
	//___________________________________________
	//END OUR BARCODE/SEESAW CHECKERS
	//___________________________________________
	
	//___________________________________________
	//THE MERGE METHODS
	//___________________________________________
	
	public static void merge(){
		//TODO debug
		if(hasMutualSeeSaw()){
			permutationBySeeSaw();
			mergeMapsSeesaw(mergeMaps(getPermutatedDirection()));
		}
//		else if(hasMutualBarcode()){
//			permutationByBarcode();
//			mergeMapsBarcode(mergeMaps(getPermutatedDirection()));
//		}
		else 
			throw new IllegalArgumentException("There was nothing base found to merge on");
		//FOLLOWED BY MERGING THE SQUARES AND MAKING UP WHERE OUR TEAMMATE IS
		mergeMaps(getPermutatedDirection());
	}
	
	@SuppressWarnings("unused")
	private static void mergeMapsBarcode(String[][] mergeMaps) {
		
		
	}
	private static void mergeMapsSeesaw(String[][] mergeMaps) {
		String[][] originList = getOurMaze();
		String[][] permList = mergeMaps;
		String[][] resultList = new String[originList.length * 2][originList[0].length * 2];
		for (int i = 0; i < resultList.length; i++) {
			for (int j = 0; j < resultList[0].length; j++) {
				resultList[i][j] = "dummy     ";
			}
		}
		
		//Fills the grid with our found elements 
		System.out.println("totalx : "+resultList.length+" totaly : "+resultList[0].length);
		for (int i = 0; i < resultList.length; i++) {
			for (int j = 0; j < resultList[0].length; j++) {
				System.out.println("x: "+i+"   y: "+j);
				try {
					resultList[i][j] = originList[i][j];
				} catch (Exception e) {}
			}
		}
		System.out.println(matrixToString(resultList));
		mergeRightUp(resultList,permList);
		mergeRightDown(resultList,permList);
		mergeLeftUp(resultList,permList);
		System.out.println(matrixToString(resultList));
		mergeLeftDown(resultList,permList);
	}
	
	private static void mergeLeftDown(String[][] resultList, String[][] permList) {
		int permX = otherMergePointX;
		int permY = otherMergePointY;
		for (int i = ourMergePointX; i < resultList.length; i--) {
			for (int j = ourMergePointY; j < resultList[0].length; j--) {
				try {
					resultList[i][j] = permList[permX][permY];
				} catch (Exception e) {}
				
				permY--;
			}
			permX--;
		}
		
	}
	private static void mergeLeftUp(String[][] resultList, String[][] permList) {
		int permX = otherMergePointX;
		int permY = otherMergePointY;
		for (int i = ourMergePointX; i < resultList.length; i--) {
			for (int j = ourMergePointY; j < resultList[0].length; j++) {
				try {
					resultList[i][j] = permList[permX][permY];
				} catch (Exception e) {}
				permY++;
			}
			permX--;
		}
		
	}
	private static void mergeRightDown(String[][] resultList, String[][] permList) {
		int permX = otherMergePointX;
		int permY = otherMergePointY;
		for (int i = ourMergePointX; i < resultList.length; i++) {
			for (int j = ourMergePointY; j >= 0 ; j--) {
				try {
					resultList[i][j] = permList[permX][permY];
				} catch (Exception e) {}
				permY--;
			}
			permX++;
		}
		
	}
	private static void mergeRightUp(String[][] resultList, String[][] permList){
		int permX = otherMergePointX;
		int permY = otherMergePointY;
		for (int i = ourMergePointX; i < resultList.length; i++) {
			for (int j = ourMergePointY; j < resultList[0].length; j++) {
				try {
					resultList[i][j] = permList[permX][permY];
				} catch (Exception e) {}
				permY++;
			}
			permX++;
		}
	}
	
	private static String[][] mergeMaps(Permutation permutatedDirection) {
		switch (permutatedDirection) {
		case DEGREES_90:
			return get90Degree();
		case DEGREES_180:
			return get180Degree();
		case DEGREES_270:
			return get270Degree();
		case ORIGINAL:
			return getOriginal();
		default:
			return null;
		}
		
	}
	//___________________________________________
	// 				SUBPART	
	//PERMUTATION DIRECTION IS BEING CHECKED
	//___________________________________________
	
	@SuppressWarnings("unused")
	private static void permutationByBarcode() {
		for (int i = 0; i < getOurMaze().length; i++) {
			for (int j = 0; j < getOurMaze()[0].length; j++) {
				String[] str = getOurMaze()[i][j].split(regex);
				if(getBarcodeValue(getOurMaze()[i][j]) > 0);
					if(checkForBarcodeMatches(str[1], getBarcodeValue(getOurMaze()[i][j]),i,j)){
						ourMergePointX = i;
						ourMergePointY = j;
					}
			}
		}
	}
	
	private static boolean checkForBarcodeMatches(String direction,int number,int originalX,int originalY){
		for (Permutation dir : Permutation.values()) {
			switch (dir) {
				case DEGREES_180:
					if(checkForBarcodePermutation(get180Degree(),direction,number,originalX,originalY)){
						setPermutatedDirection(Permutation.DEGREES_180);
						return true;
					}
					break;
				case DEGREES_270:
					if(checkForBarcodePermutation(get270Degree(),direction,number,originalX,originalY)){
						setPermutatedDirection(Permutation.DEGREES_270);
						return true;
					}
					break;
				case DEGREES_90:
					if(checkForBarcodePermutation(get90Degree(),direction,number,originalX,originalY)){
						setPermutatedDirection(Permutation.DEGREES_90);
						return true;
					}
					break;
				case ORIGINAL:
					if(checkForBarcodePermutation(getOriginal(),direction,number,originalX,originalY)){
						setPermutatedDirection(Permutation.ORIGINAL);
						return true;
					}
					break;
				default:
					break;
			}
		}
		return false;
	}
	
	private static boolean checkForBarcodePermutation(String[][] maze,String direction, int number,int originalX,int originalY) {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				String[] str = maze[i][j].split(regex);
				//TODO this needs to be checked -> the directions that are being given.
				if (str[1].equals(direction) && getBarcodeValue(maze[i][j]) == number && checkBarcodeSurrounding(maze,originalX,originalY,i,j)) {
					otherMergePointX = i;
					otherMergePointY = j;
				}
			}
		}
		return false;
	}
	private static boolean checkBarcodeSurrounding(String[][] maze,int originalX,int originalY, int permX, int permY) {
		int counter = 0;
		if(checkBarcodeEquality(maze,originalX+1,originalY,permX+1,permY));
			counter++;
		if(checkBarcodeEquality(maze,originalX,originalY+1,permX,permY+1));
			counter++;
		if(checkBarcodeEquality(maze,originalX-1,originalY,permX-1,permY));
			counter++;
		if(checkBarcodeEquality(maze,originalX,originalY-1,permX,permY-1));
			counter++;
		if(checkBarcodeEquality(maze,originalX-1,originalY-1,permX-1,permY-1));
			counter++;
		if(checkBarcodeEquality(maze,originalX+1,originalY+1,permX+1,permY+1));
			counter++;
		if(checkBarcodeEquality(maze,originalX+1,originalY-1,permX+1,permY-1));
			counter++;			
		if(checkBarcodeEquality(maze,originalX-1,originalY+1,permX-1,permY+1));	
			counter++;
		if(counter > 5)
			return true;
		return false;
		
	}
	
	private static boolean checkBarcodeEquality(String[][] maze,int originalX,int originalY, int permX, int permY){
		String[] str = maze[originalX][originalY].split(regex);
		String[] str2 = maze[permX][permY].split(regex);
		if(str[0].equals(str2[0]) && str[1].equals(str2[1]))
			return true;
		else
			return false;
	}
	//SEESAW PERMUTATION CALCULATOR
	private static void permutationBySeeSaw() {
		for (int i = 0; i < getOurMaze().length; i++) {
			for (int j = 0; j < getOurMaze()[0].length; j++) {
				String[] str = getOurMaze()[i][j].split(regex);
				if(str[0].equals("Seesaw")){
					if(checkForSeesawMatches(str[1])){
						ourMergePointX = i;
						ourMergePointY = j;
					}
				}
			}
		}
	}
	private static boolean checkForSeesawMatches(String string) {
		for (Permutation dir : Permutation.values()) {
			switch (dir) {
				case DEGREES_180:
					if(checkForSeesawPermutation(get180Degree(),string)){
						setPermutatedDirection(Permutation.DEGREES_180);
						return true;
					}
					break;
				case DEGREES_270:
					if(checkForSeesawPermutation(get270Degree(),string)){
						setPermutatedDirection(Permutation.DEGREES_270);
						return true;
					}
					break;
				case DEGREES_90:
					if(checkForSeesawPermutation(get90Degree(),string)){
						setPermutatedDirection(Permutation.DEGREES_90);
						return true;
					}
					break;
				case ORIGINAL:
					if(checkForSeesawPermutation(getOriginal(),string)){
						setPermutatedDirection(Permutation.ORIGINAL);
						return true;
					}
					break;
				default:
					System.out.println("never called locally");
					return false;
				
			}
		}
		return false;
	}
	
	private static boolean checkForSeesawPermutation(String[][] maze, String string) {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				String[] str = maze[i][j].split(regex);
				if (str[0].equals("Seesaw")){
					if(ourHasGivenBarcode(getSeeSawNumber(maze, i, j, str[1]))) {
					otherMergePointX = i;
					otherMergePointY = j;
					return true;
				}
					
				}
			}
		}
		return false;
	}
	//___________________________________________
	// 			END	SUBPART	
	//PERMUTATION DIRECTION IS BEING CHECKED
	//___________________________________________
	
	//________________________________________________________________________
	//END THE MERGE METHODS
	//________________________________________________________________________
	
	//___________________________________________
	//BASIC UTILS
	//___________________________________________
	private static int getSeeSawNumber(String[][] ourMaze2, int i, int j,String string) {
		if(string.equals("N")){
			return getBarcodeValue(ourMaze2[i][j+1]);
		}
		else if(string.equals("E")){
			return getBarcodeValue(ourMaze2[i+1][j]);
		}
		else if(string.equals("S")){
			return getBarcodeValue(ourMaze2[i][j-1]);
		}
		else if(string.equals("W")){
			return getBarcodeValue(ourMaze2[i-1][j]);
		}
		return 0;
	}
	
	private static int getBarcodeValue(String string){
		String[] parsedString = string.split(regex);
		try {
			return Integer.parseInt(parsedString[2]);
		} catch (Exception e) {
			return 0;
		}
		
	}
	
	
}
