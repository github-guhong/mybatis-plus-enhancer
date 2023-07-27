package guhong.play.mybatisplusenhancer.handler.criteria;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import guhong.play.mybatisplusenhancer.base.criteria.CriteriaParam;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 条件匹配处理器
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@SuppressWarnings("all")
public abstract class CriteriaHandler {

    @Setter
    protected Annotation paramAnnotation;

    public void appendCondition(AbstractWrapper abstractWrapper, CriteriaParam sourceParam) {
        CriteriaParam databaseParam = toDatabaseParam(sourceParam);
        List<String> paramNameList = databaseParam.getParamNameList();
        if (databaseParam.getOr()) {
            if (abstractWrapper instanceof UpdateWrapper) {
                UpdateWrapper<?> updateWrapper = (UpdateWrapper<?>) abstractWrapper;
                updateWrapper.and(wrapper -> {
                    for (int i = 0; i < paramNameList.size(); i++) {
                        if (i % 2 != 0) {
                            wrapper.or();
                        }
                        doAppend(wrapper, paramNameList.get(i), databaseParam.getParamValue());
                    }
                });
            } else {
                QueryWrapper<?> queryWrapper = (QueryWrapper<?>) abstractWrapper;
                queryWrapper.and(wrapper -> {
                    for (int i = 0; i < paramNameList.size(); i++) {
                        if (i % 2 != 0) {
                            wrapper.or();
                        }
                        doAppend(wrapper, paramNameList.get(i), databaseParam.getParamValue());
                    }
                });
            }
        } else {
            for (String paramName : paramNameList) {
                doAppend(abstractWrapper, paramName, databaseParam.getParamValue());
            }
        }
    }

    protected abstract CriteriaParam toDatabaseParam(CriteriaParam sourceParam);

    protected abstract void doAppend(AbstractWrapper abstractWrapper, String paramName, Object paramValue);

}
