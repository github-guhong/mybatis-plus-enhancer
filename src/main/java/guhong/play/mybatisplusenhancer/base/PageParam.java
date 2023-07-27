package guhong.play.mybatisplusenhancer.base;

import guhong.play.mybatisplusenhancer.constants.enums.OrderType;
import guhong.play.mybatisplusenhancer.util.TextUtil;
import guhong.play.mybatisplusenhancer.util.VerifyUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 分页参数对象
 * 需要前端传入页码或限制条数，否则不会进行分页操作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class PageParam<T> {

    /**
     * 当前页码
     */
    private Long page = 1L;

    /**
     * 页面大小（每页限制条数）
     */
    private Long limit = 10L;

    /**
     * 排序字段。
     * 格式为： 排序字段 排序方式 , 排序字段 排序方式 ......
     * 如： sort desc , create_time asc
     */
    private Set<String> orderFields = CollectionUtil.newHashSet();

    /**
     * 根据排序方式获得该排序方式的所有排序字段
     *
     * @param entityType  实体类型
     * @param orderFields 排序字段数组
     * @param way         排序方式
     * @return 返回根据排序方式获得该排序方式的所有排序字段
     */
    private static Set<String> getOrderFieldByWay(@NonNull Class<?> entityType, @NonNull Set<String> orderFields, @NonNull OrderType way) {
        if (entityType == null) {
            throw new RuntimeException("请传入实体类型");
        }
        if (CollectionUtil.isEmpty(orderFields)) {
            return null;
        }
        if (way == null) {
            way = OrderType.ASC;
        }

        Set<String> list = new HashSet<>();
        for (String orderField : orderFields) {
            if (StringUtils.isNotBlank(orderField)) {
                String[] split = orderField.split(" ");
                String orderFieldName = split[0];
                String orderWay = split[1];
                VerifyUtil.verifyOrderField(entityType, orderFieldName);
                if (way.getTypeName().equals(orderWay)) {
                    list.add(orderFieldName);
                }
            }
        }
        return list;
    }

    /**
     * 获得desc排序字段
     */
    public Set<String> getDescOrderField(Class<?> verifyClass) {
        return getOrderFieldByWay(verifyClass, orderFields, OrderType.DESC);
    }

    /**
     * 获得asc排序字段
     */
    public Set<String> getAscOrderField(Class<?> verifyClass) {
        return getOrderFieldByWay(verifyClass, orderFields, OrderType.ASC);
    }

    /**
     * 添加desc排序字段
     *
     * @param fields 字段名，数据库字段名
     */
    public void addOrderFieldByDesc(String... fields) {
        for (String field : fields) {
            this.orderFields.add(TextUtil.toUnderline(field) + " " + OrderType.DESC.getTypeName());
        }
    }

    /**
     * 添加asc排序字段
     *
     * @param fields 字段名，数据库字段名
     */
    public void addOrderFieldByAsc(String... fields) {
        for (String field : fields) {
            this.orderFields.add(TextUtil.toUnderline(field) + " " + OrderType.ASC.getTypeName());
        }
    }


    public void setOrderFields(String orderFields) {
        String[] fields = orderFields.split(",");
        for (String field : fields) {
            String fieldName = field.trim().split(" ")[0];
            String way = field.trim().split(" ")[1];
            this.orderFields.add(TextUtil.toUnderline(fieldName) + " " + way);
        }
    }

    /**
     * 是否使用分页
     *
     * @return true表示使用
     */
    public boolean hasPage() {
        // 如果page不为空，limit为空，则为limit设置默认值
        if (page != null && page > 0) {
            if (limit == null || limit < 0) {
                limit = 10L;
            }
        }
        // limit不为空，page为空，则为page设置默认值
        if (limit != null && limit > 0) {
            if (page == null || page < 0) {
                page = 1L;
            }
        }
        if (page != null && limit != null && page > 0 && limit > 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否存在排序
     *
     * @return true表示使用
     */
    public boolean hasOrderBy() {
        return CollectionUtil.isNotEmpty(orderFields);
    }


    /**
     * 构建分页对象
     *
     * @return 返回分页对象
     */
    public IPage<T> buildPage() {
        if (page == null || page < 0) {
            page = 1L;
        }
        if (limit == null || limit < 0) {
            limit = 10L;
        }
        return new Page<T>(page, limit);
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }
}
