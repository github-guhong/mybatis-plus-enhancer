package guhong.play.mybatisplusenhancer.handler.param.impl;


import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.exception.InParamHandleException;

import java.lang.reflect.Parameter;

/**
 * 对象传参方式，表示将整个对象传递进来的传参方式
 *
 * @author 李双凯
 * @date 2019/10/21 10:03
 */
public class EntityInParamHandler implements WholeInParamHandler {


    /**
     * 获得添加时传进来的参数
     *
     * @param entityClass   实体类型
     * @param joinPointWrapper 切入点包装类
     * @return 返回添加时传进来的参数
     */
    @Override
    public Object findOriginalInParamForInsert(Class<?> entityClass, JoinPointWrapper joinPointWrapper) {
        Parameter[] parameters = joinPointWrapper.getParameters();
        Object[] paramValues = joinPointWrapper.getParamValues();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.getType().equals(entityClass)) {
                return paramValues[i];
            }
        }
        throw new InParamHandleException("添加时无法获得传入的实体对象，请检查传参或传参处理器的选择是否正确！");
    }

    /**
     * 将传进来的参数转为实体类
     *
     * @param entityClass   实体类型
     * @param originalParam 原参数
     * @param joinPointWrapper 切入点包装类
     * @return 返回实体类
     */
    @Override
    public Object toInsertEntityObject(Class<?> entityClass, Object originalParam, JoinPointWrapper joinPointWrapper) {
        return originalParam;
    }

    /**
     * 获得更新时传进来的参数
     *
     * @param entityClass   实体类型
     * @param joinPointWrapper 切入点包装类
     * @return 返回添加时传进来的参数
     */
    @Override
    public Object findOriginalInParamForUpdate(Class<?> entityClass, JoinPointWrapper joinPointWrapper) {
        return findOriginalInParamForInsert(entityClass, joinPointWrapper);
    }

    /**
     * 将传进来的参数转为实体类
     *
     * @param entityClass   实体类型
     * @param originalParam 原参数
     * @param joinPointWrapper 切入点包装类
     * @return 返回实体类
     */
    @Override
    public Object toUpdateEntityObject(Class<?> entityClass, Object originalParam, JoinPointWrapper joinPointWrapper) {
        return toInsertEntityObject(entityClass, originalParam, joinPointWrapper);
    }
}
