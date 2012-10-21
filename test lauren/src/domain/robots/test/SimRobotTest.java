package domain.robots.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.robots.SimRobotPilot;

public class SimRobotTest {
	
	SimRobotPilot simRobotpilot;
	
	@Before
	public void setUp(){
		this.simRobotpilot=new SimRobotPilot();
	}

	@Test
	public void moveTest(){
		assertEquals(0,simRobotpilot.getOrientation(),0);
		assertEquals(0,simRobotpilot.getPosition().getX(),0);
		assertEquals(0,simRobotpilot.getPosition().getY(),0);
		simRobotpilot.move(10);
		assertEquals(10,simRobotpilot.getPosition().getX(),0.1);
		assertEquals(0,simRobotpilot.getPosition().getY(),0.1);
		simRobotpilot.turn(90);
		assertEquals(90,simRobotpilot.getOrientation(),0);
		simRobotpilot.move(10);
		assertEquals(10,simRobotpilot.getPosition().getX(),0.1);
		assertEquals(10,simRobotpilot.getPosition().getY(),0.1);

	}


	@Test
	public void turnTest(){
		simRobotpilot.turn(-90);
		assertEquals(-90, simRobotpilot.getOrientation(),0.1);
		simRobotpilot.turn(-40);
		assertEquals(-130, simRobotpilot.getOrientation(),0.1);
		simRobotpilot.turn(-100);
		assertEquals(130, simRobotpilot.getOrientation(),0.1);
		simRobotpilot.turn(30.5);
		assertEquals(160.5, simRobotpilot.getOrientation(), 0.1);
	}
}