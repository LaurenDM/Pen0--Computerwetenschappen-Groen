package gui;


import java.util.List;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SensorGraphsPanel extends JPanel{
	public static final int LIGHTPLOT=0;
	public static final int DISTANCEPLOT=1;
	// Eclipse requested to use the serialVersionUID
	private static final long serialVersionUID = -829408870867430651L;
	
	//The current number of plots in this panel.
	private int numberOfPlots=0;
	private List<PlotJPanel> plotList= new ArrayList<PlotJPanel>();
	
	
	public SensorGraphsPanel(){
    	this.setSize(300, 350);
		addPlot(1, 0.5, 0, 0, 50, -200, 200, "Light Value");
		addPlot(1, 0.5, 0, 0.5, 50, 0, 257, "Distance"); 
	}
	
	public void addValue(int plotNumber,double value){
		plotList.get(plotNumber).addValue(value);
	} 
	
	/**
	 * This method is now private so that no graphs are added at runtime because we don't want to test the consequences of that right now.
	 * In the future however, this method could become public to allow adding a plot @ runtime
	 * This method can be used to add a new plot.
	 * @param xPortion=a number between 0 and 1 that decides how big width of the plot is in comparison to the width of the whole SensorGraphsPanel
	 * @param yPortion= analog to x Portion but for height
	 * @param specialXCo= this is a number between 0 and 1 that controls where the plot is place horizontally in comparison to the whole SensorGraphsPanel. eg. 0.5 means this plot will start in the middle horizontically
	 * @param specialYCo= analog to x Portion but for vertical position
	 * @param noValues= the maximum number of values that will be shown on the plot
	 * @param minY=the minimal Y value that will be shown
	 * @param maxY=the maximal Y value that will be shown
	 * @return the index number of the plot
	 */
	private PlotJPanel addPlot(double xPortion, double yPortion, double specialXCo, double specialYCo, int noValues, int minY, int maxY, String plotname){
		int plotIndex=numberOfPlots++;
		PlotJPanel newPlotJPanel=new PlotJPanel(noValues, minY, maxY, plotname);
		int 	completePanelXsize=this.getWidth(), 
				completePanelYsize=this.getHeight();
		int xsize=(int) (completePanelXsize*xPortion),
			ysize=(int) (completePanelYsize*yPortion),
			xco=(int) (completePanelXsize*specialXCo),
			yco=(int) (completePanelYsize*specialYCo);
		newPlotJPanel.fixPanelLayout(xsize, ysize, xco, yco);
		this.add(newPlotJPanel);
		plotList.add(newPlotJPanel);
		return newPlotJPanel;
		
	}
    public void fixPanelLayout(int xsize, int ysize, int xco, int yco){
    	this.setLayout(null);
    	this.setLocation(xco, yco);
    	this.setSize(xsize, ysize);
    }

}
