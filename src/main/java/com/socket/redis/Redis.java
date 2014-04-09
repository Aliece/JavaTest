package com.socket.redis;

import org.junit.Test;
import org.springframework.test.context.TestExecutionListeners;

import redis.clients.jedis.Jedis;

/**
 * @title: Redis.java
 * @description: 
 * @date 2014年3月14日
 * @version 1.0
 */

public class Redis {
	
	
	@Test
	public void test() {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		
		String hw = jedis.get("test");
		
		System.out.println(hw);
	}

}
