package com.wch.e3mall.mq;

import static org.junit.Assert.*;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext-dao.xml",
			"classpath:spring/applicationContext-activemq.xml"})
public class TestSpringMq {
	
	/**
	 * @Rsource 注解先根据属性名称查找bean ，所以属性名一定不要重复！！
	 */
	@Resource
	ApplicationContext applicationContext;
	
	@Resource
	private Destination destinationQueue;
	/*@Autowired
	private JmsTemplate jmsTemplate;*/
	
	/**
	 * 
	 * queue send
	 * @throws Exception
	 */
	@Test
	public void testSpringQueue() throws Exception {
		
		System.in.read();
	}
	
	
	
}
