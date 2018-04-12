package com.wch.e3mall.redis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wch.e3mall.common.redis.JedisClient;

public class TestJedisClient {

//	@Test
	public void testJedisClient() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		String result = jedisClient.set("testJedisClient", "jedisClient");
		System.out.println(result);
		System.out.println(jedisClient.get("testJedisClient"));
	}
	
}
