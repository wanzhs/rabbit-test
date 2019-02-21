package com.wanzhs.mqtt.channel;


/**
 * @author:luqi
 * @description: 频道基础类
 * @date:2018/10/12_14:09
 */
public interface BaseChannel<K, T> {

    void add(K key, T value);

    T get(K key);

    void remove(K key);

    boolean contains(K key);

    int size();
}
