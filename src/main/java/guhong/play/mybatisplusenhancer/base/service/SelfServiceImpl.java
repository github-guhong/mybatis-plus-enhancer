package guhong.play.mybatisplusenhancer.base.service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.handler.verify.InsertVerifyHandler;
import guhong.play.mybatisplusenhancer.handler.verify.UpdateVerifyHandler;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * 表示是处理器就是自身Service。实际上这个类什么都不会做，只是一个标识
 * 适用于 ：
 * 1、Automation* 中servie属性
 * 2、AutomationInsert 和 AutomationUpdate 中的verifyHandler属性
 *
 * @author : 李双凯
 * @date : 2022/4/19 13:35
 **/
@SuppressWarnings("all")
public class SelfServiceImpl implements IService<Object>, InsertVerifyHandler, UpdateVerifyHandler {

    @Override
    public boolean saveBatch(Collection<Object> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Object> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<Object> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(Object entity) {
        return false;
    }

    @Override
    public Object getOne(Wrapper<Object> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Object> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<Object> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<Object> getBaseMapper() {
        return null;
    }

    @Override
    public void insertVerify(Object verifyObject, IService service) {

    }

    @Override
    public void updateVerify(Object verifyObject, IService service) {

    }
}
