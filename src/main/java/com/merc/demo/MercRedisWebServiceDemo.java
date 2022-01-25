package com.merc.demo;

import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.merc.demo.model.Item;

import redis.clients.jedis.Protocol;

@SpringBootApplication
public class MercRedisWebServiceDemo {

	@SuppressWarnings("deprecation")
	@Bean
	public JedisConnectionFactory jedisConnFactory() {
		try {
			String redistogoUrl = System.getenv("REDISTOGO_URL");
			URI redistogoUri = new URI(redistogoUrl);
			JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory();
			jedisConnFactory.setUsePool(true);
			jedisConnFactory.setHostName(redistogoUri.getHost());
			jedisConnFactory.setPort(redistogoUri.getPort());
			jedisConnFactory.setTimeout(Protocol.DEFAULT_TIMEOUT);
			jedisConnFactory.setPassword(redistogoUri.getUserInfo().split(":", 2)[1]);
			return jedisConnFactory;
		} catch (Exception e) {
			return null;
		}
	}

	@Bean
	RedisTemplate<String, Item> redisTemplate() {
		RedisTemplate<String, Item> redisTemplate = new RedisTemplate<String, Item>();
		redisTemplate.setConnectionFactory(jedisConnFactory());
		return redisTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(MercRedisWebServiceDemo.class, args);
	}
}
