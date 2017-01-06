package com.shsxt.rabbitmq.ps;
import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogs {
	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		// 获取默认的queueName
		String queueName01 = "lg_01";
		channel.queueDeclare(queueName01, false, false, false, null);  
		// queue和exchange进行绑定
		channel.queueBind(queueName01, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. ");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(queueName01, true, consumer);
	}
}