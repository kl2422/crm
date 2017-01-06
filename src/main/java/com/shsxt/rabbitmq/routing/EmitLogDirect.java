package com.shsxt.rabbitmq.routing;

import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {

	private static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 设置exchange的type为direct：会把消息路由到那些binding key与routing key完全匹配的Queue中
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

		@SuppressWarnings("resource")
		Scanner tools =new Scanner(System.in);
		while(true) {
			String content = tools.nextLine(); // 控制台输入错误级别#消息内容
			if(content.equalsIgnoreCase("q") 
					&& content.contains("#") 
					&& content.split("#").length > 2 ) { // 如果控制台输入q则关闭连接
				channel.close();
				connection.close();
				break;
			}
			String severity = content.split("#")[0]; // 错误级别
			String message = content.split("#")[1]; // 错误消息
			channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
			System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
		}
	}
}