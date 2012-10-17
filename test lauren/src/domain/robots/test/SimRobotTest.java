package domain.robots.test;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.Position.Position;
import domain.robots.SimRobotPilot;

public class SimRobotTest {

	@Test
	public void moveTest(){
		SimRobotPilot simRobotpilot=new SimRobotPilot();
		simRobotpilot.move(10);
		simRobotpilot.turn(90);
		simRobotpilot.move(10);
		assertEquals(simRobotpilot.getPosition().getX(),10,0.1);
		assertEquals(simRobotpilot.getPosition().getY(),10,0.1);

	}
}
