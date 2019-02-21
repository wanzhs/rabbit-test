package com.wanzhs.mqtt.mqtt1;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTSender {
    private final String host = MQTTConstants.host;
    private final String clientId = MQTTConstants.senderId;
    private final String userName = MQTTConstants.userName;
    private final String password = MQTTConstants.password;
    private MqttClient mqttClient;
    private MqttTopic topic;

    public MQTTSender() throws MqttException {
        mqttClient = new MqttClient(host, clientId, new MemoryPersistence());
        connect();
    }

    public void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
        options.setCleanSession(true);
        mqttClient.connect(options);
        mqttClient.setCallback(new MQTTCallBack());
        topic = mqttClient.getTopic(MQTTConstants.topic1);
    }

    public void publish(MqttTopic topic, MqttMessage message) throws MqttException {
        //将指定的消息发布到此主题，但不等待消息传递完成
        MqttDeliveryToken token = topic.publish(message);
        //阻塞当前线程，直到与此令牌关联的操作完成为止
        token.waitForCompletion();
    }

    public static void main(String[] args) throws MqttException {
        MQTTSender sender = new MQTTSender();
        MqttMessage message = new MqttMessage();
        message.setId(200);
        message.setQos(2);
        message.setPayload("hello, I am wanzhs!".getBytes());
        sender.publish(sender.topic, message);
        System.out.println("发送完成");
    }
}
