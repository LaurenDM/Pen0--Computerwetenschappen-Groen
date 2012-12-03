package domain.robots;

import java.io.IOException;


import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class UltrasonicSensorTester {
	
	
	private static UltrasonicSensor sensor;
	private static DifferentialPilot robot;

	public static void main(String[] args) throws IOException{
//		NXTComm nxtComm;
//		NXTConnector conn = new NXTConnector();
//		NXTInfo[] nxtInfo= conn.search(null, null, NXTCommFactory.BLUETOOTH);
//		try {
//			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
//			System.out.println("We found this NXT: "+nxtInfo[0].name);//TODO
//			nxtComm.open(nxtInfo[0]);
//			NXTCommand nxtCommand=new NXTCommand(nxtComm);
//			try {
//				System.out.println("Bluetooth succeeded with " + nxtCommand.getFriendlyName());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} //TODO
//		
//		NXTCommandConnector.setNXTCommand(nxtCommand);
		sensor = new UltrasonicSensor(SensorPort.S2);
		robot = new DifferentialPilot(5.55F, 17.22F,Motor.B, Motor.C);
		robot.setTravelSpeed(1);
		robot.setRotateSpeed(3.6);
		

		System.out.println("Drive to wall from distance 50cm");
		System.in.read();
		for(int i = 0; i< 100; i++){
			System.out.println(sensor.getDistance());
			robot.travel(0.4);
		}
		System.out.println("Turn around between 4 walls");
		System.in.read();
		for(int i = 0; i< 100; i++){
			System.out.println(sensor.getDistance());
			robot.rotate(3.6);
		}
		System.out.println("Drive between two walls"); //zet robot 10 cm voor begin van muren
		System.in.read();
		for(int i = 0; i< 100; i++){
			System.out.println(sensor.getDistance());
			robot.travel(1);
		}
		
		
//		}catch(Exception e){
//			//TODO
//		}
	}
	
}
