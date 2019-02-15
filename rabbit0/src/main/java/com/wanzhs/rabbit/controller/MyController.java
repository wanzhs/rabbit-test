package com.wanzhs.rabbit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanzhs.rabbit.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class MyController {
    @Resource
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/send")
    public void test() throws JsonProcessingException {
        rabbitTemplate.convertAndSend("com.wanzhs.exchange"
                , "com.wanzhs.queue",
                "peace you! brothers and sisters");
        User user = new User().setUsername("wanzhs").setPhone("1346579810").setPassword("123456");
        log.info("================================");
        rabbitTemplate.convertAndSend("com.wanzhs.exchange"
                , "com.wanzhs.queue",
                new ObjectMapper().writeValueAsString(user));
        log.info("================================");
    }

}
