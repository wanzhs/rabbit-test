package com.wanzhs.rabbit.mq_ref;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;

import java.io.IOException;

//@Component
@Slf4j
//@RabbitListener(queues = RabbitConstants.QUEUE_TCP,containerFactory = "rabbitListenerContainerFactory")
public class RabbitMqReceiver {
    @RabbitHandler
    public void process(String msg, Channel channel, Message message){
        try {
            MQMessageDispatcher.getInstatnce().dispatch(msg);
        }catch (Exception e){
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            } catch (IOException e1) {
                log.error("收到 wanzhs.tcp.webs ACK 报错 ======= > " + e1);
            }
            log.error("收到 wanzhs.tcp.webs 消费报错 ======= > " + e);
        }
    }
}
