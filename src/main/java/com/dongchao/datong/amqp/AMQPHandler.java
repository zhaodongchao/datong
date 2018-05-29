package com.dongchao.datong.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AMQPHandler {
    @Autowired
    private RabbitTemplate rabbitTemplate ;

    @RequestMapping("/send")
    public String sendMessage(){
        for (int i = 1; i <= 100; i++) {
            rabbitTemplate.convertAndSend("my_rabbit_queues","来自RabbitMQ的第"+i+"次问候");
        }
        return "success send the messages!" ;
    }
}
