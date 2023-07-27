package guhong.play.mybatisplusenhancer.base.workbox;

import guhong.play.mybatisplusenhancer.interfaces.Releasable;
import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;

/**
 * 切入点工具箱
 * @author : 李双凯
 * @date : 2022/3/19 20:18
 **/
public class JoinPointWorkBox implements Releasable {

    @Getter
    protected JoinPointWrapper joinPointWrapper;

    public JoinPointWorkBox(JoinPoint joinPoint) {
        this.joinPointWrapper = new JoinPointWrapper(joinPoint);
    }

    @Override
    public void release() {
        joinPointWrapper = null;
    }
}
