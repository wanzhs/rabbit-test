package com.wanzhs.rabbit.mq_ref;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQMessageDispatcher {

    public static MQMessageDispatcher getInstatnce() {
        return Singleton.INSTANCE.getInstatnce();
    }


    private enum Singleton {
        /**
         * 单例
         * **/
        INSTANCE;
        private MQMessageDispatcher singleton;

        Singleton() {
            singleton = new MQMessageDispatcher();
        }

        public MQMessageDispatcher getInstatnce() {
            return singleton;
        }
    }
    public void dispatch(String str){
        MQMessageFactory.getInstance().submit(()->{
            log.info("===========================:{}",str);
        });
    }
}
