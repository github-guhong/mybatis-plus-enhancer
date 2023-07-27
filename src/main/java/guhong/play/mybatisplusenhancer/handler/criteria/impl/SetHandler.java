package guhong.play.mybatisplusenhancer.handler.criteria.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import guhong.play.mybatisplusenhancer.annotation.criteria.Set;
import guhong.play.mybatisplusenhancer.base.criteria.CriteriaParam;
import guhong.play.mybatisplusenhancer.handler.criteria.CriteriaHandler;
import guhong.play.mybatisplusenhancer.util.ToolKit;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * set处理器
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@Component
@SuppressWarnings("all")
public class SetHandler extends CriteriaHandler {

    @Override
    protected CriteriaParam toDatabaseParam(CriteriaParam sourceParam) {
        Set set = (Set) paramAnnotation;
        CriteriaParam databaseParam = new CriteriaParam();
        BeanUtil.copyProperties(sourceParam, databaseParam);

        if (ToolKit.isNotAllEmpty(set.columnNames())) {
            databaseParam.formatParamNameList(set.columnNames());
        }
        if (StrUtil.isNotBlank(set.tableName())) {
            databaseParam.setTableName(set.tableName());
        }
        databaseParam.setOr(false);
        return databaseParam;
    }

    @Override
    protected void doAppend(AbstractWrapper abstractWrapper, String paramName, Object paramValue) {
        UpdateWrapper updateWrapper = (UpdateWrapper) abstractWrapper;
        updateWrapper.set(paramName, paramValue);
    }

}
