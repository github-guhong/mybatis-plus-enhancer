package guhong.play.mybatisplusenhancer.handler.core.impl;

import guhong.play.mybatisplusenhancer.base.workbox.CrudWorkBox;
import guhong.play.mybatisplusenhancer.base.workbox.DeleteWorkBox;
import guhong.play.mybatisplusenhancer.constants.FieldConstant;
import guhong.play.mybatisplusenhancer.constants.ValueConstant;
import guhong.play.mybatisplusenhancer.handler.core.CrudHandler;
import guhong.play.mybatisplusenhancer.handler.result.DeleteResultHandler;
import guhong.play.mybatisplusenhancer.interfaces.UserSupport;
import guhong.play.mybatisplusenhancer.util.CrudUtil;
import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.util.SpringUtil;
import guhong.play.mybatisplusenhancer.util.WrapperBuilder;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 删除操作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@SuppressWarnings("all")
public class DeleteHandler extends CrudHandler {

    private DeleteWorkBox workBox;

    public DeleteHandler(CrudWorkBox crudWorkBox) {
        super(crudWorkBox);
        workBox = (DeleteWorkBox) crudWorkBox;
    }

    /**
     * 执行 CRUD 操作
     *
     * @return 返回执行结果
     */
    @Override
    public Object execute() {
        UpdateWrapper updateWrapper = this.buildWrapper();
        boolean isSuccess = this.executeDelete(updateWrapper);
        return this.buildDeleteResult(isSuccess);
    }


    private UpdateWrapper buildWrapper() {
        JoinPointWrapper joinPointWrapper = workBox.getJoinPointWrapper();
        IService service = workBox.getService();
        UpdateWrapper updateWrapper = WrapperBuilder.buildUpdateWrapperByMethodPoint(joinPointWrapper, service);

        // 如果存在id或者ids则自动设置条件，这样方法参数就可以不用添加注解
        String id = joinPointWrapper.getStringValue(FieldConstant.ID);
        updateWrapper.eq(StrUtil.isNotBlank(id), FieldConstant.ID, id);

        Collection<String> ids = joinPointWrapper.getCollectionValue(FieldConstant.IDS);
        updateWrapper.in(CollectionUtil.isNotEmpty(ids), FieldConstant.ID, ids);
        return updateWrapper;
    }

    private boolean executeDelete(UpdateWrapper updateWrapper) {
        boolean isSuccess = false;
        IService service = workBox.getService();
        if (CrudUtil.isLogicDelete(workBox.getEntityClass())) {
            this.fillLogicDeleteWrapper(updateWrapper);
            isSuccess = service.update(updateWrapper);
        } else {
            isSuccess = service.remove(updateWrapper);
        }
        return isSuccess;
    }


    /**
     * mp的逻辑删除不会进MybatisPlusMetaObjectHandler的填充，所以这里另外做一下
     * @param updateWrapper
     */
    private void fillLogicDeleteWrapper(UpdateWrapper updateWrapper) {
        Class<?> entityClass = workBox.getEntityClass();
        Field updateTime = ReflectUtil.getField(entityClass, FieldConstant.UPDATE_TIME);
        if (updateTime != null) {
            updateWrapper.set(FieldConstant.UPDATE_TIME_, LocalDateTime.now());
        }
        Field updateUser = ReflectUtil.getField(entityClass, FieldConstant.UPDATE_USER);
        if (updateUser != null) {
            UserSupport userSupport = SpringUtil.getBean(UserSupport.class);
            updateWrapper.set(FieldConstant.UPDATE_USER_, userSupport.getCurrentUserId());
        }
        Field isDelete = ReflectUtil.getField(entityClass, FieldConstant.IS_DELETE);
        if (isDelete != null) {
            updateWrapper.set(FieldConstant.IS_DELETE_, ValueConstant.LOGIC_DELETE);
        }

    }


    private Object buildDeleteResult(boolean isSuccess) {
        DeleteResultHandler resultHandler = workBox.getResultHandler();
        return resultHandler.buildDeleteResult(isSuccess);
    }


}
