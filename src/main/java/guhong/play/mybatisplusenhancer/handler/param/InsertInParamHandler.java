package guhong.play.mybatisplusenhancer.handler.param;


import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;

/**
 * 插入操作入参处理器
 * 不同开发人员，不同项目，传参方式/习惯都不太一样。
 * 有的喜欢把实体类的字段全部拆出来放到接口入参中。如：add(String name, String id, Integer sex, String address)
 * 有的喜欢直接用实体类当入参。如：add(User user)
 * 有的喜欢用一个DTO（参数传递对象）来处理传参，这个对象最后会转为实体类。如：add(UserDTO userDto)
 *
 * 所有需要这个接口来处理参数的转化
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface InsertInParamHandler {


    /**
     * 获得用于插入数据的原始入参
     * @param entityClass 实体类型
     * @param joinPointWrapper 切入点包装类
     * @return 返回用于插入数据的入参
     */
    public Object findOriginalInParamForInsert(Class<?> entityClass, JoinPointWrapper joinPointWrapper);

    /**
     * 将入参转为用于插入的实体类
     * @param entityClass 实体类型
     * @param originalParam 原参数
     * @param joinPointWrapper 切入点包装类
     * @return 返回实体类对象
     */
    public Object toInsertEntityObject(Class<?> entityClass, Object originalParam, JoinPointWrapper joinPointWrapper);

}
