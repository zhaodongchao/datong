package com.dongchao.datong.common.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDao {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public void save(String key ,String value){
        stringRedisTemplate.opsForValue().set(key,value);
    }
    public void save(User user){
       redisTemplate.opsForValue().setIfAbsent(user.getId(),user);
    }
    public String getStr(String key){
       return stringRedisTemplate.opsForValue().get(key);
    }
    public User get(){
       return (User) redisTemplate.opsForValue().get("zhaodongchao");
    }
}
