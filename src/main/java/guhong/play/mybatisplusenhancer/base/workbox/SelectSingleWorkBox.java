package guhong.play.mybatisplusenhancer.base.workbox;

import guhong.play.mybatisplusenhancer.annotation.core.AutomationSelectSingle;
import guhong.play.mybatisplusenhancer.handler.result.SelectOneResultHandler;
import guhong.play.mybatisplusenhancer.util.ComplexQueryUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;

/**
 * @author : 李双凯
 * @date : 2022/4/15 21:30
 **/
@SuppressWarnings("all")
@Getter
public class SelectSingleWorkBox extends CrudWorkBox {

    private AutomationSelectSingle annotation;

    private SelectOneResultHandler resultHandler;

    private ComplexQueryUtil complexQueryUtil;

    public SelectSingleWorkBox(JoinPoint joinPoint) {
        super(joinPoint);
        this.annotation = super.joinPointWrapper.getMethodAnnotation(AutomationSelectSingle.class);
        this.resultHandler = ReflectUtil.newInstance(annotation.resultHandler());

        super.setServiceAndEntity(annotation.service());

        this.complexQueryUtil = new ComplexQueryUtil(annotation.voClass(), super.service, super.entityClass);
    }

    @Override
    public void release() {
        super.release();
        annotation = null;
        resultHandler = null;
        complexQueryUtil = null;
    }
}
