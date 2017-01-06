package com.shsxt.rabbitmq.simple;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Receive {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] arg) throws Exception {

		// 建立连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();

		// 创建channel
		Channel channel = connection.createChannel();
		/*  durable:是否持久化 默认是的
			exclusive: 仅创建者可以使用的私有队列，断开后自动删除
			auto_delete: 当所有消费客户端连接断开后，是否自动删除队列
			此处要跟Send设置要一致
		 */
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. ");

		// 接收消息
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, 
					AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}
}