package com.taoding.mq;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yaochenglong on 2017/11/30.
 * 消息发送者
 */
//@Component
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Map<String,String> msg){
        rabbitTemplate.convertAndSend("foo",msg);
    }
}
