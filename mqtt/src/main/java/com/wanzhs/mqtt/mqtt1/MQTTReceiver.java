package com.wanzhs.mqtt.mqtt1;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public class MQTTReceiver {
    private final String host = MQTTConstants.host;
    private final String clientId = MQTTConstants.receiverId;
    private final String userName = MQTTConstants.userName;
    private final String password = MQTTConstants.password;
    private MqttClient mqttClient;
    private MqttTopic topic;

    public MQTTReceiver() throws MqttException {
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
    public void subscribe(String[] topic,int[] qos) throws MqttException {
        mqttClient.subscribe(topic,qos);
    }
    public static void  main(String[] args) throws MqttException {
        MQTTReceiver receiver=new MQTTReceiver();
        String[] topic={MQTTConstants.topic1};
        int[] qos={2};
        receiver.subscribe(topic,qos);
    }

}
