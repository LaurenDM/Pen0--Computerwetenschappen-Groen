package rabbitMQ;

import java.io.IOException;
import java.net.ConnectException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.ConnectionParameters;

public class MQ {
	/**
	 * Create a connection to a AMQP server using the configuration in the
	 * Config class
	 * 
	 * @return An active connection
	 * @throws IOException
	 */
	public static Connection createConnection() throws IOException {

	//	try{
	//	ConnectionParameters params = new ConnectionParameters();
	//	params.setRequestedHeartbeat(0);
		//ConnectionFactory factory = new ConnectionFactory(params);
		System.out.println("Trying to connect...");
		//Connection conn = factory.newConnection("localhost", 8888);
	//	return conn;
		return null;
	//	}
	//	catch(ConnectException e){
	//		System.out.println("Failed to connect through ssh, trying direct connection (campusnet)");
	//		return createCampusConnection();
	//	}
		
	}
	
	public static Connection createCampusConnection() throws IOException{
	//	try{
	//		ConnectionParameters params = new ConnectionParameters();
	//		params.setUsername(Config.USER_NAME);
	//		params.setPassword(Config.PASSWORD);
	//		params.setVirtualHost(Config.VIRTUAL_HOST);
	//		params.setRequestedHeartbeat(0);
		//	ConnectionFactory factory = new ConnectionFactory(params);

		//	Connection conn = factory.newConnection(Config.HOST_NAME, Config.PORT);
		//	return conn;
		//	}
		//	catch(ConnectException e){
			//	System.out.println("Failed to connect through campusnet (Are you connected to campusnet?)");
		//	}
		return null;
	}

	/**
	 * Create a channel on the given connection. This method will also configure
	 * a message exchange and message queue based on the configuration in the
	 * Config class. The routing key in the config class is used to route
	 * message from the exchange to the queue.
	 * 
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	public static Channel createChannel(Connection conn) throws IOException {
		Channel channel = conn.createChannel();
		channel.exchangeDeclare(Config.EXCHANGE_NAME, "topic");

		return channel;
	}
}