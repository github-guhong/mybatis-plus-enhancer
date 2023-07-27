package guhong.play.mybatisplusenhancer.handler.core.impl;

import guhong.play.mybatisplusenhancer.base.workbox.CrudWorkBox;
import guhong.play.mybatisplusenhancer.base.workbox.SelectExistWorkBox;
import guhong.play.mybatisplusenhancer.handler.core.CrudHandler;
import guhong.play.mybatisplusenhancer.util.CrudUtil;

/**
 * 查询存在操作
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class SelectExistHandler extends CrudHandler {


    private SelectExistWorkBox workBox;

    public SelectExistHandler(CrudWorkBox crudWorkBox) {
        super(crudWorkBox);
        workBox = (SelectExistWorkBox) crudWorkBox;
    }

    /**
     * 执行 CRUD 操作
     * @return 返回执行结果
     */
    @Override
    public Object execute() {
        String[] paramNames = workBox.getParamNames();
        Object[] paramValues = workBox.getParamValues();
        boolean isExist = CrudUtil.isExist(paramNames, paramValues, workBox.getService());
        return this.buildSelectExistResult(isExist);
    }

    private Object buildSelectExistResult(boolean isExist) {
        return workBox.getResultHandler().buildSelectExistResult(isExist);
    }

}
