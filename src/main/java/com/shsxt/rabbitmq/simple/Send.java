package com.shsxt.rabbitmq.simple;

import java.util.Scanner;

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

		/*  durable:是否持久化 默认是的
			exclusive: 仅创建者可以使用的私有队列，断开后自动删除
			auto_delete: 当所有消费客户端连接断开后，是否自动删除队列
		 */
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		@SuppressWarnings("resource")
		Scanner tools =new Scanner(System.in);
		while(true){
			String message = tools.nextLine(); // 控制台输入
			if(message.equalsIgnoreCase("q")) { // 如果控制台输入q则关闭连接
				// 关闭
				channel.close();
				connection.close();
				break;
			}
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");		    
		}
	}
}