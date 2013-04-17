package domain.robots;


import java.io.IOException;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.IRSeeker;
import lejos.robotics.navigation.DifferentialPilot;

public class InfraredSensorTester {
	
	private static IRSeeker sensor;
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
		sensor = new IRSeeker(SensorPort.S1);
		robot = new DifferentialPilot(5.55F, 17.22F,Motor.B, Motor.C);
		robot.setTravelSpeed(1);
		robot.setRotateSpeed(3.6);
		


		System.out.println("Drive toward infrared ball from 100 cm away. Press enter when ready.");
		System.in.read();
		for(int i = 0; i< 100; i++){
			robot.travel(1);
			System.out.println("Loop " + i + " for id = 1 - 2 - 3- 4- 5");
			System.out.println("               ");
			for(int j = 1; j<6; j++){
				System.out.print(sensor.getSensorValue(j) + " - ");
			}
		}
		
		
		
	}

}
