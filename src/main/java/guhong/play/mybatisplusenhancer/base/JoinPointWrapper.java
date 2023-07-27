package guhong.play.mybatisplusenhancer.base;

import guhong.play.mybatisplusenhancer.exception.MethodRetrievalFailureException;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;

/**
 * JoinPoint包装类
 * 封装获取切面中的方法、获取方法参数等方法
 *
 * @author 李双凯
 * @date 2019/9/15 19:06
 */
@Data
public class JoinPointWrapper {

    private final JoinPoint joinPoint;

    public JoinPointWrapper(JoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    /**
     * 获得连接点中的方法
     *
     * @return 返回方法对象
     */
    public Method getMethod() {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        Method realMethod = null;
        try {
            // 如果目标对象已经是个代理对象。就无法获得真正的目标对象，导致报错。
            realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(ms.getName(), method.getParameterTypes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (realMethod == null) {
            throw new MethodRetrievalFailureException();
        }
        return realMethod;
    }


    public String getMethodName() {
        return getMethod().getName();
    }

    public String getWholeMethodName() {
        Method method = getMethod();
        String className = method.getDeclaringClass().getName();
        return className + "." + method.getName();
    }


    /**
     * 获得方法参数名数组
     *
     * @return 返回方法参数名字数组
     */
    public String[] getParamNames() {
        return ((MethodSignature) joinPoint.getSignature()).getParameterNames();
    }

    /**
     * 获得方法参数对象列表
     *
     * @return 返回方法参数对象列表
     */
    public Parameter[] getParameters() {
        return this.getMethod().getParameters();
    }

    /**
     * 获得方法中的参数值
     *
     * @return 返回参数值列表
     */
    public Object[] getParamValues() {
        return joinPoint.getArgs();
    }

    /**
     * 获得指定参数名字的值
     *
     * @param name 参数名
     * @return 返回参数值，如果没有则返回null
     */
    public Object getObjectValue(String name) {
        String[] paramNames = this.getParamNames();
        Object[] paramValues = this.getParamValues();
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            if (paramName.equals(name)) {
                return paramValues[i];
            }
        }
        return null;
    }

    /**
     * 获得指定参数的string值
     * @param name 参数名
     * @return 返回参数值，如果没有则返回null
     */
    public String getStringValue(String name) {
        Object paramValue = this.getObjectValue(name);
        if (paramValue != null) {
            return paramValue.toString();
        }
        return null;
    }

    /**
     * 获得指定参数的int值
     * @param name 参数名
     * @return 返回参数值，如果没有则返回null
     */
    public Integer getIntegerValue(String name) {
        Object value = this.getObjectValue(name);
        if (value != null) {
            if (value instanceof Integer) {
                return Integer.parseInt(value.toString());
            }
        }
        return null;
    }

    public <T> Collection<T> getCollectionValue(String name) {
        Object value = this.getObjectValue(name);
        if (value != null) {
            if (value instanceof Collection) {
                return (Collection<T>) value;
            }
        }
        return null;
    }


    /**
     * 获得指定参数的boolean值
     * @param name 参数名
     * @return 返回参数值，如果没有则返回null
     */
    public boolean getBooleanValue(String name) {
        Object value = this.getObjectValue(name);
        if (value != null) {
            if (value instanceof Boolean) {
                return Boolean.parseBoolean(value.toString());
            }
        }
        return false;
    }

    /**
     * 获得方法上的注解
     *
     * @param annotationClass 注解类型
     * @return 返回方法上的注解
     */
    public <T extends Annotation> T getMethodAnnotation(Class<T> annotationClass) {
        return this.getMethod().getAnnotation(annotationClass);
    }

    /**
     * 获得类上面的注解
     *
     * @param annotationClass 注解类型
     * @return 返回类上的注解
     */
    public <T extends Annotation> T getClassAnnotation(Class<T> annotationClass) {
        return this.getMethod().getClass().getAnnotation(annotationClass);
    }


    public Class<?> getMethodReturnType() {
        return this.getMethod().getReturnType();
    }


}
