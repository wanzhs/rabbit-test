package com.wanzhs.rabbit.mq_ref;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class MQMessageFactory {
    public static MQMessageFactory  getInstance(){return Singleton.INSTANCE.getInstatnce();}
    private enum Singleton {
        /**
         * 单例
         * **/
        INSTANCE;
        private MQMessageFactory singleton;
        Singleton() {
            singleton = new MQMessageFactory();
        }
        public MQMessageFactory getInstatnce() {
            return singleton;
        }
    }

    /**系统的线程数**/
    final int threadNum=2*Runtime.getRuntime().availableProcessors()+1;

    private ThreadFactory tf;
    private ExecutorService pool;
    public void initPool(){
        tf=new ThreadFactoryBuilder().setNameFormat("deal-data-%d").build();
        pool=new ThreadPoolExecutor(threadNum,200,0l,
                TimeUnit.MICROSECONDS,new LinkedBlockingDeque<>(1024),tf,new ThreadPoolExecutor.AbortPolicy());
    }
    public void submit(Runnable runnable){pool.execute(runnable);}
    public void showdown(){pool.shutdown();}
}
