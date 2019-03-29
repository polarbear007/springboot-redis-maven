package cn.itcast.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	@Bean
	public RedisTemplate<String, Object> redisTemplate(@Autowired RedisConnectionFactory connectionFactory){
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		
		// 设置默认的序列化器---> 把对象转成 json ，不要使用jdk 自带的序列化器
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		// 一般key 我们就用字符串就 好了，不需要转成 json 
		template.setKeySerializer(new StringRedisSerializer());
		
		return template;
	}
}
