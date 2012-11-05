package domain.robots;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

public class LightSensorTester {
	
	private static LightSensor sensor;
	private static DifferentialPilot robot;
	
	public static void main(String[] args) throws IOException{
		sensor = new LightSensor(SensorPort.S3);
		robot = new DifferentialPilot(5.55F, 11.22F, Motor.C, Motor.B);
		

		FileOutputStream output1 = new FileOutputStream("woodValues.txt");
		FileOutputStream output2 = new FileOutputStream("whiteValues.txt");
		
		System.out.println("Calibrate high");
		Button.waitForAnyPress();
		sensor.calibrateHigh();
		System.out.println("Calibrate Low");
		Button.waitForAnyPress();
		sensor.calibrateLow();
		
		System.out.println("Wood values");
		Button.waitForAnyPress();
		for(int i= 0; i<100; i++){
			robot.rotate(5);
			output1.write(sensor.readValue());
		}
		
		System.out.println("White values");
		Button.waitForAnyPress();
		for(int i=0; i<100; i++){
			robot.travel(2);
			output2.write(sensor.readValue());
		}
		
		
		
		
		
		
	}

}
