package com.wtlib.base.mq;

/**
 * ClassName: RabbitMQAdapter
 * @Description: 统一管理RabbitMQ节点,防止节点的重复
 * @author dapengniao
 * @date 2016年7月5日 下午2:26:33
 */
public enum RabbitMQAdapter {
	
	DEMO("Demo", "消息队列示例");

	// 消息队列节点名称
	private String endpointName;
	//消息队列名称
	private String queueName;
	
	/**
	 * @Description: TODO
	 * @param @param endpointName 消息队列节点名称
	 * @param @param queueName 消息队列名称
	 * @author dapengniao
	 * @date 2016年7月4日 下午2:57:29
	 */
	private RabbitMQAdapter(String endpointName, String queueName) {
		this.endpointName = endpointName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

}
