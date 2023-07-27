package guhong.play.mybatisplusenhancer.handler.param.impl;


import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.exception.InParamHandleException;
import guhong.play.mybatisplusenhancer.interfaces.DTO;

/**
 * DTO传参方式，表示将整个对象传递进来的传参方式
 *
 * @author 李双凯
 * @date 2019/10/21 10:03
 */
public class DTOInParamHandler implements WholeInParamHandler {


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
            if (paramValues[i] instanceof DTO) {
                return paramValues[i];
            }
        }
        throw new InParamHandleException("添加时无法获得DTO对象，请确保你的DTO对象实现了" + DTO_CLASS_PATH + "接口！");
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
        if (originalParam instanceof DTO) {
            return ((DTO<?>) originalParam).toGeneric();
        }
        throw new InParamHandleException("无法将DTO对象转换为实体类，" + originalParam + "没有实现" + DTO_CLASS_PATH + "接口");
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
