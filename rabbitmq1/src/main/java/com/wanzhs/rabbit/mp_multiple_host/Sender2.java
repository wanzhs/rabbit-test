package com.wanzhs.rabbit.mp_multiple_host;

import com.wanzhs.rabbit.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.wanzhs.rabbit.mp_multiple_host.RabbitMulConstants.declareName;

/**
 * @author:wanzhongsu
 * @description: 配置发送信息者（即生产者）
 * @date:2019/2/15 14:00
 */
@Slf4j
@RestController
public class Sender2 {
    @Resource(name = "myTemplate2")
    RabbitTemplate myTemplate2;

    @RequestMapping(value = "/send2/{abc}", method = RequestMethod.GET)
    public String test2(@PathVariable(value = "abc") String abc) {
        myTemplate2.convertAndSend(declareName, declareName,abc + " from RabbitMQ!");
        return "abc2";
    }

    @RequestMapping(value = "/send2/{username}/{password}/{phone}", method = RequestMethod.GET)
    public String testuser2(@PathVariable(value = "username") String username,
                            @PathVariable(value = "password") String password,
                            @PathVariable(value = "phone") String phone) {
        User user = new User();
        user.setUsername(username).setPassword(password).setPhone(phone);
        myTemplate2.convertAndSend(declareName,declareName,user);
        return "user2";
    }
}
