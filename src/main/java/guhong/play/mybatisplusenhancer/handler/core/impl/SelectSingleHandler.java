package guhong.play.mybatisplusenhancer.handler.core.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.base.workbox.CrudWorkBox;
import guhong.play.mybatisplusenhancer.base.workbox.SelectSingleWorkBox;
import guhong.play.mybatisplusenhancer.handler.core.CrudHandler;
import guhong.play.mybatisplusenhancer.util.ComplexQueryUtil;
import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.util.WrapperBuilder;

/**
 * 查询详情操作
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@SuppressWarnings("all")
public class SelectSingleHandler extends CrudHandler {

    private SelectSingleWorkBox workBox;

    public SelectSingleHandler(CrudWorkBox crudWorkBox) {
        super(crudWorkBox);
        workBox = (SelectSingleWorkBox) crudWorkBox;
    }

    /**
     * 执行 CRUD 操作
     * @return 返回执行结果
     */
    @Override
    @SuppressWarnings("all")
    public Object execute() {
        QueryWrapper queryWrapper = this.buildWrapper();
        Object result = this.executeSelect(queryWrapper);
        return this.buildSelectOneResult(result);
    }

    private QueryWrapper buildWrapper() {
        JoinPointWrapper joinPointWrapper = workBox.getJoinPointWrapper();
        IService service = workBox.getService();
        QueryWrapper queryWrapper = WrapperBuilder.buildQueryWrapperByMethodPoint(joinPointWrapper, service);
        String id = joinPointWrapper.getStringValue("id");
        if (StrUtil.isNotBlank(id)) {
            queryWrapper.eq("id", id);
        }
        return queryWrapper;
    }

    private Object executeSelect(QueryWrapper queryWrapper) {
        Object result = null;
        ComplexQueryUtil complexQueryUtil = workBox.getComplexQueryUtil();
        IService service = workBox.getService();
        if (complexQueryUtil.isNeedComplexQuery()) {
            result = complexQueryUtil.getSingle(queryWrapper);
        } else {
            result = service.getOne(queryWrapper);
        }
        return result;
    }


    private Object buildSelectOneResult(Object result) {
        return workBox.getResultHandler().buildSelectOneResult(result);
    }

}
