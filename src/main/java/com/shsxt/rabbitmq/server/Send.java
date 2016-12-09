package com.shsxt.rabbitmq.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
	
	private final static String QUEUE_NAME = "hello";
	
	public static void main(String[] argv) throws Exception {
		
		// 建立连接
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
//	    factory.setUsername(username);
//	    factory.setPassword(password);
	    Connection connection = factory.newConnection();
	    
	    // 创建channel
	    Channel channel = connection.createChannel();
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    String message = "Hello World! 顾骁伟";
	    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
	    System.out.println(" [x] Sent '" + message + "'");
	    
	    
	    // 关闭
	    channel.close();
	    connection.close();
		
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("192.168.1.152");
//		factory.setUsername("chenpan");
//		factory.setPassword("123456");
//		Connection connection = factory.newConnection();
//		Channel channel = connection.createChannel();
//		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//		String message = "Hello World!";
//		channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
//		System.out.println(" [x] Sent '" + message + "'");
//		channel.close();
//		connection.close();
	}
}