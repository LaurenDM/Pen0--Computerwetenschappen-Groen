package gui;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import jcckit.GraphicsPlotCanvas;
import jcckit.data.DataCurve;
import jcckit.data.DataPlot;
import jcckit.data.DataPoint;
import jcckit.plot.CartesianCoordinateSystem;
import jcckit.plot.SymbolFactory;
import jcckit.util.ConfigParameters;
import jcckit.util.PropertiesBasedConfigData;

public class PlotJPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -588344062075484174L;
	private double[] _data;
	  private DataPlot _dataPlot;
	private Properties _props;
	private GraphicsPlotCanvas _plotCanvas;
	private final int 		_noValues;
	private final double	_minY,
							_maxY;
	DataCurve _curve;
	
	public PlotJPanel(int noValues, double minY, double maxY){
		this._noValues=noValues;
		_data=new double[noValues];
		_minY=minY;
		_maxY=maxY;
	    _plotCanvas = createPlotCanvas();
	    _dataPlot = new DataPlot();
	    _dataPlot.addElement(new DataCurve(""));
	    _plotCanvas.connect(_dataPlot);
		_curve = new DataCurve("");
	    for (int i = 0; i < _data.length; i++) {
	      _curve.addElement(new DataPoint(i, _data[i]));
	    }
	    _dataPlot.replaceElementAt(0,_curve);	
	
	    	this.setBackground(Color.BLUE);
	    
	}
	  private void fixCanvasLayout(Canvas canvas, int xsize, int ysize, int xco, int yco){
	    	canvas.setLocation(xco, yco);
	    	canvas.setSize(xsize, ysize);
	    	this.add(canvas);
	    }
	  public void fixLayout(){
		  
	  }
	 
	  private GraphicsPlotCanvas createPlotCanvas() {
	   _props = new Properties();
	   SymbolFactory x;

	    ConfigParameters config
	        = new ConfigParameters(new PropertiesBasedConfigData(_props));
	    _props.put("plot/legendVisible", "false");// een rechts-kadertje bovenaan
	    _props.put("plot/coordinateSystem/xAxis/minimum", Double.toString(0));
	    _props.put("plot/coordinateSystem/xAxis/maximum",  Double.toString(_noValues));
//	    props.put("plot/coordinateSystem/xAxis/ticLabelFormat/className",
//	              "jcckit.plot.TicLabelMap");
//	    props.put("plot/coordinateSystem/xAxis/ticLabelFormat/map",
//	              "0=Mo;1=Tu;2=We;3=Th;4=Fr;5=Sa;6=Su");
	    _props.put("plot/coordinateSystem/yAxis/axisLabel", "value");
	    _props.put("plot/coordinateSystem/yAxis/maximum", Double.toString(_maxY));
	    _props.put("plot/coordinateSystem/yAxis/minimum", Double.toString(_minY));
	    _props.put("plot/coordinateSystem/yAxis/ticLabelFormat", "%d");//hoe de nummers op y afgebeeld worden, in dit geval als percentages
	    _props.put("plot/coordinateSystem/xAxis/ticLabelFormat", "%d");//hoe de nummers op x afgebeeld worden, in dit geval als percentages
	    _props.put("plot/curveFactory/definitions", "curve"); //Dit lijntje heb je nodig om verdere eigenschappen van de curve te kunnen bepalen die hieronder staan
	    _props.put("plot/curveFactory/curve/withLine", "true"); //Dppr dit true te zetten staat er altijd een lijn ook al zijn er ook staafjes
//	    _props.put("plot/curveFactory/curve/symbolFactory/className", 
//	              "jcckit.plot.BarFactory"); //Dit zorgt van de effectieve keuze voor staafjes ook mogelijk is CircleSymbolFactory en SquareSymbolFactory en ErrorBarFactory(geen idee wat dit precies is)
//	    _props.put("plot/curveFactory/curve/symbolFactory/attributes/className", 
//	              "jcckit.graphic.ShapeAttributes"); //Dit zorgt ervoor dat we de attributen van de staafjes kunnen aanpassen zoals hieronder gedaan word
//	    _props.put("plot/curveFactory/curve/symbolFactory/attributes/fillColor", 
//	              "0xfe8000"); //Kleur van de staafjes
//	    _props.put("plot/curveFactory/curve/symbolFactory/attributes/lineColor", 
//	              "1"); //lijn rond staafjes
//	    _props.put("plot/curveFactory/curve/symbolFactory/size", "0.005"); //dikte gekozen soort curve, van staafjes dus, niet van lijn
	    _props.put("plot/initialHintForNextCurve/className", 
	              "jcckit.plot.PositionHint");
	    _props.put("plot/initialHintForNextCurve/position", "0 0.1"); //bepaalt positie begin staafjes: "iets te maken met x iets te maken met y", "0 0.1" is juiste

	  GraphicsPlotCanvas newPlotCanvas= new GraphicsPlotCanvas(config);
	    
	    return newPlotCanvas;
	  }


	  
		public void fixCanvasLayout(int xsize, int ysize, int xco, int yco) {
			Canvas canvas = _plotCanvas.getGraphicsCanvas();
			canvas.setLocation(xco, yco);
			canvas.setSize(xsize, ysize);
			this.add(canvas);
		}

		public void fixPanelLayout(int xsize, int ysize, int xco,
				int yco) {
			this.setLayout(null);
			this.setLocation(xco, yco);
			this.setSize(xsize, ysize);
			fixCanvasLayout(xsize, ysize, 0	, 0);
		}
		
	public void addValue(double d) {
		double overwrittenValue=0;
		//First move al the values on the plot
		for(int i=0;i<_noValues-1;i++){
			overwrittenValue=_data[i];
			_data[i]=_data[i+1];
		}
		//now add the new value
		_data[_noValues-1]=d;
		DataCurve newCurve = new DataCurve("");
	    for (int i = 0; i < _data.length; i++) {
	    	newCurve.addElement(new DataPoint(i, _data[i]));
	    }
	    _curve=newCurve;
	    _dataPlot.replaceElementAt(0, _curve);

	}
}
