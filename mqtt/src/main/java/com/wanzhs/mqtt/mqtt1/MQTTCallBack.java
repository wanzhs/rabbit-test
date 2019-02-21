package com.wanzhs.mqtt.mqtt1;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class MQTTCallBack implements MqttCallback {
    //与服务器连接断开时调用
    @Override
    public void connectionLost(Throwable throwable) {
//      log.info("进入connectionLost方法，与服务器断开连接。。。。。。。。。。。。。。。。。。");
    }
    //消息到达调用此方法
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("进入messageArrived方法----->：主题：{},消息ID:{},服务质量：{},消息内容：{}",
                s,mqttMessage.getId(),mqttMessage.getQos(),new String(mqttMessage.getPayload()));
    }
    //消息发送完成时调用，并且已经收到所有确认时调用
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("进入deliveryComplete方法，与消息发送完成。。。。。。。。。。。。。。。。。。");
        try {
            log.info("消息发送是否成功:{},消息ID:{}---->{},服务质量：{},消息内容：{}",
                    iMqttDeliveryToken.isComplete(),
                    iMqttDeliveryToken.getMessageId(),
                    iMqttDeliveryToken.getMessage().getId(),
                    iMqttDeliveryToken.getMessage().getQos(),
                    new String(iMqttDeliveryToken.getMessage().getPayload()));
        } catch (MqttException e) {
            e.printStackTrace();
        }finally {
            log.info("消息发送是否成功:{},消息ID:{}",
                    iMqttDeliveryToken.isComplete(),
                    iMqttDeliveryToken.getMessageId());
        }
    }
}
