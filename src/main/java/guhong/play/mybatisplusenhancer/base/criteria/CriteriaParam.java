package guhong.play.mybatisplusenhancer.base.criteria;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.mybatisplusenhancer.util.CrudUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 条件参数
 * @author 李双凯
 */
@Data
@NoArgsConstructor
public class CriteriaParam {

    /**
     * 该参数所属的表名
     */
    private String tableName;

    private List<String> paramNameList;

    private Object paramValue;

    private Boolean or = false;


    public CriteriaParam(Object paramValue, String tableName, String... paramNames) {
        this.tableName = tableName;
        this.formatParamNameList(paramNames);
        this.setParamValue(paramValue);
    }

    public boolean isAble() {
        // 准许value为空字符串
        return CollectionUtil.isNotEmpty(paramNameList) && paramValue != null;
    }

    public void formatParamNameList(String... paramNames) {
        paramNameList = new ArrayList<>();
        for (String paramName : paramNames) {
            if (StrUtil.isBlank(paramName)) {
                continue;
            }
            paramNameList.add(tableName + "." + CrudUtil.formatField(paramName));
        }
    }


    public void setParamValue(Object paramValue) {
        if (paramValue instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) paramValue;
            // formatNormal --> yyyy-MM-dd HH:mm:ss
            paramValue = LocalDateTimeUtil.formatNormal(localDateTime);
        }
        this.paramValue = paramValue;
    }
}
