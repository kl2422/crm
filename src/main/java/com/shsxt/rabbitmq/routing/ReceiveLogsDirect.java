package com.shsxt.rabbitmq.routing;
import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveLogsDirect {

	private static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// 设置exchange的type为direct：会把消息路由到那些binding key与routing key完全匹配的Queue中
		channel.exchangeDeclare(EXCHANGE_NAME, "direct"); 
		String queueName = channel.queueDeclare().getQueue(); // 获取默认的queue

		if (argv.length < 1) { // 执行main方法的参数要有值，多个以空格隔开
			System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
			System.exit(1);
		}

		for(String severity : argv) { 
			// queue、exchange及路由绑定
			channel.queueBind(queueName, EXCHANGE_NAME, severity);
		}
		System.out.println(" [*] Waiting for messages. ");

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