package domain.robots;


import java.io.FileOutputStream;
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
import lejos.util.PilotProps;


public class LightSensorTester {
	
	private static LightSensor sensor;
	
	private static DifferentialPilot robot;
	
	public static void main(String[] args) throws IOException{
		NXTComm nxtComm;
		NXTConnector conn = new NXTConnector();
		NXTInfo[] nxtInfo= conn.search(null, null, NXTCommFactory.BLUETOOTH);
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			System.out.println("We found this NXT: "+nxtInfo[0].name);//TODO
			nxtComm.open(nxtInfo[0]);
			NXTCommand nxtCommand=new NXTCommand(nxtComm);
			try {
				System.out.println("Bluetooth succeeded with " + nxtCommand.getFriendlyName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //TODO
		
		NXTCommandConnector.setNXTCommand(nxtCommand);
		sensor = new LightSensor(SensorPort.S3);
		robot = new DifferentialPilot(5.55F, 17.22F,nxtCommand);
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
		
		}catch(Exception e){
			//TODO
		}

		
		
		
		
	}

}
