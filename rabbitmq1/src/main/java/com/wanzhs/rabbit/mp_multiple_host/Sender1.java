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
public class Sender1 {
    @Resource(name = "myTemplate1")
    RabbitTemplate myTemplate1;

    @RequestMapping(value = "/send1/{abc}", method = RequestMethod.GET)
    public String testabc(@PathVariable(value = "abc") String abc) {
        log.info("sender:{}",abc);
        myTemplate1.convertAndSend(declareName, declareName,abc + " from RabbitMQ!");
        return "abc1";
    }

    @RequestMapping(value = "/send1/{username}/{password}/{phone}", method = RequestMethod.GET)
    public String testuser(@PathVariable(value = "username") String username,
                           @PathVariable(value = "password") String password,
                           @PathVariable(value = "phone") String phone) {
        User user = new User();
        user.setUsername(username).setPassword(password).setPhone(phone);
        myTemplate1.convertAndSend(declareName, user);
        return "user1";
    }
}
