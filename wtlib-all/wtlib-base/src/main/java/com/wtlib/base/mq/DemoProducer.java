package com.wtlib.base.mq;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.amqp.utils.SerializationUtils;



 
/**
 * ClassName: DemoProducer
 * @Description: 消息生产者
 * @author dapengniao
 * @date 2016年7月5日 下午4:53:02
 */
public class DemoProducer extends RabbitMQClient{
     
    public DemoProducer(RabbitMQAdapter rabbitMQAdapter) throws IOException{
        super(rabbitMQAdapter);
    }
 
    /**
     * 这个如果要将消息设置为持久化的则第三个参数设置为MessageProperties.PERSISTENT_TEXT_PLAIN
     */
    public void sendMessage(Serializable object) throws IOException {
        channel.basicPublish("",endPointName,null, SerializationUtils.serialize(object));
    }  
}