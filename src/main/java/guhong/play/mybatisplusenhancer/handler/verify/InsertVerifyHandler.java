package guhong.play.mybatisplusenhancer.handler.verify;

import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.handler.param.InsertInParamHandler;

/**
 * 添加验证处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 * @param <V> 验证的类型
 * @param <S> IService 类。这个类型需要和自动CRUD注解的service属性相同
 **/
public interface InsertVerifyHandler<V, S extends IService<?>> {

    /**
     * 添加验证
     * @param verifyObject 验证的对象，这个对象必须和{@link InsertInParamHandler}.findOriginalInParamForInsert方法获得的对象相同
     * @param service 该实体对应的service
     */
    public void insertVerify(V verifyObject, S service);

}
