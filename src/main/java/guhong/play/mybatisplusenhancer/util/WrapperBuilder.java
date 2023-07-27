package guhong.play.mybatisplusenhancer.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.base.criteria.Criteria;
import guhong.play.mybatisplusenhancer.base.criteria.CriteriaContainer;
import guhong.play.mybatisplusenhancer.base.criteria.CriteriaParam;
import guhong.play.mybatisplusenhancer.constants.FieldConstant;
import guhong.play.mybatisplusenhancer.constants.ValueConstant;
import guhong.play.mybatisplusenhancer.constants.enums.CrudType;
import guhong.play.mybatisplusenhancer.handler.criteria.CriteriaHandler;
import guhong.play.mybatisplusenhancer.interfaces.UserSupport;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 条件构造者
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@SuppressWarnings("all")
public class WrapperBuilder {

    /**
     * 通过方法切入点的参数来构造【查询】条件构造器
     *
     * @param joinPointWrapper 切入点包装类
     * @param service          service
     * @return 返回构建好的条件构造器
     */
    public static QueryWrapper buildQueryWrapperByMethodPoint(JoinPointWrapper joinPointWrapper, IService service) {
        return (QueryWrapper) buildWrapperByMethodPoint(joinPointWrapper, service, CrudType.SELECT);
    }


    /**
     * 通过方法切入点的参数来构造【更新】条件构造器
     *
     * @param joinPointWrapper 切入点包装类
     * @param service          service
     * @return 返回构建好的条件构造器
     */
    public static UpdateWrapper buildUpdateWrapperByMethodPoint(JoinPointWrapper joinPointWrapper, IService service) {
        return (UpdateWrapper) buildWrapperByMethodPoint(joinPointWrapper, service, CrudType.UPDATE);
    }

    /**
     * 通过方法切入点的参数来构造条件构造器
     *
     * @param joinPointWrapper 切入点包装类
     * @param service          service
     * @param crudType         操作类型，默认查询
     * @return 返回构建好的条件构造器
     */
    public static AbstractWrapper buildWrapperByMethodPoint(@NonNull JoinPointWrapper joinPointWrapper,
                                                            @NonNull IService service,
                                                            @NonNull CrudType crudType) {
        AbstractWrapper abstractWrapper = buildWrapperByCrudType(crudType);
        Object[] paramValues = joinPointWrapper.getParamValues();
        Parameter[] parameters = joinPointWrapper.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            Class<?> parameterType = parameter.getType();
            if (Criteria.class.isAssignableFrom(parameterType)) {
                // 通过条件类追加条件
                Criteria condition = (Criteria) paramValues[i];
                Field[] fields = ReflectUtil.getFields(parameterType);
                for (Field field : fields) {
                    String paramName = field.getName();
                    Object paramValue = ReflectUtil.getFieldValue(condition, field);
                    Annotation[] paramAnnotations = field.getAnnotations();
                    appendCondition(abstractWrapper, paramName, paramValue, service, paramAnnotations);
                }
            } else {
                // 通过参数追加条件
                String paramName = parameter.getName();
                Object paramValue = paramValues[i];
                Annotation[] paramAnnotations = parameter.getAnnotations();
                appendCondition(abstractWrapper, paramName, paramValue, service, paramAnnotations);
            }
        }
        return abstractWrapper;
    }


    private static AbstractWrapper buildWrapperByCrudType(CrudType crudType) {
        if (crudType.isSelect()) {
            return new QueryWrapper();
        } else {
            return new UpdateWrapper();
        }
    }

    private static void appendCondition(AbstractWrapper abstractWrapper, String paramName, Object paramValue,
                                        IService service, Annotation[] paramAnnotations) {
        if (ArrayUtil.isEmpty(paramAnnotations)) {
            return;
        }
        CriteriaParam sourceParam = createSourceParam(paramName, paramValue, service);
        if (sourceParam == null || !sourceParam.isAble()) {
            return;
        }
        List<CriteriaHandler> criteriaHandlerList = CriteriaContainer.findCriteriaHandlerList(paramAnnotations);
        if (CollectionUtil.isEmpty(criteriaHandlerList)) {
            return;
        }
        for (CriteriaHandler criteriaHandler : criteriaHandlerList) {
            criteriaHandler.appendCondition(abstractWrapper, sourceParam);
        }
    }

    private static CriteriaParam createSourceParam(String paramName, Object paramValue, IService service) {
        return new CriteriaParam(paramValue, CrudUtil.getTableNameByService(service), paramName);
    }

    /**
     * 根据构建逻辑删除的条件构造器
     *
     * @param id id
     * @return 返回条件构造器
     */
    public static <T> UpdateWrapper<T> buildLogicDeleteUpdateWrapperById(Object id) {
        return buildLogicDeleteUpdateWrapperById(id, null);
    }

    /**
     * 根据构建逻辑删除的条件构造器
     *
     * @param id     id
     * @param tClass 实体类型
     * @return 返回条件构造器
     */
    private static <T> UpdateWrapper<T> buildLogicDeleteUpdateWrapperById(Object id, Class<T> tClass) {
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        UserSupport userSupport = SpringUtil.getBean(UserSupport.class);
        updateWrapper.set(FieldConstant.UPDATE_TIME_, LocalDateTime.now())
                .set(FieldConstant.UPDATE_USER_, userSupport.getCurrentUserId())
                .set(FieldConstant.IS_DELETE_, ValueConstant.LOGIC_DELETE)
                .eq(FieldConstant.ID, id);
        return updateWrapper;
    }


    /**
     * 构建一个逻辑删除的查询条件
     *
     * @param name      业务名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回回收站业务列表
     */
    public static <T> QueryWrapper<T> buildLogicDeleteQueryWrapper(String name, LocalDateTime startTime, LocalDateTime endTime) {
        return buildLogicDeleteQueryWrapper(name, startTime, endTime, null);
    }

    /**
     * 构建一个逻辑删除的查询条件
     *
     * @param name      业务名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param tClass    类型
     * @return 返回回收站业务列表
     */
    private static <T> QueryWrapper<T> buildLogicDeleteQueryWrapper(String name, LocalDateTime startTime,
                                                                    LocalDateTime endTime, Class<T> tClass) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name), FieldConstant.NAME_, name)
                .eq(FieldConstant.IS_DELETE_, ValueConstant.LOGIC_DELETE)
                .ge(startTime != null, FieldConstant.UPDATE_USER_, startTime)
                .lt(endTime != null, FieldConstant.UPDATE_USER_, endTime);
        queryWrapper.orderByDesc(FieldConstant.UPDATE_USER_);
        return queryWrapper;
    }


}
