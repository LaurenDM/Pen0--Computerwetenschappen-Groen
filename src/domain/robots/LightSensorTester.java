package domain.robots;


import java.io.IOException;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.remote.NXTCommand;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTCommandConnector;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import lejos.robotics.navigation.DifferentialPilot;

//TODO verbeteren naar nieuwe verbinding
public class LightSensorTester {
	
	private static LightSensor sensor;
	
	private static DifferentialPilot robot;
	
	public static void main(String[] args) throws IOException{

		robot = new DifferentialPilot(5.55F, 17.22F,Motor.B, Motor.C);
//		robot = new DifferentialPilot(5.55F, 17.22F,nxtCommand); TODO Francis
		
		System.out.print(1);

//		FileOutputStream output1 = new FileOutputStream("woodValues.txt");
//		FileOutputStream output2 = new FileOutputStream("whiteValues.txt");
		
		System.out.println("Calibrate high");
		System.in.read();
		sensor.calibrateHigh();
		System.out.println("Calibrate Low");
		System.in.read();
		sensor.calibrateLow();
		
		System.out.println("Wood values");
		System.in.read();
		for(int i= 0; i<100; i++){
			robot.rotate(5);
			System.out.println(sensor.readValue());
		}
		robot.setTravelSpeed(1);
		System.out.println("White values");
		System.in.read();
		for(int i=0; i<100; i++){
			robot.travel(2);
			System.out.println(sensor.readValue());
		}
		
		}

		
		
		
		
	

}
