package guhong.play.mybatisplusenhancer.handler.core.impl;

import guhong.play.mybatisplusenhancer.annotation.relation.RelationInsert;
import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.base.workbox.CrudWorkBox;
import guhong.play.mybatisplusenhancer.base.workbox.InsertWorkBox;
import guhong.play.mybatisplusenhancer.exception.InParamHandleException;
import guhong.play.mybatisplusenhancer.handler.core.CrudHandler;
import guhong.play.mybatisplusenhancer.handler.param.InsertInParamHandler;
import guhong.play.mybatisplusenhancer.handler.param.impl.DTOInParamHandler;
import guhong.play.mybatisplusenhancer.handler.result.InsertResultHandler;
import guhong.play.mybatisplusenhancer.handler.verify.InsertVerifyHandler;
import guhong.play.mybatisplusenhancer.interfaces.DTO;
import guhong.play.mybatisplusenhancer.util.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 插入操作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@SuppressWarnings("all")
public class InsertHandler extends CrudHandler {

    private InsertWorkBox workBox;

    public InsertHandler(CrudWorkBox crudWorkBox) {
        super(crudWorkBox);
        this.workBox = (InsertWorkBox) crudWorkBox;
    }

    /**
     * 执行 CRUD 操作
     *
     * @param crudWorkBox 工具箱
     * @return 返回执行结果
     */
    @Override
    public Object execute() {
        Object originalInParam = this.findOriginalInParam();
        this.verify(originalInParam);
        Object entity = this.executeInsert(originalInParam);
        return this.buildInsertResult(entity);
    }

    private Object findOriginalInParam() {
        Class<?> entityClass = workBox.getEntityClass();
        JoinPointWrapper joinPointWrapper = workBox.getJoinPointWrapper();

        InsertInParamHandler inParamHandler = workBox.getInParamHandler();
        return inParamHandler.findOriginalInParamForInsert(entityClass, joinPointWrapper);
    }

    private void verify(Object originalInParam) {
        IService service = workBox.getService();
        // 先执行自定义验证
        try {
            InsertVerifyHandler verifyHandler = workBox.getVerifyHandler();
            verifyHandler.insertVerify(originalInParam, service);
        } catch (IllegalArgumentException e) {
            InsertInParamHandler inParamHandler = workBox.getInParamHandler();
            String transferParamHandlerName = inParamHandler.getClass().getSimpleName();
            String message = "参数不匹配！通过传参处理器：【" + transferParamHandlerName + "】获得的对象: 【" + originalInParam + "】与验证处理器所需的对象不匹配！";
            throw new InParamHandleException(message);
        }
        // 再执行通用验证
        VerifyUtil.verifyInsertObject(originalInParam, service);

    }


    private Object executeInsert(Object originalInParam) {
        // 把入参转为实体类对象
        Object entity = this.toInsertEntityObject(originalInParam);
        IService service = workBox.getService();

        boolean isSuccess = false;
        if (entity instanceof Collection) {
            isSuccess = service.saveBatch((Collection) entity);
        } else {
            isSuccess = service.save(entity);
        }
        if (!isSuccess) {
            return null;
        }

        // 关联插入
        executeRelationInsert(entity);

        // 回显DTO对象主键
        if (isDtoInParam()) {
            Object[] paramValues = workBox.getJoinPointWrapper().getParamValues();
            echoDtoPrimary(paramValues, entity);
        }

        return entity;
    }

    private Object toInsertEntityObject(Object insertOriginalParam) {
        InsertInParamHandler inParamHandler = workBox.getInParamHandler();
        Class<?> entityClass = workBox.getEntityClass();
        JoinPointWrapper joinPointWrapper = workBox.getJoinPointWrapper();
        return inParamHandler.toInsertEntityObject(entityClass, insertOriginalParam, joinPointWrapper);
    }


    // todo 批量增加不同relation，比如有多个字段用了@RelationInsert注解，
    //  把所有的RelationEntity根据不同的@RelationInsert创建出来，再批量添加
    private void executeRelationInsert(Object entity) {
        if (entity instanceof Collection) {
            Collection collection = (Collection) entity;
            for (Object o : collection) {
                executeRelationInsert(o);
            }
        } else {
            Field[] fields = ReflectUtil.getFields(entity.getClass());
            for (Field field : fields) {
                RelationInsert relationInsert = field.getAnnotation(RelationInsert.class);
                if (relationInsert == null) {
                    continue;
                }
                IService<?> relationService = SpringUtil.getBean(relationInsert.relationService());
                Class<?> relationEntityClass = CrudUtil.getEntityClassByService(relationService);

                Collection relationEntityList = buildRelationEntityList(relationInsert, entity, field, relationEntityClass);
                if (CollectionUtil.isEmpty(relationEntityList)) {
                    continue;
                }

                VerifyUtil.verifyInsertObject(relationEntityList, relationService);

                relationService.saveBatch(relationEntityList);
            }
        }
    }

    private Collection buildRelationEntityList(RelationInsert relationInsert, Object entity, Field field, Class relationEntityClass) {

        String originator = relationInsert.originator();
        String linker = relationInsert.linker();


        Object originatorValue = CrudUtil.getPrimaryValue(entity);
        Object linkerValueObject = ReflectUtil.getFieldValue(entity, field);

        if (linkerValueObject == null || StrUtil.isBlank(linkerValueObject.toString())) {
            return null;
        }

        Collection relationEntityList = CollectionUtil.newArrayList();
        for (String linkerValue : linkerValueObject.toString().split(",")) {

            Object relationEntity = ReflectUtil.newInstance(relationEntityClass);
            ReflectUtil.setFieldValue(relationEntity, originator, originatorValue);
            ReflectUtil.setFieldValue(relationEntity, linker, linkerValue);

            relationEntityList.add(relationEntity);
        }

        return relationEntityList;
    }



    private boolean isDtoInParam() {
        InsertInParamHandler inParamHandler = workBox.getInParamHandler();
        return inParamHandler instanceof DTOInParamHandler;
    }

    private Object buildInsertResult(Object entity) {
        InsertResultHandler resultHandler = workBox.getResultHandler();
        return resultHandler.buildInsertResult(entity != null, entity);
    }


    private void echoDtoPrimary(Object[] paramValues, Object entity) {
        if (entity instanceof Collection) {
            Collection<?> collection = (Collection<?>) entity;
            for (Object o : collection) {
                echoDtoPrimary(paramValues, o);
            }
        } else {
            Object primaryValue = CrudUtil.getPrimaryValue(entity);
            if (ToolKit.isEmpty(primaryValue)) {
                return;
            }
            for (int i = 0; i < paramValues.length; i++) {
                if (!(paramValues[i] instanceof DTO)) {
                    continue;
                }
                Object dto = paramValues[i];
                Field primaryField = CrudUtil.getPrimaryField(dto.getClass());
                if (primaryField == null) {
                    return;
                }
                ReflectUtil.setFieldValue(dto, primaryField, primaryValue);
            }
        }
    }
}
