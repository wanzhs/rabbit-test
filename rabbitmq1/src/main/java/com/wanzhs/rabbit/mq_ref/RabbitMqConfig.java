package com.wanzhs.rabbit.mq_ref;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:wanzhongsu
 * @description: 配置参考类
 * @date:2019/2/15 12:56
 */
//@Configuration
//@EnableRabbit
public class RabbitMqConfig {
    @Value("${rabbitmq.username}")
    private String userName;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private int port;

    private final String virtualHostTcp="/wanzhs/tcp";

    private final String virtualHostAdmin="/wanzhs/admin";


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHostTcp);
        return connectionFactory;
    }

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(RabbitConstants.EXCHANGE_TCP);
    }

    @Bean
    public Queue simpleQueue() {
        return new Queue(RabbitConstants.QUEUE_TCP);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        template.setRoutingKey(RabbitConstants.ROUGING_KEY_TCP);
        return template;
    }

    @Bean
    public SimpleMessageListenerContainer userListenerContainer() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory());
        listenerContainer.setQueues(simpleQueue());
        listenerContainer.setMessageConverter(jsonMessageConverter());
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return listenerContainer;
    }
    @Bean("rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                                            MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
    @Bean("baseDataExchange")
    public FanoutExchange baseDataExchange() {
        Map<String, Object> args = new HashMap<>();
        FanoutExchange exchange = new FanoutExchange(RabbitConstants.EXCHANGE_BASE_DATA, true, false, args);
        return exchange;
    }

    @Bean("queueBaseData")
    public Queue queueBaseData() {
        return new Queue(RabbitConstants.QUEUE_BASE_DATA);
    }

    @Bean("bindingBaseData")
    public Binding bindingBaseData(@Qualifier("baseDataExchange") FanoutExchange baseDataExchange,
                                   @Qualifier("queueBaseData") Queue queueBaseData) {
        return BindingBuilder.bind(queueBaseData).to(baseDataExchange);
    }

    @Bean("connectionFactoryAdmin")
    public ConnectionFactory connectionFactoryAdmin() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHostAdmin);
        return connectionFactory;
    }

    @Bean("rabbitTemplateAdmin")
    public RabbitTemplate rabbitTemplateAdmin() {
        RabbitTemplate template = new RabbitTemplate(connectionFactoryAdmin());
        template.setRoutingKey(RabbitConstants.ROUGING_KEY_ADMIN);
        return template;
    }

    @Bean("userListenerContainerAdmin")
    public SimpleMessageListenerContainer userListenerContainerAdmin() {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactoryAdmin());
        listenerContainer.setQueues(queueBaseData());
        listenerContainer.setMessageConverter(jsonMessageConverter());
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }

    @Bean("rabbitListenerContainerFactoryAdmin")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactoryAdmin(
            @Qualifier("connectionFactoryAdmin") ConnectionFactory connectionFactoryAdmin,
            @Qualifier("messageConverterAdmin") MessageConverter messageConverterAdmin
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactoryAdmin);
        factory.setMessageConverter(messageConverterAdmin);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    @Bean("messageConverterAdmin")
    public MessageConverter messageConverterAdmin() {
        return new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter());
    }

    @Bean("baseDataExchangeAdmin")
    public FanoutExchange baseDataExchangeAdmin() {
        Map<String, Object> args = new HashMap<>();
        FanoutExchange exchange = new FanoutExchange(RabbitConstants.EXCHANGE_BASE_DATA, true, false, args);
        return exchange;
    }

    @Bean("queueBaseDataAdmin")
    public Queue queueBaseDataAdmin() {
        return new Queue(RabbitConstants.QUEUE_BASE_DATA);
    }

    @Bean
    public Binding bindingOrderAdmin(@Qualifier("baseDataExchangeAdmin") FanoutExchange baseDataExchange,
                                     @Qualifier("queueBaseDataAdmin") Queue queueBaseData) {
        return BindingBuilder.bind(queueBaseData).to(baseDataExchange);
    }
}
