package domain.robotFunctions;

import java.util.ArrayList;

public class MatchMap {
	private String[][] resultMap = new String[10][10]; 
	private String[][] transpose = new String[10][10]; 
	private String[][] horizontalPermutation = new String[10][10]; 
	private String[][] verticalPermutation = new String[10][10]; 
	private String[][] original = new String[10][10];
	
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
	public String[][] getTranspose() {
		return transpose;
	}
	/**
	 * Method that gets the horizontalPermutation
	 *
	 * @return the horizontalPermutation
	 */
	public String[][] getHorizontalPermutation() {
		return horizontalPermutation;
	}
	/**
	 * Method that gets the verticalPermutation
	 *
	 * @return the verticalPermutation
	 */
	public String[][] getVerticalPermutation() {
		return verticalPermutation;
	}
	/**
	 * Method that gets the original
	 *
	 * @return the original
	 */
	public String[][] getOriginal() {
		return original;
	}
	/**
	 * Method that sets the resultMap
	 *
	 * @param resultMap the resultMap to set
	 */
	public void setResultMap(String[][] resultMap) {
		this.resultMap = resultMap;
	}
	/**
	 * Method that sets the transpose
	 *
	 * @param transpose the transpose to set
	 */
	public void setTranspose(String[][] transpose) {
		this.transpose = transpose;
	}
	/**
	 * Method that sets the horizontalPermutation
	 *
	 * @param horizontalPermutation the horizontalPermutation to set
	 */
	public void setHorizontalPermutation(String[][] horizontalPermutation) {
		this.horizontalPermutation = horizontalPermutation;
	}
	/**
	 * Method that sets the verticalPermutation
	 *
	 * @param verticalPermutation the verticalPermutation to set
	 */
	public void setVerticalPermutation(String[][] verticalPermutation) {
		this.verticalPermutation = verticalPermutation;
	}
	/**
	 * Method that sets the original
	 *
	 * @param original the original to set
	 */
	public void setOriginal(String[][] original) {
		this.original = original;
	}
	/**
	 * Method that sets the original
	 *
	 * @param original the original to set
	 */
	public void setOriginal(ArrayList<String> list) {
		//TODO size need to be determined
		String[][] originalList = new String[list.size()][list.size()];
		for (int i = 0; i < originalList.length; i++) {
			for (int j = 0; j < originalList.length; j++) {
				originalList[i][j] = "";
			}
		}
		for (String string : list) {
			String delims = ".";
			String[] tokens = string.split(delims);
			originalList[Integer.parseInt(tokens[1])][Integer.parseInt(tokens[2])] = tokens[0];
		}
		this.original = originalList;
		setTranspose(getFirstPermutation(getOriginal()));
		setVerticalPermutation(verticalPerm(getOriginal()));
		setHorizontalPermutation(horizontalPerm(getOriginal()));
		
	}
	private String[][] horizontalPerm(String[][] original2) {
		int horizon = original2[1].length - 1;
		String[][] permList = new String[original2[0].length][original2[1].length];
		for (int i = 0; i < original2[0].length; i++) {
			for (int j = 0; j < original2[1].length; j++) {
				permList[i][horizon - j] = original2[i][j];
			} 
		}
		return permList;
	}
	private String[][] verticalPerm(String[][] original2) {
		int verti = original2[0].length - 1;
		String[][] permList = new String[original2[0].length][original2[1].length];
		for (int i = 0; i < original2[0].length; i++) {
			for (int j = 0; j < original2[1].length; j++) {
				permList[verti - i][j] = original2[i][j];
			} 
		}
		return permList;
	}
	private String[][] getFirstPermutation(String[][] original2) {
		String[][] permList = new String[original2[0].length][original2[1].length];
		for (int i = 0; i < original2[0].length; i++) {
			for (int j = 0; j < original2[1].length; j++) {
				permList[j][i] = original2[i][j];
			}
		}
		return permList;
	}
	
	
	

	
	
}
