package com.shsxt.rabbitmq.ps;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {

	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout"); // 定义一个exchange，类型为fanout
		
		@SuppressWarnings("resource")
		Scanner tools =new Scanner(System.in);
		while(true) {
			String message = tools.nextLine(); // 控制台输入
			if(message.equalsIgnoreCase("q")) { // 如果控制台输入q则关闭连接
				channel.close();
				connection.close();
				break;
			}
			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");		    
		}
	}
}