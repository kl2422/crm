package com.shsxt.rabbitmq.workqueues;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Worker {
  private static final String TASK_QUEUE_NAME = "task_queue";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    
    final Connection connection = factory.newConnection();
    final Channel channel = connection.createChannel();
    // 声明此队列并且持久化  
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. ");
    
    // 告诉RabbitMQ同一时间给一个消息给消费者  
    channel.basicQos(1);

    final Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, 
    		  AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
        try {
          doWork(message);
        } finally {
          System.out.println(" [x] Done");
          channel.basicAck(envelope.getDeliveryTag(), false);
        }
      }
    };
    
    // 把名字为TASK_QUEUE_NAME的Channel的值回调给QueueingConsumer,
    // 即使一个worker在处理消息的过程中停止了，这个消息也不会失效 
    boolean autoAck = false;
    channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
  }

  private static void doWork(String task) {
    for (char ch : task.toCharArray()) {
      if (ch == '.') {
        try {
          Thread.sleep(1000); // 这里是假装我们很忙 
        } catch (InterruptedException _ignored) {
          Thread.currentThread().interrupt();
        }
      }
    }
  }
}