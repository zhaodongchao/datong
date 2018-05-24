package com.dongchao.datong.system.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>() ;
        redisTemplate.setConnectionFactory(redisConnectionFactory);//必须设置redisConnectionFactory,此处直接注入

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        redisTemplate.setKeySerializer( new StringRedisSerializer()); //设置key的序列化采用StringRedisSerializer
        /*
         虽然Spring boot会为我们自动配置好RedisTemplate，但是默认的对Object的序列化采用的jdk自带的JdkSerializationRedisSerializer,
         而JdkSerializationRedisSerializer是以二进制存储数据的，不利于我们调试,此处我们将自定义RedisTemplate的值序列化工具为Jackson2JsonRedisSerializer
         */
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);//设置值的序列化采用Jackson2JsonRedisSerializer

        return redisTemplate ;
    }
}
