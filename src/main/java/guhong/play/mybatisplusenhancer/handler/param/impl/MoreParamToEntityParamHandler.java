package guhong.play.mybatisplusenhancer.handler.param.impl;


import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;

/**
 * 多个参数传参，既把实体类拆分开的传参方式。
 * 直接把原始入参转成实体类，这个可以用于多参入参处理器但验证处理器验证的是实体类的情况
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class MoreParamToEntityParamHandler extends MoreParamInParamHandler  {


    /**
     * 获得添加时传进来的参数， 直接把入参转成实体类
     *
     * @param entityClass   实体类型
     * @param joinPointWrapper 切入点包装类
     * @return 返回添加时传进来的参数
     */
    @Override
    public Object findOriginalInParamForInsert(Class<?> entityClass, JoinPointWrapper joinPointWrapper) {
        return toInsertEntityObject(entityClass, null, joinPointWrapper);
    }

}
