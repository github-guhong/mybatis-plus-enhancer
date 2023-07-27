package guhong.play.mybatisplusenhancer.handler.criteria.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import guhong.play.mybatisplusenhancer.annotation.criteria.Ne;
import guhong.play.mybatisplusenhancer.base.criteria.CriteriaParam;
import guhong.play.mybatisplusenhancer.handler.criteria.CriteriaHandler;
import guhong.play.mybatisplusenhancer.util.ToolKit;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * ne （不等于） 匹配处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@Component
@SuppressWarnings("all")
public class NeHandler extends CriteriaHandler {



    @Override
    protected CriteriaParam toDatabaseParam(CriteriaParam sourceParam) {
        Ne ne = (Ne) paramAnnotation;
        CriteriaParam databaseParam = new CriteriaParam();
        BeanUtil.copyProperties(sourceParam, databaseParam);

        if (ToolKit.isNotAllEmpty(ne.columnNames())) {
            databaseParam.formatParamNameList(ne.columnNames());
        }
        if (StrUtil.isNotBlank(ne.tableName())) {
            databaseParam.setTableName(ne.tableName());
        }
        databaseParam.setOr(ne.or());
        return databaseParam;
    }

    @Override
    protected void doAppend(AbstractWrapper abstractWrapper, String paramName, Object paramValue) {
        abstractWrapper.ne(paramName, paramValue);
    }

}
