package com.wanzhs.mqtt.channel;


import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:luqi
 * @description: 用户频道
 * @date:2018/11/1_13:48
 */
public class UserChannel implements BaseChannel<String, Channel> {

    private final ConcurrentHashMap<String, Channel> userMap = new ConcurrentHashMap();

    private UserChannel() {}

    public static UserChannel getInstatnce() {
        return Singleton.INSTANCE.getInstatnce();
    }

    /**
     * 充电枪 单例
     */
    private enum Singleton {
        INSTANCE;
        private UserChannel singleton;

        //JVM保证只调用一次
        Singleton() {
            singleton = new UserChannel();
        }

        public UserChannel getInstatnce() {
            return singleton;
        }
    }

    @Override
    public void add(String key, Channel value) {
        userMap.put(key, value);
    }

    @Override
    public Channel get(String key) {
        return userMap.get(key);
    }

    @Override
    public void remove(String key) {
        userMap.remove(key);
    }

    @Override
    public boolean contains(String key) {
        return userMap.containsKey(key);
    }

    @Override
    public int size() {
        return userMap.size();
    }
}
