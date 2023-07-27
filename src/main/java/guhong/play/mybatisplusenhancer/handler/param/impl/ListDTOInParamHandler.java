package guhong.play.mybatisplusenhancer.handler.param.impl;


import cn.hutool.core.collection.CollectionUtil;
import guhong.play.mybatisplusenhancer.exception.InParamHandleException;
import guhong.play.mybatisplusenhancer.interfaces.DTO;
import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.util.ToolKit;

import java.util.Collection;

/**
 * List<DTO>传参方式，表示将整个对象传递进来的传参方式
 *
 * @author 李双凯
 * @date 2019/10/21 10:03
 */
@SuppressWarnings("all")
public class ListDTOInParamHandler implements WholeInParamHandler {


    private static final String DTO_CLASS_PATH = DTO.class.getName();

    /**
     * 获得添加时传进来的参数
     *
     * @param entityClass      实体类型
     * @param joinPointWrapper 切入点包装类
     * @return 返回添加时传进来的参数
     */
    @Override
    public Object findOriginalInParamForInsert(Class<?> entityClass, JoinPointWrapper joinPointWrapper) {
        Object[] paramValues = joinPointWrapper.getParamValues();
        for (int i = 0; i < paramValues.length; i++) {
            Object paramValue = paramValues[i];
            if (paramValue instanceof Collection) {
                Collection collection = (Collection) paramValue;
                if (CollectionUtil.isEmpty(collection)) {
                    continue;
                }
                Class<?> elementType = CollectionUtil.getElementType(collection);
                if (ToolKit.findInterfaceClassInImpl(elementType, DTO.class) != null) {
                    return paramValue;
                }
            }
        }
        throw new InParamHandleException("添加时无法获得List<DTO>对象，请确保你的入参是List<DTO>且不为空！");
    }

    /**
     * 将传进来的参数转为实体类
     *
     * @param entityClass      实体类型
     * @param originalParam    原参数
     * @param joinPointWrapper 切入点包装类
     * @return 返回实体类
     */
    @Override
    public Object toInsertEntityObject(Class<?> entityClass, Object originalParam, JoinPointWrapper joinPointWrapper) {
        Collection result = CollectionUtil.newArrayList();
        for (Object o : (Collection) originalParam) {
            Object entity = ((DTO<?>) o).toGeneric();
            result.add(entity);
        }
        return result;
    }

    /**
     * 获得更新时传进来的参数
     *
     * @param entityClass      实体类型
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
     * @param entityClass      实体类型
     * @param originalParam    原参数
     * @param joinPointWrapper 切入点包装类
     * @return 返回实体类
     */
    @Override
    public Object toUpdateEntityObject(Class<?> entityClass, Object originalParam, JoinPointWrapper joinPointWrapper) {
        return toInsertEntityObject(entityClass, originalParam, joinPointWrapper);
    }
}
