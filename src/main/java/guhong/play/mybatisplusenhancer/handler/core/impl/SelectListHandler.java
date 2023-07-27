package guhong.play.mybatisplusenhancer.handler.core.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.base.PageParam;
import guhong.play.mybatisplusenhancer.base.workbox.CrudWorkBox;
import guhong.play.mybatisplusenhancer.base.workbox.SelectListWorkBox;
import guhong.play.mybatisplusenhancer.handler.core.CrudHandler;
import guhong.play.mybatisplusenhancer.handler.result.SelectListResultHandler;
import guhong.play.mybatisplusenhancer.util.ComplexQueryUtil;
import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.util.WrapperBuilder;

import java.util.List;
import java.util.Set;

/**
 * 查询列表处理器
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@SuppressWarnings("all")
public class SelectListHandler extends CrudHandler {

    private SelectListWorkBox workBox;

        public SelectListHandler(CrudWorkBox crudWorkBox) {
        super(crudWorkBox);
        workBox = (SelectListWorkBox) crudWorkBox;
    }

    /**
     * 执行 CRUD 操作
     *
     * @return 返回执行结果
     */
    @Override
    public Object execute() {
        AbstractWrapper queryWrapper = this.buildWrapper();
        Object result = this.executeSelect(queryWrapper);
        return this.buildSelectListResult(result);
    }



    private AbstractWrapper buildWrapper() {
        return WrapperBuilder.buildQueryWrapperByMethodPoint(workBox.getJoinPointWrapper(), workBox.getService());
    }

    private Object executeSelect(AbstractWrapper queryWrapper) {
        PageParam pageParam = this.getPageParam();
        Object result = null;
        if (pageParam != null) {
            result = this.executePageSelect(pageParam, queryWrapper);
        } else {
            result = this.executeListSelect(queryWrapper);
        }
        return result;
    }

    /**
     * 获得分页参数对象
     *
     * @return 返回分页参数对象
     */
    private PageParam getPageParam() {
        JoinPointWrapper joinPointWrapper = workBox.getJoinPointWrapper();
        // 根据名字：“pageParam”去查找PageParam对象
        Object pageParam = joinPointWrapper.getObjectValue("pageParam");
        if (pageParam != null) {
            if (pageParam instanceof PageParam) {
                return (PageParam) pageParam;
            }
        }
        // 根据名字没找到，则通过类型去找
        Object[] parameters = joinPointWrapper.getParamValues();
        if (parameters != null) {
            for (Object parameter : parameters) {
                if (parameter instanceof PageParam) {
                    return (PageParam) parameter;
                }

            }
        }
        return null;
    }

    private IPage executePageSelect(PageParam pageParam, AbstractWrapper queryWrapper) {
        this.setOrderByPageParam(pageParam, queryWrapper);

        ComplexQueryUtil complexQueryUtil = workBox.getComplexQueryUtil();
        IPage page = pageParam.buildPage();

        if (complexQueryUtil.isNeedComplexQuery()) {
            page = complexQueryUtil.page(page, queryWrapper);
        } else {
            IService service = workBox.getService();
            page = service.page(page, queryWrapper);
        }

        return page;
    }

    private void setOrderByPageParam(PageParam pageParam, AbstractWrapper queryWrapper) {
        if (!pageParam.hasOrderBy()) {
            return;
        }
        Class<?> entityClass = workBox.getEntityClass();
        Set descOrderField = pageParam.getDescOrderField(entityClass);
        Set ascOrderField = pageParam.getAscOrderField(entityClass);
        queryWrapper.orderByDesc(CollectionUtil.isNotEmpty(descOrderField), ArrayUtil.toArray(descOrderField, String.class));
        queryWrapper.orderByAsc(CollectionUtil.isNotEmpty(ascOrderField), ArrayUtil.toArray(ascOrderField, String.class));
    }

    private Object executeListSelect(AbstractWrapper queryWrapper) {
        List list = CollectionUtil.newArrayList();
        ComplexQueryUtil complexQueryUtil = workBox.getComplexQueryUtil();
        if (complexQueryUtil.isNeedComplexQuery()) {
            list = complexQueryUtil.list(queryWrapper);
        } else {
            IService service = workBox.getService();
            list = service.list(queryWrapper);
        }
        return list;
    }


    private Object buildSelectListResult(Object result) {
        SelectListResultHandler resultHandler = workBox.getResultHandler();
        return resultHandler.buildSelectListResult(result);
    }


}
