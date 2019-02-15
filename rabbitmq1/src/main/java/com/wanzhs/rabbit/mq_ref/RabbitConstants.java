package com.wanzhs.rabbit.mq_ref;

public class RabbitConstants {
    /**
     * 基础数据交换
     */
    public static final String EXCHANGE_BASE_DATA = "oamm.exchange.base.data";

    /**
     * netty 数据推送 交换
     */
    public static final String EXCHANGE_TCP = "wanzhs.tcp.webs";
    /**
     * netty 数据推送 队列
     */
    public static final String QUEUE_TCP = "wanzhs.tcp.webs";
    public static final String  ROUGING_KEY_TCP = "wanzhs.tcp.webs";
    public static final String  ROUGING_KEY_ADMIN = "wanzhs.admin.webs";
    /**
     * 基础数据队列
     */
    public static final String QUEUE_BASE_DATA = "oamm.queue.base.data";




    public static final String ORG = "ORG";

    public static final String SCAN_POINT = "SCAM_POINT";

    public static final String DEV_WARNING = "DEV_WARNING" ;

    public static final String ROOM = "ROOM";

    public static final String OMA_USER = "OMA_USER";
}
