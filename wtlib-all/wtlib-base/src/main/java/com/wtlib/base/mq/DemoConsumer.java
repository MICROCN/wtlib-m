package com.wtlib.base.mq;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.amqp.utils.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * ClassName: QueueConsumer
 * 
 * @Description: 读取队列的程序端，实现了Runnable接口
 * @author dapengniao
 * @date 2016年6月21日 下午12:06:46
 */
public class DemoConsumer extends RabbitMQClient implements Runnable, Consumer {

	public DemoConsumer(RabbitMQAdapter rabbitMQAdapter) throws IOException {
		super(rabbitMQAdapter);
	}

	public void run() {
		try {
			// start consuming messages. Auto acknowledge messages. 一直在等待消息,
			// boolean autoAck开始消耗消息 ack=true自动确认消息,如果为false则不会自动回复需要人为手动确认
			channel.basicConsume(endPointName, true, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when consumer is registered --- 当消费者注册时
	 */
	public void handleConsumeOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + " registered");
	}

	/**
	 * Called when new message is available -- 当有消息到达时候
	 */
	public void handleDelivery(String consumerTag, Envelope env,
			BasicProperties props, byte[] body) throws IOException {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HashMap<String, Object> map = (HashMap) SerializationUtils
				.deserialize(body);
		System.out.println("收取到的消息======================" + map.get("demo"));
	}

	public void handleCancel(String consumerTag) {
	}

	public void handleCancelOk(String consumerTag) {
	}

	public void handleRecoverOk(String consumerTag) {
	}

	public void handleShutdownSignal(String consumerTag,
			ShutdownSignalException arg1) {
	}
}