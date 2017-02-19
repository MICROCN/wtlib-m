package com.wtlib.base.mq;

import java.io.IOException;
import java.util.HashMap;

/**
 * ClassName: MQDemo
 * 
 * @Description: MQ消息生产者和消费者示例
 * @author dapengniao
 * @date 2016年7月5日 下午3:22:37
 */
public class MQDemo {

	public static void Demo() throws IOException{

		// 消费者启动
		DemoConsumer consumer = new DemoConsumer(RabbitMQAdapter.DEMO);
		Thread consumerThread = new Thread(consumer);
		
		consumerThread.start();

		/**
		 * MQ生产者启动
		 */
		DemoProducer mq = new DemoProducer(RabbitMQAdapter.DEMO);
		for (int i = 0; i < 20; i++) {
			HashMap<String, Object> message = new HashMap<String, Object>();
			message.put("demo", "Message " + i);
			mq.sendMessage(message);
			System.out.println("========================" + i);
		}

	}

	public static void main(String[] args) throws IOException {
		// 消费者启动
		DemoConsumer consumer = new DemoConsumer(RabbitMQAdapter.DEMO);
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		/**
		 * MQ生产者启动
		 */
		DemoProducer mq = new DemoProducer(RabbitMQAdapter.DEMO);
		for (int i = 0; i < 20; i++) {
			HashMap<String, Object> message = new HashMap<String, Object>();
			message.put("demo", "Message " + i);
			mq.sendMessage(message);
			System.out.println("========================" + i);
		}
	}
}
