package guhong.play.mybatisplusenhancer.handler.verify.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.constants.enums.CrudType;
import org.springframework.stereotype.Component;

/**
 * 默认的验证处理器
 * 什么都不做
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@SuppressWarnings("all")
@Component
public class DefaultVerifyHandler<T, S extends IService<?>> implements WholeVerifyHandler<T, S> {

    /**
     * 添加验证
     *
     * @param verifyObject  实体对象
     * @param service 该实体对应的service
     */
    @Override
    public void insertVerify(T verifyObject, S service) {

    }

    /**
     * 更新验证
     *
     * @param verifyObject  实体对象
     * @param service 该实体对应的service
     */
    @Override
    public void updateVerify(T verifyObject, S service) {

    }

    /**
     * 验证，该方法自动判断添加和更新
     *
     * @param entity  实体对象
     * @param service 该实体对应的service
     * @param type    操作类型
     */
    public void verify(T entity, S service, CrudType type) {
        if (CrudType.INSERT.equals(type)) {
            insertVerify(entity, service);
        } else if (CrudType.UPDATE.equals(type)) {
            updateVerify(entity, service);
        }
    }
}
