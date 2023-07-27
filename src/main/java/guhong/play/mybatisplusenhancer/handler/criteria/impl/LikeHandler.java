package guhong.play.mybatisplusenhancer.handler.criteria.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import guhong.play.mybatisplusenhancer.annotation.criteria.Like;
import guhong.play.mybatisplusenhancer.base.criteria.CriteriaParam;
import guhong.play.mybatisplusenhancer.handler.criteria.CriteriaHandler;
import guhong.play.mybatisplusenhancer.util.ToolKit;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * %% （全模糊查询） 匹配处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@Component
@SuppressWarnings("all")
public class LikeHandler extends CriteriaHandler {

    @Override
    protected CriteriaParam toDatabaseParam(CriteriaParam sourceParam) {
        Like like = (Like) paramAnnotation;
        CriteriaParam databaseParam = new CriteriaParam();
        BeanUtil.copyProperties(sourceParam, databaseParam);

        if (ToolKit.isNotAllEmpty(like.columnNames())) {
            databaseParam.formatParamNameList(like.columnNames());
        }
        if (StrUtil.isNotBlank(like.tableName())) {
            databaseParam.setTableName(like.tableName());
        }
        databaseParam.setOr(like.or());
        return databaseParam;
    }

    @Override
    protected void doAppend(AbstractWrapper abstractWrapper, String paramName, Object paramValue) {
        abstractWrapper.like(paramName, paramValue);
    }
}
