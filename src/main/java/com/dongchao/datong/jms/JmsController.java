package com.dongchao.datong.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JmsController {
    @Autowired
    private JmsTemplate jmsTemplate ;
    @RequestMapping("/send")
    public String sendMessage(){
        jmsTemplate.send("my-destination",new MyMessage());
        return "success send message" ;
    }
}
