package gui;

import javax.swing.JPanel;
import jcckit.*;
import jcckit.plot.Plot;
public class SensorGraphsPanel extends JPanel{
	/**
	 * Java requested to do this
	 */
	private static final long serialVersionUID = -829408870867430651L;
	//The current number of plots in this panel.
	private int numberOfPlots=0;
	
	public void addPoint(int plotNumber, int xDifference, int y, int Value){
		//TODO
	} 
	private int addPlot(int xWidth, int minY, int maxY){
		int plotIndex=numberOfPlots++;
		Plot plot=new Plot(null);
		return plotIndex;
	}
}
