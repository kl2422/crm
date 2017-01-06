package com.shsxt.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogTopic {

	private static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 设置exchange的type为topic：会把消息路由到那些binding key与routing key按规定匹配或者模糊匹配的Queue中
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");

		String routingKeyOne = "logs.debug.one";// 定义一个路由名为“error”  
		for (int i = 0; i <= 1; i++) {  
			String messageOne = "this is one debug logs:" + i;  
			channel.basicPublish(EXCHANGE_NAME, routingKeyOne, null, messageOne  
					.getBytes());  
			System.out.println(" [x] Sent '" + routingKeyOne + "':'" + messageOne + "'");  
		}  

		System.out.println("---------------------------------");  
		String routingKeyTwo = "logs.debug.two";  
		for (int i = 0; i <= 2; i++) {  
			String messageTwo = "this is two debug logs:" + i;  
			channel.basicPublish(EXCHANGE_NAME, routingKeyTwo, null, messageTwo  
					.getBytes());  
			System.out.println(" [x] Sent '" + routingKeyTwo + "':'" + messageTwo + "'");  
		}  

		System.out.println("---------------------------------"); 
		String routingKeyThree = "logs.info.one";  
		for (int i = 0; i <= 3; i++) {  
			String messageThree = "this is one info logs:" + i;  
			channel.basicPublish(EXCHANGE_NAME, routingKeyThree, null,  
					messageThree.getBytes());  
			System.out.println(" [x] Sent '" + routingKeyThree + "':'" + messageThree + "'");  
		}  
		
		channel.close();
		connection.close();
	}

}