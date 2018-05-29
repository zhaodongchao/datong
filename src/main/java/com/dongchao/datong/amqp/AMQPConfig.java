package com.dongchao.datong.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {
    /*
      定义一个消息队列，名为my_rabbit_queues
     */
    @Bean
    public Queue wiselyQueue(){
        return new Queue("my_rabbit_queues");
    }
}
