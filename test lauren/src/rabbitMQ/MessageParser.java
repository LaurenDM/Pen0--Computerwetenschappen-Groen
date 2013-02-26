package rabbitMQ;

import controller.Controller;
import domain.robots.Robot;

public class MessageParser {
	
		private Controller controller;
	
		public MessageParser(Controller controller){
			this.controller = controller;
		}
		
		public void parse(String routingKey, String body) {
//			System.out.println(routingKey);
			if(!body.contains("start")){
			Robot robot_temp = controller.getRobotFromIdentifier(routingKey.split("_")[1]);
			if(robot_temp!=null){
				if(body.startsWith("Ball")){
					System.out.println("Ball found");
				}
				else{
					robot_temp.setPose((int)Double.parseDouble(body.split("_")[2]), (int)Double.parseDouble(body.split("_")[0]), (int)Double.parseDouble(body.split("_")[1]));
				}
			}
		}
	}
}