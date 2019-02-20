package com.wanzhs.rabbit.mp_multiple_host;

import com.wanzhs.rabbit.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.wanzhs.rabbit.mp_multiple_host.RabbitMulConstants.declareName;

@Slf4j
@Component
@RabbitListener(containerFactory = "myListenerContainer1",queues = {declareName})
public class Receiver1 {
    @RabbitHandler
    public void receiveUser(User user) {
        log.info("##user#receiver1## = {}",user);
    }
    @RabbitHandler
    public void receiveStr(String str) {
        log.info("##str#receiver1## = {}",str);
    }
}
