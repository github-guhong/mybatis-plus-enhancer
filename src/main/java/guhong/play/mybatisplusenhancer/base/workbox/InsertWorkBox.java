package guhong.play.mybatisplusenhancer.base.workbox;

import guhong.play.mybatisplusenhancer.annotation.core.AutomationInsert;
import guhong.play.mybatisplusenhancer.base.service.SelfServiceImpl;
import guhong.play.mybatisplusenhancer.handler.param.InsertInParamHandler;
import guhong.play.mybatisplusenhancer.handler.result.InsertResultHandler;
import guhong.play.mybatisplusenhancer.handler.verify.InsertVerifyHandler;
import cn.hutool.core.util.ReflectUtil;
import guhong.play.mybatisplusenhancer.util.SpringUtil;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;

/**
 * 插入操作工具箱
 *
 * @author : 李双凯
 * @date : 2022/3/19 20:39
 **/
@Getter
@SuppressWarnings("all")
public class InsertWorkBox extends CrudWorkBox {

    private AutomationInsert annotation;

    private InsertInParamHandler inParamHandler;

    private InsertVerifyHandler verifyHandler;

    private InsertResultHandler resultHandler;

    public InsertWorkBox(JoinPoint joinPoint) {
        super(joinPoint);
        this.annotation = super.joinPointWrapper.getMethodAnnotation(AutomationInsert.class);
        this.inParamHandler = ReflectUtil.newInstance(annotation.inParamHandler());
        this.resultHandler = ReflectUtil.newInstance(annotation.resultHandler());

        super.setServiceAndEntity(annotation.service());

        if (annotation.verifyHandler().isAssignableFrom(SelfServiceImpl.class)) {
            this.verifyHandler = (InsertVerifyHandler) super.service;
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
