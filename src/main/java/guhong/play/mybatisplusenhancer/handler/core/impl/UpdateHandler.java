package guhong.play.mybatisplusenhancer.handler.core.impl;

import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.base.workbox.CrudWorkBox;
import guhong.play.mybatisplusenhancer.base.workbox.UpdateWorkBox;
import guhong.play.mybatisplusenhancer.exception.InParamHandleException;
import guhong.play.mybatisplusenhancer.exception.VerifyException;
import guhong.play.mybatisplusenhancer.handler.core.CrudHandler;
import guhong.play.mybatisplusenhancer.handler.param.UpdateInParamHandler;
import guhong.play.mybatisplusenhancer.handler.result.UpdateResultHandler;
import guhong.play.mybatisplusenhancer.handler.verify.UpdateVerifyHandler;
import guhong.play.mybatisplusenhancer.util.CrudUtil;
import guhong.play.mybatisplusenhancer.util.VerifyUtil;
import guhong.play.mybatisplusenhancer.util.WrapperBuilder;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 更新操作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@SuppressWarnings("all")
public class UpdateHandler extends CrudHandler {

    private UpdateWorkBox workBox;

    public UpdateHandler(CrudWorkBox crudWorkBox) {
        super(crudWorkBox);
        workBox = (UpdateWorkBox) crudWorkBox;
    }

    /**
     * 执行 CRUD 操作
     *
     * @return 返回执行结果
     */
    @Override
    public Object execute() {
        Object originalInParam = this.findOriginalInParam();
        this.verify(originalInParam);
        Object entity = this.executeUpdate(originalInParam);
        return this.buildUpdateResult(entity);
    }


    private Object findOriginalInParam() {
        UpdateInParamHandler inParamHandler = workBox.getInParamHandler();
        Class<?> entityClass = workBox.getEntityClass();
        JoinPointWrapper joinPointWrapper = workBox.getJoinPointWrapper();
        return inParamHandler.findOriginalInParamForUpdate(entityClass, joinPointWrapper);
    }


    private void verify(Object originalInParam) {
        IService service = workBox.getService();
        // 先执行自定义验证
        try {
            UpdateVerifyHandler verifyHandler = workBox.getVerifyHandler();
            verifyHandler.updateVerify(originalInParam, service);
        } catch (IllegalArgumentException e) {
            UpdateInParamHandler inParamHandler = workBox.getInParamHandler();
            String transferParamHandlerName = inParamHandler.getClass().getSimpleName();
            String message = "参数不匹配！通过传参处理器：【" + transferParamHandlerName + "】获得的对象: 【" + originalInParam + "】与验证处理器所需的对象不匹配！";
            throw new InParamHandleException(message);
        }
        // 再执行通用验证
        VerifyUtil.verifyUpdateObject(originalInParam, service);
    }

    private Object executeUpdate(Object originalInParam) {
        Object entity = this.toUpdateEntityObject(originalInParam);
        IService service = workBox.getService();
        boolean isSuccess = false;

        // 如果主键存在值，那就根据主键来更新，否则使用条件更新
        Object primaryValue = CrudUtil.getPrimaryValue(entity);
        if (primaryValue != null) {
            if (!CrudUtil.isExistById(primaryValue, service)) {
                throw new VerifyException("该数据已不存在！");
            }
            isSuccess = service.updateById(entity);
        } else {
            UpdateWrapper updateWrapper = WrapperBuilder.buildUpdateWrapperByMethodPoint(workBox.getJoinPointWrapper(), service);
            isSuccess = service.update(updateWrapper);
        }

        if (!isSuccess) {
            return null;
        }
        return entity;
    }

    private Object toUpdateEntityObject(Object updateOriginalParam) {
        UpdateInParamHandler inParamHandler = workBox.getInParamHandler();
        Class<?> entityClass = workBox.getEntityClass();
        JoinPointWrapper joinPointWrapper = workBox.getJoinPointWrapper();
        return inParamHandler.toUpdateEntityObject(entityClass, updateOriginalParam, joinPointWrapper);
    }

    private Object buildUpdateResult(Object entity) {
        UpdateResultHandler resultHandler = workBox.getResultHandler();
        return resultHandler.buildUpdateResult(entity != null, entity);
    }


}
