package com.wtlib.base.mq;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.utils.SerializationUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wtlib.base.constants.GlobalConstants;

public abstract class  RabbitMQClient {
	protected final static Logger logger = LoggerFactory.getLogger(RabbitMQClient.class);
	private static final String RABBITMQ_SERVERS = GlobalConstants.getString("rabbitmq.servers");
	private static final Integer RABBITMQ_PORT = GlobalConstants.getInt("rabbitmq.port");
	private static final String RABBITMQ_USER = GlobalConstants.getString("rabbitmq.user");
	private static final String RABBITMQ_PSW = GlobalConstants.getString("rabbitmq.psw");
	
	protected Channel channel;
	protected Connection connection;
	protected String endPointName;
	
	public RabbitMQClient(RabbitMQAdapter rabbitMQAdapter) throws IOException{
        this.endPointName = rabbitMQAdapter.getEndpointName();
        //Create a connection factory
        ConnectionFactory factory = new ConnectionFactory();
        
        //hostname of your rabbitmq server  MQ连接配置信息
        factory.setHost(RABBITMQ_SERVERS);
        factory.setPort(RABBITMQ_PORT);
        factory.setUsername(RABBITMQ_USER);
        factory.setPassword(RABBITMQ_PSW);
        
        //getting a connection  获取连接
        connection = factory.newConnection();
        
        //creating a channel 创建一个通道
        channel = connection.createChannel();
        
        //declaring a queue for this channel. If queue does not exist,
        //it will be created on the server.(队列名，是否持久化，是否独有，是否自动删除，参数)
        channel.queueDeclare(endPointName, false, false, false, null);
   }
	
	/**
	 * @Description: 消息生产者 发送消息到节点
	 * @param @param object
	 * @param @throws IOException   
	 * @author dapengniao
	 * @date 2016年7月5日 下午2:33:49
	 */
	 public void sendMessage(Serializable object) throws IOException {
	        channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
	    }  
	
	
	 /**
     * 关闭channel和connection 并非必须，因为隐含是自动调用的。
     * @throws IOException
     */
     public void close() throws IOException{
         this.channel.close();
         this.connection.close();
     }
	
     
}
