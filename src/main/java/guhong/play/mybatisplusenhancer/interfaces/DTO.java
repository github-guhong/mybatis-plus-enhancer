package guhong.play.mybatisplusenhancer.interfaces;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import guhong.play.mybatisplusenhancer.util.ToolKit;

/**
 * DTO对象，每个DTO类都应该实现该接口
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 * <T> 目标类型
 **/
public interface DTO<T> {

    /**
     * 转换为目标类型
     * @param dto dto对象
     * @return 返回源对象
     */
    @SuppressWarnings("all")
    public default T toGeneric() {
        // 获得接口的第一个泛型类型，即获得DtoObject接口中的第一个泛型类型，也就是实体类的类型
        Class<T> entityClass = ToolKit.getGenericByInterface(this.getClass(), DTO.class, 0);
        T source = ReflectUtil.newInstance(entityClass);
        BeanUtil.copyProperties(this, source);
        return source;
    }


}
