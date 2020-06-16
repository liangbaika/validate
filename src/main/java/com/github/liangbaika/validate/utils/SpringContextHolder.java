package com.github.liangbaika.validate.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring的ApplicationContext的持有者,可以用静态方法的方式获取spring容器中的bean
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext(null, null);
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        assertApplicationContext(beanName, null);
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertApplicationContext(null, requiredType);
        return applicationContext.getBean(requiredType);
    }

    private static void assertApplicationContext(String beanName, Class requiredType) {
        if (SpringContextHolder.applicationContext == null) {
            throw new RuntimeException("applicaitonContext属性为null,请检查是否注入了SpringContextHolder!");
        }
        if (beanName != null) {
            boolean have = applicationContext.containsBean(beanName);
            if (!have) {
                throw new RuntimeException("无此bean 或者此bean没被spring容器管理 ");
            }
        }
        if (requiredType != null) {
            String[] beanNamesForType = applicationContext.getBeanNamesForType(requiredType);
            if (beanNamesForType == null || beanNamesForType.length == 0) {
                throw new RuntimeException("此bean没被spring容器管理 ");
            }
            if (beanNamesForType.length != 1) {
                throw new RuntimeException("有多个类型的bean 不支持class类型获取 请使用名字获取此bean");
            }
            boolean have = applicationContext.containsBean(beanNamesForType[0]);
            if (!have) {
                throw new RuntimeException("此bean没被spring容器管理 ");
            }
        }

    }

}
