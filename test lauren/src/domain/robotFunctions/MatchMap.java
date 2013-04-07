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
		Tile _tile7 = new Tile(0, 2, "Straight.N");
		Tile _tile3 = new Tile(1, 1, "Straight.E.4");
		Tile _tile4 = new Tile(2, 1, "Seesaw.W");
		Tile _tile5 = new Tile(3, 1, "Seesaw.E");
		Tile _tile6 = new Tile(4, 1, "Straight.E.3");
		Tile _tile8 = new Tile(1, 2, "DeadEnd.S");
		Tile _tile9 = new Tile(4, 2, "DeadEnd.S");
		Tile _tile10 = new Tile(4, 0, "Test.N");
		Tile[] _tileList = new Tile[10];
		_tileList[0] = _tile1;
		_tileList[1] = _tile2;
		_tileList[2] = _tile3;
		_tileList[3] = _tile4;
		_tileList[4] = _tile5;
		_tileList[5] = _tile6;
		_tileList[6] = _tile7;
		_tileList[7] = _tile8;
		_tileList[8] = _tile9;
		_tileList[9] = _tile10;
		setOurMazeTiles(tileList);
		setOriginalTiles(_tileList);
		System.out.println(matrixToString(getOurMaze()));
		System.out.println(matrixToString(getOriginal()));
		merge();
		System.out.println(getPermutatedDirection());
		System.out.println(getBasicVector()[0]);
		System.out.println(getBasicVector()[1]);
    }
	public final static String dummyString = "dummy     ";
	
	public enum Permutation {
		ORIGINAL,DEGREES_90,DEGREES_180,DEGREES_270;
		
	}

	
	private final static String regex = "[.]";
	
	private static Permutation permutatedDirection = null;
	private static int ourStartMergeX = 0;
	private static int ourStartMergeY = 0;
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
	
	private static String[][] resultMap;
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
	public static void setResultMap(String[][] resultMap1) {
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
				originalList[i][j] = dummyString;
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
		
		
		
		
		if(false){//hasMutualSeeSaw()){
			permutationBySeeSaw();
			mergeMaps(mergeMaps(getPermutatedDirection()));
		}
		else if(hasMutualBarcode()){
			permutationByBarcode();
			mergeMaps(mergeMaps(getPermutatedDirection()));
		}
		else 
			throw new IllegalArgumentException("There was nothing base found to merge on");
		//FOLLOWED BY MERGING THE SQUARES AND MAKING UP WHERE OUR TEAMMATE IS
		mergeMaps(getPermutatedDirection());
	}
	private static void mergeMaps(String[][] mergeMaps) {
		String[][] originList = getOurMaze();
		String[][] permList = mergeMaps;
		System.out.println(matrixToString(mergeMaps));
		String[][] resultList = new String[originList.length * 2][originList[0].length * 2];
		for (int i = 0; i < resultList.length; i++) {
			for (int j = 0; j < resultList[0].length; j++) {
				resultList[i][j] = dummyString;
			}
		}
		
		//Fills the grid with our found elements 
		for (int i = 0; i < resultList.length; i++) {
			for (int j = 0; j < resultList[0].length; j++) {
				try {
					resultList[i][j] = originList[i][j];
				} catch (Exception e) {}
			}
		}
		//System.out.println(matrixToString(resultList));
		resultList = mergeRightUp(resultList,permList);
		System.out.println("_________________________________");
		System.out.println("_________________________________");
		System.out.println("mergeRightUP");
		System.out.println(matrixToString(resultList));
		resultList = mergeLeftUp(resultList, permList);
		System.out.println("_________________________________");
		System.out.println("_________________________________");
		System.out.println("mergeLeftUp");
		System.out.println(matrixToString(resultList));
		resultList = mergeRightDown(resultList,permList);
		System.out.println("_________________________________");
		System.out.println("_________________________________");
		System.out.println("mergeRightDown");
		System.out.println(matrixToString(resultList));
		resultList = mergeLeftDown(resultList,permList);
		System.out.println("_________________________________");
		System.out.println("_________________________________");
		System.out.println("mergeLeftDown");
		System.out.println(matrixToString(resultList));
		System.out.println("_________________________________");
		System.out.println("_________________________________");
		System.out.println("resultstring");
		System.out.println(matrixToString(resultList));
		setResultMap(resultList);
	}
	
	private static String[][] mergeLeftDown(String[][] resultList, String[][] permList) {
		int permX = otherMergePointX;
		int permY = otherMergePointY;
		int i = ourMergePointX;
		int j = ourMergePointY;
		while(i >= 0){
			while (j >= 0){
				System.out.println("tested x :"+i+ "  tested y : "+j);
				try {
					System.out.println("adjusted");
					resultList[i][j] = permList[permX][permY];
				} catch (Exception e) {}
				permY--;
				j--;
				if(j < 0 && permY >= 0 && permX >= 0 ){
					System.out.println("enlarging y");
					int y = resultList[0].length;
					resultList = enlargeResultList(resultList,false,true);
					permX = otherMergePointX -1;
					permY = otherMergePointY;
					j = y;
					i = ourMergePointX;
				}
			}
			j = ourMergePointY;
			permY = otherMergePointY;
			permX--;
			if(i < 0 && permY >= 0 && permX >= 0){					
				resultList = enlargeResultList(resultList, true, true);
			}
			i--;
			if(i < 0 && permY >= 0 && permX >= 0){
				
			}
		}
		
//		for (int i = ourMergePointX; i < resultList.length; i--) {
//			for (int j = ourMergePointY; j < resultList[0].length; j--) {
//				try {
//					resultList[i][j] = permList[permX][permY];
//				} catch (Exception e) {}
//				
//				permY--;
//			}
//			permX--;
//		}
		return resultList;
	}
	
	private static String[][] mergeRightDown(String[][] resultList, String[][] permList) {
		int permX = otherMergePointX;
		int permY = otherMergePointY;
		for (int i = ourMergePointX; i < resultList.length; i++) {
			int j = ourMergePointY;
			while(j >= 0){ 
				try {
					resultList[i][j] = permList[permX][permY];
				} catch (Exception e) {}
				permY--;
				j--;
				if(j < 0 && permY >= 0 && permX >= 0 ){
					System.out.println("enlarging y");
					int y = resultList[0].length;
					resultList = enlargeResultList(resultList,false,true);
					permX = otherMergePointX -1;
					permY = otherMergePointY;
					j = y;
					i = ourMergePointX;
				}
			}
			permY = otherMergePointY;
			permX++;
			if(i > resultList.length){
				resultList = enlargeResultList(resultList, true, true);
			}
		}
		return resultList;
	}
	
	private static String[][] mergeLeftUp(String[][] resultList, String[][] permList) {
		int permX = otherMergePointX;
		int permY = otherMergePointY;
		int i = ourMergePointX;
		while(i >= 0){
			for (int j = ourMergePointY; j < permList[0].length; j++) {
				try {
					resultList[i][j] = permList[permX][permY];
				} catch (Exception e) {}
				permY++;
				if(permY >= resultList[0].length){
					resultList = enlargeResultList(resultList,false,true);
				}
			}
			i--;
			permY = otherMergePointY;
			permX--;
			
			if(i < 0 && permX >= 0 && permY >= 0){
				System.out.println("called2");
				int size = resultList.length;
				//resultList = enlargeResultList(resultList, true, false);
				resultList = enlargeResultList(resultList, true, false);
				System.out.println(matrixToString(resultList));
				i = size;
				permX = otherMergePointX;
				permY = otherMergePointY;
			}
			
		}
		return resultList;
	}
		
	
	private static String[][] mergeRightUp(String[][] resultList, String[][] permList){
		System.out.println(otherMergePointX);
		System.out.println(otherMergePointY);
		System.out.println("ours");
		System.out.println(ourMergePointX);
		System.out.println(ourMergePointY);
		int permX = otherMergePointX;
		int permY = otherMergePointY;
		for (int i = ourMergePointX; i < permList.length; i++) {
			for (int j = ourMergePointY; j < permList[0].length; j++) {
				if(j >= resultList[0].length){
					System.out.println("enlarged matrix y");
					resultList = enlargeResultList(resultList,false,true);
				}
				if(i > resultList.length){
					System.out.println("enlarged matrix x");
					resultList = enlargeResultList(resultList, true, true);
				}
				try {
					if(resultList[i][j].equals(dummyString) && ! permList[permX][permY].equals(dummyString))
					resultList[i][j] = permList[permX][permY];
				} catch (Exception e) {
					System.out.println("catched on x "+i+"   y "+j);
				}
				permY++;
			}
			permY = otherMergePointY;
			permX++;
		}
		System.out.println(matrixToString(resultList));
		return resultList;
	}
	
	//xOrY -> true = expand x size
	//downOrUp -> true = positively
	private static String[][] enlargeResultList(String[][] resultList,boolean xOrY,boolean downOrUp) {
		//Enlarge on x size 
		//Positively
		if(xOrY == true && downOrUp == true){
			String[][] newList = new String[resultList.length * 2][resultList[0].length];
			
			for (int i = 0; i < newList.length; i++) {
				for (int j = 0; j < newList[0].length; j++) {
					try {
						newList[i][j] = resultList[i][j];
					} catch (Exception e) {
						newList[i][j] = dummyString;
					}
					
				}
			}
			return newList;
		}
		//Enlarge on x size
		//Negatively
		else if(xOrY == true && downOrUp == false){
			String[][] newList = new String[resultList.length * 2][resultList[0].length];
			for (int i = 0; i < newList.length; i++) {
				for (int j = 0; j < newList[0].length; j++) {
					newList[i][j] = dummyString;
				}
			}
			int startX = resultList.length;
			int startY = 0;
			for (int i = 0; i < resultList.length; i++) {
				for (int j = 0; j < resultList[0].length; j++) {
					
					String string = resultList[i][j];
					newList[startX][startY] = string;
					startY++;
				}
				startY = 0;
				startX++;
			}
			ourMergePointX += resultList.length;
			return newList;
		}
		//Enlarge on Y size
		//Positively
		else if(xOrY == false && downOrUp == true){
			String[][] newList = new String[resultList.length][resultList[0].length * 2];
			for (int i = 0; i < newList.length; i++) {
				for (int j = 0; j < newList[0].length; j++) {
					try {
						newList[i][j] = resultList[i][j];
					} catch (Exception e) {
						newList[i][j] = dummyString;
					}
					
				}
			}
			return newList;
		}
		//Enlarge on Y size
		//Negativly
		else if(xOrY == false && downOrUp == false){
			String[][] newList = new String[resultList.length][resultList[0].length * 2];
			for (int i = 0; i < newList.length; i++) {
				for (int j = resultList[0].length; j < newList[0].length; j++) {
					try {
						newList[i][j] = resultList[i][j];
					} catch (Exception e) {
						newList[i][j] = dummyString;
					}
				}
			}
			ourMergePointY += resultList.length;
			return newList;
		}
		
		return null;
		
	}

	private static String[][] mergeMaps(Permutation permutatedDirection) {
		System.out.println(permutatedDirection);
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
	
	private static void permutationByBarcode() {
		for (int i = 0; i < getOurMaze().length; i++) {
			for (int j = 0; j < getOurMaze()[0].length; j++) {
				String[] str = getOurMaze()[i][j].split(regex);
				if(getBarcodeValue(getOurMaze()[i][j]) > 0);
					if(checkForBarcodeMatches(str[1], getBarcodeValue(getOurMaze()[i][j]),i,j)){
						System.out.println("MERGEPOINT HAS BEEN SET");
						ourMergePointX = i;
						ourMergePointY = j;
						ourStartMergeX = i;
						ourStartMergeY = j;
						j = getOurMaze()[0].length;
						i = getOurMaze().length;
					}
			}
		}
	}
	
	private static boolean checkForBarcodeMatches(String direction,int number,int originalX,int originalY){
		System.out.println("______");
		for (Permutation dir : Permutation.values()) {
			switch (dir) {
				case DEGREES_180:
					System.out.println("180");
					if(checkForBarcodePermutation(get180Degree(),direction,number,originalX,originalY)){
						System.out.println("test");
						setPermutatedDirection(Permutation.DEGREES_180);
						return true;
					}
					break;
				case DEGREES_270:
					System.out.println("270");
					if(checkForBarcodePermutation(get270Degree(),direction,number,originalX,originalY)){
						System.out.println("test");
						setPermutatedDirection(Permutation.DEGREES_270);
						return true;
					}
					break;
				case DEGREES_90:
					System.out.println("90");
					System.out.println(matrixToString(get90Degree()));
					if(checkForBarcodePermutation(get90Degree(),direction,number,originalX,originalY)){
						System.out.println("test");
						setPermutatedDirection(Permutation.DEGREES_90);
						return true;
					}
					break;
				case ORIGINAL:
					System.out.println("ori");
					if(checkForBarcodePermutation(getOriginal(),direction,number,originalX,originalY)){
						System.out.println("test");
						setPermutatedDirection(Permutation.ORIGINAL);
						return true;
					}
					break;
				default:
					System.out.println("none called");
					break;
			}
		}
		return false;
	}
	
	private static boolean checkForBarcodePermutation(String[][] maze,String direction, int number,int originalX,int originalY) {
		
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				//String[] str = maze[i][j].split(regex);
				try {
					if (getBarcodeValue(maze[i][j]) == number && checkBarcodeSurrounding(maze,originalX,originalY,i,j)) {
						System.out.println("equality found on");
						System.out.println(matrixToString(maze));
						System.out.println(i);
						System.out.println(j);
						otherMergePointX = i;
						otherMergePointY = j;
						return true;
					}
				} catch (Exception e) {}
			}
		}
		return false;
	}
	private static boolean checkBarcodeSurrounding(String[][] maze,int originalX,int originalY, int permX, int permY) {
		int counter = 0;
		System.out.println("______start test ________");
		System.out.println(maze[permX][permY]);
		System.out.println(matrixToString(getOurMaze()));
		System.out.println("_________");
		System.out.println(matrixToString(maze));
		System.out.println("_________");
		System.out.println("orix : "+originalX+"   oriY: "+originalY);
		System.out.println("checkX : "+permX+"  checkY :"+permY);
		counter+=checkBarcodeEquality(maze,originalX+1,originalY,permX+1,permY);
		counter+=checkBarcodeEquality(maze,originalX,originalY+1,permX,permY+1);
		counter+=checkBarcodeEquality(maze,originalX-1,originalY,permX-1,permY);
		counter+=checkBarcodeEquality(maze,originalX,originalY-1,permX,permY-1);
		counter+=checkBarcodeEquality(maze,originalX-1,originalY-1,permX-1,permY-1);
		counter+=checkBarcodeEquality(maze,originalX+1,originalY+1,permX+1,permY+1);
		counter+=checkBarcodeEquality(maze,originalX+1,originalY-1,permX+1,permY-1);
		counter+=checkBarcodeEquality(maze,originalX-1,originalY+1,permX-1,permY+1);	
		System.out.println("counter:"+counter);
		System.out.println("__________end test ________");
		if(counter > 11)
			return true;
		return false;
		
	}
	
	private static int checkBarcodeEquality(String[][] maze,int originalX,int originalY, int permX, int permY){
		System.out.println("_____try2_____");
		System.out.println("orix : "+originalX+"   oriY: "+originalY);
		System.out.println("checkX : "+permX+"  checkY :"+permY);
		try {
			String[] str = getOurMaze()[originalX][originalY].split(regex);
			String[] str2 = maze[permX][permY].split(regex);
			System.out.println("string 1 : "+str[0]+"    string 2 : "+str2[0]);
			if(str[0].equals(str2[0]) && isEqualOrientation(str[1], str2[1]))
				return 5;
			else
				return 0;
		} catch (Exception e) {
			return 1;
		}
	}
	
	private static boolean isEqualOrientation(String or1, String or2){
		if(or1.equals(or2) || Orientation.getOrientation(or1).getRight().getRight().toString().substring(0, 1) == or2){
			return true;
		}
		return false;
	}
	//SEESAW PERMUTATION CALCULATOR
	private static void permutationBySeeSaw() {
		for (int i = 0; i < getOurMaze().length; i++) {
			for (int j = 0; j < getOurMaze()[0].length; j++) {
				String[] str = getOurMaze()[i][j].split(regex);
				if(str[0].equals("Seesaw")){
					if(checkForSeesawMatches(str[1],getSeeSawNumber(getOurMaze(), i, j, str[1]))){
						ourMergePointX = i;
						ourMergePointY = j;
						ourStartMergeX = i;
						ourStartMergeY = j;
						//TODO more checkers if needed
						j = getOurMaze()[0].length;
						i = getOurMaze().length;
					}
				}
			}
		}
	}
	private static boolean checkForSeesawMatches(String string, int barcodeNumber) {
		for (Permutation dir : Permutation.values()) {
			switch (dir) {
				case DEGREES_180:
					if(checkForSeesawPermutation(get180Degree(),string,barcodeNumber)){
						setPermutatedDirection(Permutation.DEGREES_180);
						return true;
					}
					break;
				case DEGREES_270:
					if(checkForSeesawPermutation(get270Degree(),string,barcodeNumber)){
						setPermutatedDirection(Permutation.DEGREES_270);
						return true;
					}
					break;
				case DEGREES_90:
					if(checkForSeesawPermutation(get90Degree(),string,barcodeNumber)){
						setPermutatedDirection(Permutation.DEGREES_90);
						return true;
					}
					break;
				case ORIGINAL:
					if(checkForSeesawPermutation(getOriginal(),string,barcodeNumber)){
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
	
	private static boolean checkForSeesawPermutation(String[][] maze, String string, int barcodeNumber) {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				String[] str = maze[i][j].split(regex);
				if (str[0].equals("Seesaw") && str[1].equals(string)){
					if(getSeeSawNumber(maze, i, j, str[1]) == barcodeNumber) {
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
	
	private static int[] getBasicVector(){
		
		System.out.println("______________________________________________");
		System.out.println("______________________________________________");
		System.out.println("______________________________________________");
		int[] vector = new int[2];
		int ourX = ourStartMergeX;
		int ourY = ourStartMergeY;
		int otherX = otherMergePointX;
		int otherY = otherMergePointY;
		System.out.println(ourMergePointX+"  "+ourMergePointY+"   "+otherMergePointX +"   "+otherMergePointY+"  ");
		Enum<Permutation> perm = getPermutatedDirection();
		if(perm == Permutation.ORIGINAL){
			vector[0] = otherX - ourX;
			vector[1] = otherY - ourY;
			return vector;
		}
		else if(perm == Permutation.DEGREES_90){
			vector[0] = otherX - ourX;
			vector[1] = getOriginal()[0].length - otherMergePointY - ourY;
			return vector;
		}
		else if(perm == Permutation.DEGREES_180){
			vector[0] = getOriginal().length - otherMergePointX - ourX;
			vector[1] = getOriginal()[0].length - otherMergePointY - ourY;
			return vector;
		}
		else if(perm == Permutation.DEGREES_270){
			vector[0] = getOriginal().length - otherMergePointX - ourX;
			vector[1] = otherY - ourY;
			return vector;
		}
		else {
			return null;
		}
		
			
		
	}
	//___________________________________________
	//END BASIC UTILS
	//___________________________________________
	
}
