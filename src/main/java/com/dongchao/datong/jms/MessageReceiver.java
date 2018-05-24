package com.dongchao.datong.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {
    /**
     * JmsListener时Spring4.1提供的新特性，用来简化jms开发。
     * 使用@JmsListener注解后，会自动监听destination发送的消息
     * @param message
     */
    @JmsListener(destination = "my-destination")
    public void receiveMessage(String message){
        System.out.println("接收到消息：<"+message+">");
    }
}
