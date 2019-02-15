package com.wanzhs.rabbit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "com.wanzhs.queue")
@Slf4j
public class RabbitHandle {
    @RabbitHandler
    public void processMessage(String message) {
        log.info("消息内容：{}",message);
    }
}
