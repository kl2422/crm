package com.shsxt.rabbitmq.topic;
import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogsTopic2 {
	private static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		String queueName = "topic_logs_logs2";// 定义"topic_logs_logs2"的Queue
		// 声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 设置exchange类型topic:会把消息路由到那些binding key与routing key按规定匹配或者模糊匹配的Queue中
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		// 将队列和exchange按照logs.debug下面的所有字符进行匹配
		channel.queueBind(queueName, EXCHANGE_NAME, "logs.debug.*");
		System.out.println(" [*]-2 Waiting for messages. ");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
}