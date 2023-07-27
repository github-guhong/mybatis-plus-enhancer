package guhong.play.mybatisplusenhancer.handler.criteria.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import guhong.play.mybatisplusenhancer.annotation.criteria.LikeLeft;
import guhong.play.mybatisplusenhancer.base.criteria.CriteriaParam;
import guhong.play.mybatisplusenhancer.handler.criteria.CriteriaHandler;
import guhong.play.mybatisplusenhancer.util.ToolKit;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * left % （左模糊查询） 匹配处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@Component
@SuppressWarnings("all")
public class LikeLeftHandler extends CriteriaHandler {

    @Override
    protected CriteriaParam toDatabaseParam(CriteriaParam sourceParam) {
        LikeLeft likeLeft = (LikeLeft) paramAnnotation;
        CriteriaParam databaseParam = new CriteriaParam();
        BeanUtil.copyProperties(sourceParam, databaseParam);

        if (ToolKit.isNotAllEmpty(likeLeft.columnNames())) {
            databaseParam.formatParamNameList(likeLeft.columnNames());
        }
        if (StrUtil.isNotBlank(likeLeft.tableName())) {
            databaseParam.setTableName(likeLeft.tableName());
        }
        databaseParam.setOr(likeLeft.or());
        return databaseParam;
    }

    @Override
    protected void doAppend(AbstractWrapper abstractWrapper, String paramName, Object paramValue) {
        abstractWrapper.likeLeft(paramName, paramValue);
    }


}
