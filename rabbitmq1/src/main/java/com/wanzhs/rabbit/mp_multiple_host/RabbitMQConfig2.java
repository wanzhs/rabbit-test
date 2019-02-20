package com.wanzhs.rabbit.mp_multiple_host;

import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.wanzhs.rabbit.mp_multiple_host.RabbitMulConstants.declareName;
import static com.wanzhs.rabbit.mp_multiple_host.RabbitMulConstants.virtualHost2;

/**
 * @author:wanzhongsu
 * @description: 配置交换机 队列 交换机与队列的绑定 消息监视容器
 * @date:2019/2/15 13:27
 */
@Configuration
@Data
@Slf4j
public class RabbitMQConfig2 {
    @Value("${rabbitmq.username}")
    private String userName;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private int port;

    @Value("${rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;

    @Value("${rabbitmq.publisher-returns}")
    private boolean publisherReturns;

    @Bean("connectionFactory2")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setPublisherReturns(publisherReturns);
        connectionFactory.setVirtualHost(virtualHost2);
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("x-delayed-type", "direct");
        Channel channel = connectionFactory.createConnection().createChannel(false);
        try {
            channel.exchangeDeclare(declareName, "x-delayed-message", true, false, false, paramMap);
            channel.queueDeclare(declareName, true, false, false, null);
//            channel.exchangeBind(declareName, declareName, "com.wanzhs", null);
            channel.queueBind(declareName, declareName, declareName, null);
        } catch (Exception e) {
//            e.printStackTrace();
            log.info("rabbit mq 启动失败！！！！！！");
        }finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return connectionFactory;
    }

    /**
     * @author:wanzhongsu
     * @description: 消费端 rabbit监听工厂
     * @date:2019/2/15 15:02
     */
    @Bean("myListenerContainer2")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(integrationEventMessageConverter());
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }

    /**
     * @author:wanzhongsu
     * @description: 生产者
     * @date:2019/2/15 14:31
     */
    @Bean(name = "myTemplate2")
    RabbitTemplate rabbitTemplate(@Qualifier("connectionFactory2") ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(integrationEventMessageConverter());
        template.setExchange(declareName);
        return template;
    }

    public MessageConverter integrationEventMessageConverter() {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        return messageConverter;
    }
}
