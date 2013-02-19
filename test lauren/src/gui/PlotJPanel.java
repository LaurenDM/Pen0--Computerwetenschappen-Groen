package gui;
import java.awt.BorderLayout;
import java.awt.Button;
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
	private int[] _data;
	  private DataPlot _dataPlot;
	private Properties _props;
	private GraphicsPlotCanvas _plotCanvas;
	private final int 	_noValues,
						_minY,
						_maxY;
	public PlotJPanel(int noValues, int minY, int maxY){
		this._noValues=noValues;
		_data=new int[noValues];
		_minY=minY;
		_maxY=maxY;
		
	}
	  public void init() {
	     _plotCanvas = createPlotCanvas();

	    _dataPlot = new DataPlot();
	    _dataPlot.addElement(new DataCurve(""));
	    _plotCanvas.connect(_dataPlot);
	    this.setLayout(new BorderLayout());
	    this.add(_plotCanvas.getGraphicsCanvas(), BorderLayout.CENTER);
	    this.add(createControlPanel(), BorderLayout.SOUTH);
	  }
	 
	  private GraphicsPlotCanvas createPlotCanvas() {
	   _props = new Properties();
	   SymbolFactory x;

	    ConfigParameters config
	        = new ConfigParameters(new PropertiesBasedConfigData(_props));
	    _props.put("plot/legendVisible", "false");// een rechts-kadertje bovenaan
	    _props.put("plot/coordinateSystem/xAxis/minimum", "-0.5");
	    _props.put("plot/coordinateSystem/xAxis/maximum", "6.5");
	    _props.put("plot/coordinateSystem/xAxis/axisLabel", "nummer meting");
//	    props.put("plot/coordinateSystem/xAxis/ticLabelFormat/className",
//	              "jcckit.plot.TicLabelMap");
//	    props.put("plot/coordinateSystem/xAxis/ticLabelFormat/map",
//	              "0=Mo;1=Tu;2=We;3=Th;4=Fr;5=Sa;6=Su");
	    _props.put("plot/coordinateSystem/yAxis/axisLabel", "Lightvalue");
	    _props.put("plot/coordinateSystem/yAxis/maximum", "100");
	    _props.put("plot/coordinateSystem/yAxis/minimum", "0");
	    _props.put("plot/coordinateSystem/yAxis/ticLabelFormat", "%d%%");//hoe de nummers op y afgebeeld worden, in dit geval als percentages
	    _props.put("plot/curveFactory/definitions", "curve"); //Dit lijntje heb je nodig om verdere eigenschappen van de curve te kunnen bepalen die hieronder staan
	    _props.put("plot/curveFactory/curve/withLine", "true"); //Dppr dit true te zetten staat er altijd een lijn ook al zijn er ook staafjes
	    _props.put("plot/curveFactory/curve/symbolFactory/className", 
	              "jcckit.plot.BarFactory"); //Dit zorgt van de effectieve keuze voor staafjes ook mogelijk is CircleSymbolFactory en SquareSymbolFactory en ErrorBarFactory(geen idee wat dit precies is)
	    _props.put("plot/curveFactory/curve/symbolFactory/attributes/className", 
	              "jcckit.graphic.ShapeAttributes"); //Dit zorgt ervoor dat we de attributen van de staafjes kunnen aanpassen zoals hieronder gedaan word
	    _props.put("plot/curveFactory/curve/symbolFactory/attributes/fillColor", 
	              "0xfe8000"); //Kleur van de staafjes
	    _props.put("plot/curveFactory/curve/symbolFactory/attributes/lineColor", 
	              "1"); //lijn rond staafjes
	    _props.put("plot/curveFactory/curve/symbolFactory/size", "0.01"); //dikte gekozen soort curve, van staafjes dus, niet van lijn
	    _props.put("plot/initialHintForNextCurve/className", 
	              "jcckit.plot.PositionHint");
	    _props.put("plot/initialHintForNextCurve/position", "0 0.1"); //bepaalt positie begin staafjes: "iets te maken met x iets te maken met y", "0 0.1" is juiste

	  GraphicsPlotCanvas newPlotCanvas= new GraphicsPlotCanvas(config);
	    
	    return newPlotCanvas;
	  }
	  
	  private JPanel createControlPanel() {
	    JPanel controlPanel = new JPanel();
	    JButton startButton = new JButton("animate");
	    startButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                  new Thread() {
	                          public void run() {
	                            animate();
	                          }
	                        }.start();
	                }
	              });
	    controlPanel.add(startButton);
	    
	    return controlPanel;
	  }
	  
	  private void animate() {
//		    setXDomain(-10, 10);

		  DataCurve curve = new DataCurve("");
	    for (int i = 0; i < _data.length; i++) {
	      curve.addElement(new DataPoint(i, _data[i]));
	    }
	    _dataPlot.replaceElementAt(0, curve);
	    curve.removeElementAt(0);
	  
	//
//	    for (int i = 0; i < _data.length; i++) {
//	      curve.replaceElementAt(i, new DataPoint(i,_data[i]));
//	      try {
//	        Thread.sleep(10);
//	      } catch (InterruptedException e) {}
//	    double x = i;
//	      double y = 0;
//	      double tester=-0.5;
//	      while (y < _data[i]) {
//	    	  tester+=0.1;
////	    	  setCoordinateSystem(tester, 6.5+tester);
//	    	try {
//	          Thread.sleep(50);
//	        } catch (InterruptedException e) {}
//	        y = Math.min(_data[i], y + 0.1);
//	        curve.replaceElementAt(i, new DataPoint(x, y));
//	      }
	    }
//	    setXDomain(3, 5);
	//  }

	  private void setXDomain(double xMin,  double xMax)
	{
		  
		  Properties gProps = new Properties();
		  gProps.put("xAxis/minimum", Double.toString(xMin));
		  gProps.put("xAxis/maximum", Double.toString(xMax));
//		  gProps.put("yAxis/axisLabel", "Lightvalue");
		  gProps.put("yAxis/maximum", "100");
		  gProps.put("yAxis/ticLabelFormat", "%d%%");

		   ConfigParameters gConfig
		       = new ConfigParameters(new PropertiesBasedConfigData(gProps));
		   CartesianCoordinateSystem cs = new CartesianCoordinateSystem(gConfig);
		   _plotCanvas.getPlot().setCoordinateSystem(cs);
	}
	  
	  public static void main(String[] args) {
	    JFrame frame = new JFrame("Animated Chart");
	    frame.addWindowListener(new WindowAdapter() {
	              public void windowClosing(WindowEvent event) {
	                System.exit(0);
	              } 
	            });
//	    PlotJPanel animatedChart = new PlotJPanel();
//	    animatedChart.init();
//	    frame.add(animatedChart); TODO Francis
	    frame.setSize(600, 500);
	    frame.show();
	  }
	  
	  
	public void addValue(int value) {
		// TODO Auto-generated method stub
		
	}
}
