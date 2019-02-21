package com.wanzhs.mqtt.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author:wanzhongsu
 * @description: Sping  反射工具类
 * @date:2019/2/21 11:28
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> clza) {
        return getApplicationContext().getBean(clza);
    }

    public static <T> T getBean(String name, Class<T> clza) {
        return getApplicationContext().getBean(name, clza);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    private SpringUtil() {

    }
}
