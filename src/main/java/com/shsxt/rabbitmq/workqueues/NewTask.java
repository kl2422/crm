package com.shsxt.rabbitmq.workqueues;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTask {

	private static final String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] argv)
			throws java.io.IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		// 声明此队列并且持久化  
		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		
		@SuppressWarnings("resource")
		Scanner tools =new Scanner(System.in);
		while(true) {
			String message = tools.nextLine(); // 控制台输入
			if(message.equalsIgnoreCase("q")) { // 如果控制台输入q则关闭连接
				channel.close();
				connection.close();
				break;
			}
			channel.basicPublish("", TASK_QUEUE_NAME,
					MessageProperties.PERSISTENT_TEXT_PLAIN,
					message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");		    
		}
	}
}