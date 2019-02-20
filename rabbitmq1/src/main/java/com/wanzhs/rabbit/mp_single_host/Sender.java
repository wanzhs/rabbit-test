package com.wanzhs.rabbit.mp_single_host;

import com.wanzhs.rabbit.entity.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author:wanzhongsu
 * @description: 配置发送信息者（即生产者）
 * @date:2019/2/15 14:00
 */
//@RestController
public class Sender {
    @Resource
    RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/send/{abc}", method = RequestMethod.GET)
    public String test(@PathVariable(value = "abc") String abc) {
        rabbitTemplate.convertAndSend("com.wanzhs", abc + " from RabbitMQ!");
        return "abc";
    }
    @RequestMapping(value = "/send/{username}/{password}/{phone}", method = RequestMethod.GET)
    public String test1(@PathVariable(value = "username") String username,
                        @PathVariable(value = "password") String password,
                        @PathVariable(value = "phone") String phone) {
        User user=new User();
        user.setUsername(username).setPassword(password).setPhone(phone);
        rabbitTemplate.convertAndSend("spring-boot",user);
        return "user";
    }
}
