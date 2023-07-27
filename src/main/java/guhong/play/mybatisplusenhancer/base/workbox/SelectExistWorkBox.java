package guhong.play.mybatisplusenhancer.base.workbox;

import guhong.play.mybatisplusenhancer.annotation.core.AutomationSelectExist;
import guhong.play.mybatisplusenhancer.handler.result.SelectExistResultHandler;
import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;

/**
 * @author : 李双凯
 * @date : 2022/3/19 20:12
 **/
@Getter
@SuppressWarnings("all")
public class SelectExistWorkBox extends CrudWorkBox {

    private AutomationSelectExist annotation;

    private SelectExistResultHandler resultHandler;

    public SelectExistWorkBox(JoinPoint joinPoint) {
        super(joinPoint);
        this.annotation = this.getJoinPointWrapper().getMethodAnnotation(AutomationSelectExist.class);
        this.resultHandler = ReflectUtil.newInstance(annotation.resultHandler());

        super.setServiceAndEntity(annotation.service());
    }

    @Override
    public void release() {
        super.release();
        annotation = null;
        resultHandler = null;
    }

    public String[] getParamNames() {
        return joinPointWrapper.getParamNames();
    }

    public Object[] getParamValues() {
        return joinPointWrapper.getParamValues();
    }
}
