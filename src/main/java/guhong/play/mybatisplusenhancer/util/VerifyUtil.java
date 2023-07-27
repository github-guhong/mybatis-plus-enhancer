package guhong.play.mybatisplusenhancer.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.annotation.verify.RelationVerify;
import guhong.play.mybatisplusenhancer.annotation.verify.UniqueVerify;
import guhong.play.mybatisplusenhancer.constants.enums.CrudType;
import guhong.play.mybatisplusenhancer.exception.VerifyException;
import lombok.Data;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
/**
 * 验证工具类
 * 添加、修改时的验证
 * 自定义排序字段的验证
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@SuppressWarnings("all")
public class VerifyUtil {

    private static final String[] NOT_VERIFY_FIELD_ARRAY = new String[]{"serialVersionUID"};


    /**
     * 手动完成对一个对象的基础验证
     * 验证不通过则抛出异常
     *
     * @param verifyObject 验证的对象
     * @param groups       验证组
     */
    public static void baseValidate(@NonNull Object verifyObject, @NonNull Class<?>... groups) {
        Validator validator = SpringUtil.getBean("validator");
        Set<ConstraintViolation<Object>> violationSet = validator.validate(verifyObject, groups);
        if (CollectionUtil.isNotEmpty(violationSet)) {
            throw new ConstraintViolationException(violationSet);
        }
    }


    /**
     * 验证排序字段是否合法
     *
     * @param entityType 实体类型
     * @param verifyName 验证的字符
     */

    public static void verifyOrderField(@NonNull Class<?> entityType, @NonNull String verifyName) {
        Field[] fields = entityType.getDeclaredFields();
        verifyName = TextUtil.toCamel(verifyName);
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            if (name.equals(verifyName)) {
                return;
            } else {
                if (i == fields.length - 1) {
                    throw new VerifyException(verifyName + " 是一个错误的字段！");
                }
            }
        }
    }

    /**
     * 验证参数，自动完成：关联验证、唯一验证
     *
     * @param verifyObject 验证的对象
     * @param service      该实体对应的service
     */
    public static void verifyInsertObject(@NonNull Object verifyObject, @NonNull IService service) {
        verifyObject(verifyObject, service, CrudType.INSERT);
    }

    /**
     * 验证参数，自动完成：关联验证、唯一验证
     *
     * @param verifyObject 验证的对象
     * @param service      该实体对应的service
     */
    public static void verifyUpdateObject(@NonNull Object verifyObject, @NonNull IService service) {
        verifyObject(verifyObject, service, CrudType.UPDATE);
    }


    /**
     * 验证参数，自动完成：关联验证、唯一验证
     *
     * @param verifyObject 验证的对象
     * @param service      该实体对应的service
     * @param crudType     操作类型
     */
    private static void verifyObject(@NonNull Object verifyObject, @NonNull IService service, @NonNull CrudType crudType) {
        if (verifyObject instanceof Collection) {
            Collection collection = (Collection) verifyObject;
            for (Object o : collection) {
                verifyObject(o, service, crudType);
            }
        } else {
            if (ToolKit.isBaseType(verifyObject.getClass())) {
                return;
            }
            // 唯一验证，获得class上的@UniqueVerify进行多字段唯一验证
            VerifyUtil.uniqueVerifyByClass(verifyObject, service, crudType);

            Field[] fields = verifyObject.getClass().getDeclaredFields();
            if (ArrayUtil.isEmpty(fields)) {
                return;
            }
            for (Field field : fields) {
                // 判断是否不字段需要验证
                if (VerifyUtil.isNoNeedVerify(field)) {
                    continue;
                }
                // 唯一验证，验证单个字段
                VerifyUtil.uniqueVerifyByField(verifyObject, field, service, crudType);
                // 关联验证
                VerifyUtil.associationVerify(verifyObject, field);
            }
        }

    }

    /**
     * 指定字段是否不需要验证
     *
     * @param field 字段对象
     * @return 需要验证返回true
     */
    private static boolean isNoNeedVerify(Field field) {
        if (field == null) {
            return true;
        }
        String name = field.getName();
        if (ArrayUtil.contains(NOT_VERIFY_FIELD_ARRAY, name)) {
            return true;
        }
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null && !tableField.exist()) {
            return true;
        }
        return false;
    }

    /**
     * 关联验证方法
     * 如果验证的数据不存在则抛出异常！
     *
     * @param verifyObject 验证的对象
     * @param field        验证的字段
     */
    private static void associationVerify(@NonNull Object verifyObject, @NonNull Field field) {

        RelationVerify associationVerify = field.getAnnotation(RelationVerify.class);
        if (associationVerify == null) {
            return;
        }
        String relationPrimaryName = TextUtil.toUnderline(associationVerify.relationPrimaryName());
        Class<? extends IService> associationService = associationVerify.relationService();
        IService service = SpringUtil.getBean(associationService);
        Object value = ReflectUtil.getFieldValue(verifyObject, field);
        boolean result = CrudUtil.isExistByOne(relationPrimaryName, value, service);
        if (!result) {
            String errorMessage = associationVerify.errorMessage();
            if (StrUtil.isBlank(errorMessage)) {
                errorMessage = "关联验证不通过,不存在 " + value + " 值！";
            }
            throw new VerifyException(errorMessage);
        }

    }


    /**
     * 对某个字段名的唯一验证
     *
     * @param verifyObject 验证的对象
     * @param fieldName    验证的字段名
     * @param service      查询服务类
     * @param crudType     操作类型
     */
    public static void uniqueVerifyByFieldName(@NonNull Object verifyObject, @NonNull String fieldName,
                                               @NonNull IService service, @NonNull CrudType crudType) {

        Field field = ReflectUtil.getField(verifyObject.getClass(), fieldName);
        if (field == null) {
            return;
        }
        VerifyUtil.uniqueVerifyByField(verifyObject, field, service, crudType);

    }

    /**
     * 对某个字段的唯一验证
     *
     * @param verifyObject 验证的对象
     * @param field        验证的字段
     * @param service      查询服务类
     * @param crudType     操作类型
     */
    private static void uniqueVerifyByField(@NonNull Object verifyObject, @NonNull Field field,
                                            @NonNull IService service, @NonNull CrudType crudType) {

        UniqueVerify uniqueVerify = field.getAnnotation(UniqueVerify.class);
        if (uniqueVerify == null) {
            return;
        }
        String[] columns = uniqueVerify.columns();
        if (ArrayUtil.isEmpty(columns)) {
            columns = new String[]{field.getName()};
        } else {
            columns = new String[]{columns[0]};
        }
        VerifyUtil.uniqueVerify(verifyObject, service, crudType, uniqueVerify.errorMessage(), columns);

    }

    /**
     * 基于class上的UniqueVerify来验证
     *
     * @param verifyObject 验证的对象
     * @param service      查询服务类
     * @param crudType     操作类型
     */
    private static void uniqueVerifyByClass(@NonNull Object verifyObject, @NonNull IService service, @NonNull CrudType crudType) {
        UniqueVerify uniqueVerify = verifyObject.getClass().getAnnotation(UniqueVerify.class);
        if (uniqueVerify == null) {
            return;
        }
        String[] columns = uniqueVerify.columns();
        if (ArrayUtil.isNotEmpty(columns)) {
            VerifyUtil.uniqueVerify(verifyObject, service, crudType, uniqueVerify.errorMessage(), columns);
        }
    }


    /**
     * 唯一验证
     *
     * @param verifyObject  验证的对象
     * @param service       查询服务类
     * @param crudType      操作类型
     * @param verifyColumns 验证的字段
     */
    private static void uniqueVerify(@NonNull Object verifyObject, @NonNull IService service,
                                     @NonNull CrudType crudType, @NonNull String errorMessage,
                                     @NonNull String... verifyColumns) {
        if (ArrayUtil.isEmpty(verifyColumns)) {
            return;
        }
        // 新增和删除分开处理，更新时需要判断是否修改
        if (CrudType.INSERT.equals(crudType)) {
            uniqueVerifyOfInsert(verifyObject, service, errorMessage, verifyColumns);
        } else if (CrudType.UPDATE.equals(crudType)) {
            uniqueVerifyOfUpdate(verifyObject, service, errorMessage, verifyColumns);
        }
    }

    private static void uniqueVerifyOfInsert(@NonNull Object verifyObject, @NonNull IService service,
                                             @NonNull String errorMessage, @NonNull String... verifyColumns) {
        executeUniqueVerify(verifyObject, service, errorMessage, verifyColumns);
    }

    private static void executeUniqueVerify(@NonNull Object verifyObject, @NonNull IService service,
                                            @NonNull String errorMessage, @NonNull String... verifyColumns) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        List<Object> valueList = CollectionUtil.newArrayList();
        for (String column : verifyColumns) {
            Object value = ReflectUtil.getFieldValue(verifyObject, TextUtil.toCamel(column));
            valueList.add(value);
            queryWrapper.eq(CrudUtil.formatField(column), value);
        }

        int count = service.count(queryWrapper);
        if (count >= 1) {
            if (StrUtil.isBlank(errorMessage)) {
                errorMessage = "唯一验证不通过，【" + ArrayUtil.join(verifyColumns, ",") + ":" + CollectionUtil.join(valueList, ",") + "】已存在！";
            }
            throw new VerifyException(errorMessage);
        }
    }

    private static void uniqueVerifyOfUpdate(@NonNull Object verifyObject, @NonNull IService service,
                                             @NonNull String errorMessage, @NonNull String... verifyColumns) {
        // 获得对象的主键
        String idValue = CrudUtil.getStrPrimaryValue(verifyObject);
        if (idValue == null) {
            // 主键没有值，无法进行验证。但我也没办法强迫你更新时必须传递主键值
            return;
        }
        Object originalEntity = service.getById(idValue);
        if (originalEntity == null) {
            throw new VerifyException("唯一验证不通过，数据已不存在！");
        }
        // 如果发生了修改，则去验证唯一性
        boolean isChanged = isChanged(verifyObject, originalEntity, verifyColumns);
        if (isChanged) {
            executeUniqueVerify(verifyObject, service, errorMessage, verifyColumns);
        }
    }

    private static boolean isChanged(Object verifyObject, Object originalEntity, String... verifyColumns) {
        for (String column : verifyColumns) {
            Object originalValue = ReflectUtil.getFieldValue(originalEntity, TextUtil.toCamel(column));
            Object verifyValue = ReflectUtil.getFieldValue(verifyObject, TextUtil.toCamel(column));
            if (!originalValue.equals(verifyValue)) {
                return true;
            }
        }
        return false;
    }


}
