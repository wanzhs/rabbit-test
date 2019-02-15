package com.wanzhs.rabbit.mp_single_host;

import com.wanzhs.rabbit.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
//@RabbitListener(containerFactory = "myListenerContainer",queues = "spring-boot")
public class Receiver1 {
    @RabbitHandler
    public void receiveUser(User user) {
        log.info("##user### = {}",user);
    }
    @RabbitHandler
    public void receiveStr(String str) {
        log.info("##str### = {}",str);
    }
}
