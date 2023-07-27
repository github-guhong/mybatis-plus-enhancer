package guhong.play.mybatisplusenhancer.handler.param.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * 多个参数传参，既把实体类拆分开的传参方式。
 * 不推荐这种方式，特别是实体字段很多的时候
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class MoreParamInParamHandler implements WholeInParamHandler {

    @Override
    public Object findOriginalInParamForInsert(Class<?> entityClass, JoinPointWrapper joinPointWrapper) {
        return CollectionUtil.newArrayList(joinPointWrapper.getParamValues());
    }

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
    public Object toInsertEntityObject(Class<?> entityClass, Object originalParam, JoinPointWrapper joinPointWrapper) {
        Parameter[] parameters = joinPointWrapper.getParameters();
        Object[] paramValues = joinPointWrapper.getParamValues();
        Object result = ReflectUtil.newInstance(entityClass);
        Field[] fields = entityClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            for (int j = 0; j < parameters.length; j++) {
                Parameter parameter = parameters[j];
                String parameterName = parameter.getName();
                if (parameterName.equals(fieldName)) {
                    ReflectUtil.setFieldValue(result, field, paramValues[j]);
                    break;
                }
            }
        }
        return result;
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
