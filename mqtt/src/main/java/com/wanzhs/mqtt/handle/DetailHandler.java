package com.wanzhs.mqtt.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:luqi
 * @description: 详情处理
 * @date:2018/11/1_15:09
 */
@Slf4j
@ChannelHandler.Sharable
public class DetailHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof MqttMessage)) return;
        MqttMessage message = (MqttMessage) msg;
        MqttMessageType messageType = message.fixedHeader().messageType();
        switch (messageType) {
            case PUBACK:
                MqttPubAckMessage pubAckMessage = (MqttPubAckMessage) message;
                PUBACK(ctx, pubAckMessage);
                break;
            case PINGREQ:
                pingDeal(ctx);
                break;
            case DISCONNECT:
                disconnectDeal(ctx);
                break;
            case UNSUBSCRIBE:
                unSubscribeDeal(ctx);
                break;
            default:
                log.info("默认请求什么也没有,{}", messageType);
                break;
        }
    }

    /**
     * 发布回调处理
     */
    private void PUBACK(ChannelHandlerContext ctx, MqttPubAckMessage pubAckMessage) {
        DecoderResult decoderResult = pubAckMessage.decoderResult();
        log.info("" + decoderResult.isSuccess());
        Object object = pubAckMessage.payload();
        System.out.println(object);
            /*pubAckMessage.fixedHeader();
            pubAckMessage.variableHeader();*/
    }

    private void pingDeal(ChannelHandlerContext ctx) {
        MqttMessage mqttMessage = new MqttMessage(new MqttFixedHeader(MqttMessageType.PINGRESP,
                false, MqttQoS.AT_MOST_ONCE,
                false, 0));
        ctx.writeAndFlush(mqttMessage);
    }

    private void disconnectDeal(ChannelHandlerContext ctx) {

        ctx.close();
    }

    /**
     * @author:luqi
     * @description: 取消订阅
     * @date:2018/12/20_15:18
     * @param:
     * @return:
     */
    private void unSubscribeDeal(ChannelHandlerContext ctx) {
        log.info("调用了取消订阅");
        MqttMessage mqttMessage = new MqttMessage(new MqttFixedHeader(MqttMessageType.UNSUBACK,
                false, MqttQoS.AT_MOST_ONCE,
                false, 0));
        ctx.writeAndFlush(mqttMessage);
    }
}
