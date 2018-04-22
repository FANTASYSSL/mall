package com.wch.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActivemq {

	@Test
	public void testQueuePruducer() throws Exception {
		String brokerURL = "tcp://192.168.9.14:61616";
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		String queueName = "teat-queue";
		Queue queue = session.createQueue(queueName);
		MessageProducer producer = session.createProducer(queue);
		TextMessage textMessage = session.createTextMessage();
		textMessage.setText("发送队列一个消息");
		producer.send(textMessage);
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws Exception {
		String brokerURL = "tcp://192.168.9.14:61616";
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("sring-queue");
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopic() throws Exception {
		String brokerURL = "tcp://192.168.9.14:61616";
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL );
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("test-topic");
		MessageProducer producer = session.createProducer(topic);
		TextMessage textMessage = session.createTextMessage("topic modle");
		producer.send(textMessage);
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicAccept() throws Exception {
		String brokerURL = "tcp://192.168.9.14:61616";
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL );
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("test-topic");
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		System.out.println("消费者启动3。。。。。。");
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}
	
	
	
	
}
