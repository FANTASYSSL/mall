package com.wch.e3mall.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			System.out.println("spring:"+textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
