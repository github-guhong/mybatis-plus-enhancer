package guhong.play.mybatisplusenhancer.handler.core;

import guhong.play.mybatisplusenhancer.base.workbox.CrudWorkBox;
import lombok.Getter;

/**
 * 默认的增删改查处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class CrudHandler {

    @Getter
    private CrudWorkBox crudWorkBox;

    public CrudHandler(CrudWorkBox crudWorkBox) {
        this.crudWorkBox = crudWorkBox;
    }

    private CrudHandler(){}

    /**
     * 执行 CRUD 操作
     * @return 返回执行结果
     */
    public Object execute() {
        throw new UnsupportedOperationException();
    }


    public void release() {
        crudWorkBox.release();
    }
}
