package com.wch.e3mall.redis;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJRedis {
	
//	@Test
	public void testJedis() throws Exception {
		Jedis jedis = new Jedis("192.168.9.11");
		jedis.set("test", "abc");
		System.out.println(jedis.get("test"));
		jedis.close();
	}
	
//	@Test
	public void jedisPool() throws Exception {
		JedisPool jedisPool = new JedisPool("192.168.9.11",6379);
		Jedis jedis = jedisPool.getResource();
		jedis.set("jedisPool", "pool");
		System.out.println(jedis.get("jedisPool"));
		jedis.close();
		jedisPool.close();
	}
	
//	@Test
	public void jedisCluster() throws Exception {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.9.11", 7001));
		nodes.add(new HostAndPort("192.168.9.11", 7002));
		nodes.add(new HostAndPort("192.168.9.11", 7003));
		nodes.add(new HostAndPort("192.168.9.11", 7004));
		nodes.add(new HostAndPort("192.168.9.11", 7005));
		nodes.add(new HostAndPort("192.168.9.11", 7006));
		JedisCluster cluster = new JedisCluster(nodes);
		Long lpush = cluster.lpush("clusterlist", "cluster01","cluster01","cluster01","cluster01");
		System.out.println(lpush);
//		List<String> list = cluster.lrange("clusterlists", 0, -1);
		List<String> list = cluster.lrange("clusterlist", 0, -1);
		for (String string : list) {
			System.out.println(string);
		}
		System.out.println(list.size());
		System.out.println(list==null);
	}
	
	
}
