package guhong.play.mybatisplusenhancer.base.workbox;

import guhong.play.mybatisplusenhancer.annotation.core.AutomationUpdate;
import guhong.play.mybatisplusenhancer.base.service.SelfServiceImpl;
import guhong.play.mybatisplusenhancer.handler.param.UpdateInParamHandler;
import guhong.play.mybatisplusenhancer.handler.result.UpdateResultHandler;
import guhong.play.mybatisplusenhancer.handler.verify.UpdateVerifyHandler;
import cn.hutool.core.util.ReflectUtil;
import guhong.play.mybatisplusenhancer.util.SpringUtil;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;

/**
 * 更新操作工具箱
 *
 * @author : 李双凯
 * @date : 2022/3/19 20:39
 **/
@Getter
@SuppressWarnings("all")
public class UpdateWorkBox extends CrudWorkBox {

    private AutomationUpdate annotation;

    private UpdateInParamHandler inParamHandler;

    private UpdateVerifyHandler verifyHandler;

    private UpdateResultHandler resultHandler;

    public UpdateWorkBox(JoinPoint joinPoint) {
        super(joinPoint);
        this.annotation = super.joinPointWrapper.getMethodAnnotation(AutomationUpdate.class);
        this.inParamHandler = ReflectUtil.newInstance(annotation.inParamHandler());
        this.resultHandler = ReflectUtil.newInstance(annotation.resultHandler());

        super.setServiceAndEntity(annotation.service());

        if (annotation.verifyHandler().isAssignableFrom(SelfServiceImpl.class)) {
            this.verifyHandler = (UpdateVerifyHandler) super.service;
        } else {
            this.verifyHandler = SpringUtil.getBean(annotation.verifyHandler());
        }
    }

    @Override
    public void release() {
        super.release();
        annotation = null;
        inParamHandler = null;
        verifyHandler = null;
        resultHandler = null;
    }
}
