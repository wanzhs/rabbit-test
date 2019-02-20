package com.wanzhs.rabbit.mp_single_host;

import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * @author:wanzhongsu
 * @description: 配置交换机 队列 交换机与队列的绑定 消息监视容器
 * @date:2019/2/15 13:27
 */
//@Configuration
@Data
public class RabbitMQConfig {
    @Value("${rabbitmq.username}")
    private String userName;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private int port;

    final static String queueName = "com.wanzhs";
    final static String exchangeName = "com.topic.wanzhs";
    final static String virtualHost = "/wanzhs1";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageConverter(integrationEventMessageConverter());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    /**
     * @author:wanzhongsu
     * @description: 消费端 rabbit监听工厂
     * @date:2019/2/15 15:02
     */
    @Bean("myListenerContainer")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(integrationEventMessageConverter());
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        MessageListenerAdapter adapter=new MessageListenerAdapter(receiver);
        adapter.setDefaultListenerMethod("receiveMessage");
        return adapter;
    }

    /**
     * @author:wanzhongsu
     * @description: 生产者
     * @date:2019/2/15 14:31
     */
    @Bean(name = "myTemplate")
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(integrationEventMessageConverter());
        template.setExchange(exchangeName);
        return template;
    }

    public MessageConverter integrationEventMessageConverter() {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        return messageConverter;
    }
}
