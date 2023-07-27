package guhong.play.mybatisplusenhancer.base.workbox;

import guhong.play.mybatisplusenhancer.annotation.core.AutomationDelete;
import guhong.play.mybatisplusenhancer.handler.result.DeleteResultHandler;
import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;

/**
 * 删除操作的工具箱
 * @author : 李双凯
 * @date : 2022/3/19 20:12
 **/
@Getter
@SuppressWarnings("all")
public class DeleteWorkBox extends CrudWorkBox {

    private AutomationDelete annotation;

    private DeleteResultHandler resultHandler;

    public DeleteWorkBox(JoinPoint joinPoint) {
        super(joinPoint);
        this.annotation = this.getJoinPointWrapper().getMethodAnnotation(AutomationDelete.class);
        this.resultHandler = ReflectUtil.newInstance(annotation.resultHandler());

        super.setServiceAndEntity(annotation.service());
    }

    @Override
    public void release() {
        super.release();
        annotation = null;
        resultHandler = null;
    }
}
