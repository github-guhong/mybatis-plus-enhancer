package guhong.play.mybatisplusenhancer.handler.verify;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 更新验证处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 * @param <V> 验证的类型
 * @param <S> IService 类。这个类型需要和自动CRUD注解的service属性相同
 **/
public interface UpdateVerifyHandler<V, S extends IService<?>> {




    /**
     * 更新验证
     * @param verifyObject 验证的对象，这个对象必须和{@link UpdateInParamHandler}.findOriginalInParamForUpdate方法获得的对象相同
     * @param service 该实体对应的service
     */
    public void updateVerify(V verifyObject, S service);

}
