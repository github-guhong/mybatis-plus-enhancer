package guhong.play.mybatisplusenhancer.util;

import org.omg.CORBA.SystemException;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Spring 工具提供类
 * @author 李双凯
 * @date 2019/9/19 22:53
 */
@Component
@Order(1)
@SuppressWarnings("all")
public class SpringUtil implements ApplicationContextAware {
    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static boolean isAble() {
        return applicationContext != null;
    }

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        if (null == applicationContext) {
            throw new RuntimeException("applicationContext没有被加载！无法使用！");
        }
        return applicationContext;
    }



    /**
     * 通过name获取 Bean.
     * @param name beanName
     * @return 返回指定Bean
     */
    public static <T> T getBean(String name){
        return (T) getApplicationContext().getBean(name);
    }


    /**
     * 通过class获取Bean.
     * @param clazz bean类型
     * @param <T> bean类型
     * @return 返回指定Bean
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过class获得多个同类型的Bean
     * @param clazz bean类型
     * @param <T> bean类型
     * @return 返回指定的Bean
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return SpringUtil.getApplicationContext().getBeansOfType(clazz);
    }


    /**
     * 获取当前环境
     */
    public static String getActiveProfile() {
        return getApplicationContext().getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 获得项目前缀
     * @return 返回前缀
     */
    public static String getApplicationPerfix() {
        return SpringUtil.getEnvironment().getProperty("server.servlet.context-path");
    }

    /**
     * 是否测试环境
     * @return 是返回true
     */
    public static boolean isTest() {
        return "local".equals(getActiveProfile()) || "test".equals(getActiveProfile());
    }


    /**
     * 获取 目标对象
     * @param proxy 代理对象
     * @return 返回代理对象的目标对象
     */
    public static Object getTarget(Object proxy) {

        if(!AopUtils.isAopProxy(proxy)) {
            //不是代理对象
            return proxy;
        }

        try {
            if(AopUtils.isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else { //cglib
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            return proxy;
        }
    }

    /**
     * 获得环境配置对象
     */
    public static Environment getEnvironment() {
        return getApplicationContext().getEnvironment();
    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }


    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();

        return target;
    }


}
