/**
 * 
 */
package domain.calibration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Joren
 *
 */
public class TestDriver {
	private String logString;
	
	
	public TestDriver(){
		logString = "";
	}
	
	public void drivePolygon(int sides, double sideLength, int times){
		
	}
	
	public void driveDistance(double distance, int times){
		
	}
	
	public void driveTurn(double angle, int times){
		
	}
	
	public void testDistanceSensor(int times){
		
	}
	
	public void testLightSensor(int times){
		
	}
	
	
	
	public void logTestResults(){
		BufferedWriter logOutputWriter = null;
		try {
			logOutputWriter = new BufferedWriter(new FileWriter("testDriveLog.txt"));
			logOutputWriter.append(logString);
			logString = "";
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				logOutputWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
