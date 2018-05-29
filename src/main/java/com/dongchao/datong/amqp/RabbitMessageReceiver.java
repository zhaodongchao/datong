package com.dongchao.datong.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessageReceiver {
    private static Logger logger = LoggerFactory.getLogger(RabbitMessageReceiver.class);

    /**
     * The following component creates a listener endpoint on the my_rabbit_queues queue:
     * @param message
     */
    @RabbitListener(queues = {"my_rabbit_queues"})
    public void receiveMessages(String message){
      logger.info("收到来自rabbitMQ的消息："+message);
    }
}
