package com.wanzhs.mqtt.channel;

import com.wanzhs.mqtt.util.MJsonUtil;
import com.wanzhs.mqtt.util.SpringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:luqi
 * @description: 用户主题操作
 * @date:2018/11/1_15:55
 */
@Slf4j
@Component("userTopicChannel")
public class UserTopicChannel {

    /**
     * 用户的topic+用户的userCode
     **/
    private final String TOPIC_USER = "/user/";

    private final ConcurrentHashMap<String, Channel> topicChannel = new ConcurrentHashMap<>();

    private final ByteBufAllocator ALLOCATOR = new UnpooledByteBufAllocator(false);

    /**
     * 订阅主题
     */
    public void sub(String userCode, Channel channel) {
        log.info("用户,{},订阅了用户主题", userCode);
        topicChannel.put(TOPIC_USER + userCode, channel);
    }

    /**
     * 取消订阅主题
     */
    public void unSub(String userCode) {
        log.info("用户,{},取消了==========用户主题", userCode);
        topicChannel.remove(TOPIC_USER + userCode);
    }

    /**
     * 发布主题
     */
    public <T> ChannelFuture publish(String userCode, T data) {
        String topic = TOPIC_USER + userCode;
        MJsonUtil mJsonUtil = SpringUtil.getBean("mJsonUtil", MJsonUtil.class);
        String str = mJsonUtil.ObjectToJsonStr(data);
        ByteBuf payload = ALLOCATOR.buffer();
        payload.writeBytes(str.getBytes(CharsetUtil.UTF_8));
        if (topicChannel.containsKey(topic)) {
            MqttPublishMessage message = MqttMessageBuilders.publish()
                    .topicName(topic).payload(payload).qos(MqttQoS.AT_MOST_ONCE).retained(false).build();
            Channel ctx = topicChannel.get(topic);
            if (ctx.isActive()) {
                return ctx.writeAndFlush(message);
            }
            return null;
        } else {
            log.info("主题,{},不存在,没有登录！！！！！！", topic);
        }
        return null;
    }

    /**
     * @author:luqi
     * @description: 指定通道推送
     * @date:2018/12/20_14:19
     * @param:
     * @return:
     */
    public <T> ChannelFuture publish(Channel ctx, String userCode, T data) {
        String topic = TOPIC_USER + userCode;
        MJsonUtil mJsonUtil = SpringUtil.getBean("mJsonUtil", MJsonUtil.class);
        String str = mJsonUtil.ObjectToJsonStr(data);
        ByteBuf payload = ALLOCATOR.buffer();
        payload.writeBytes(str.getBytes(CharsetUtil.UTF_8));
        if (topicChannel.containsKey(topic)) {
            MqttPublishMessage message = MqttMessageBuilders.publish()
                    .topicName(topic).payload(payload).qos(MqttQoS.AT_MOST_ONCE).retained(false).build();
            if (ctx.isActive()) {
                return ctx.writeAndFlush(message);
            }
            return null;
        } else {
            log.info("主题,{},不存在,没有登录！！！！！！", topic);
        }
        return null;
    }

}
