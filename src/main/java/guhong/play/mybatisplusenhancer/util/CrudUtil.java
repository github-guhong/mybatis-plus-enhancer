package guhong.play.mybatisplusenhancer.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import guhong.play.mybatisplusenhancer.constants.FieldConstant;
import guhong.play.mybatisplusenhancer.exception.MisuseException;
import guhong.play.mybatisplusenhancer.exception.WrongServiceException;
import guhong.play.mybatisplusenhancer.util.proxy.SuperEntityProxy;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * crud工具类，一个丰富的工具类
 *
 * @author 李双凯
 * @date 2019/10/29 22:14
 */
@SuppressWarnings("all")
public class CrudUtil {


    private static final String STRING = "`";

    public static <T> IPage<T> newMaxPage() {
        return new Page<T>(1, Integer.MAX_VALUE);
    }

    /**
     * 查询是否存在
     *
     * @param paramNames  参数名
     * @param paramValues 参数值
     * @param service     服务类
     * @return 返回true表示存在，反之不存在
     */
    public static boolean isExist(@NonNull QueryWrapper queryWrapper, @NonNull IService service) {
        int count = service.count(queryWrapper);
        if (count >= 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询是否存在
     *
     * @param paramNames  参数名
     * @param paramValues 参数值
     * @param service     服务类
     * @return 返回true表示存在，反之不存在
     */
    @SuppressWarnings("all")
    public static Boolean isExist(@NonNull String[] paramNames, @NonNull Object[] paramValues, @NonNull IService service) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ToolKit.isNullArray(paramValues)) {
            return false;
        }

        for (int i = 0; i < paramNames.length; i++) {
            String paramName = CrudUtil.formatField(paramNames[i]);
            Object paramValue = paramValues[i];
            if (paramValue == null || (StringUtils.isBlank(paramValue.toString()))) {
                continue;
            }
            queryWrapper.eq(paramName, paramValue);
        }

        return CrudUtil.isExist(queryWrapper, service);

    }

    /**
     * 根据id查询数据是否存在
     *
     * @param id      id值
     * @param service 服务类
     * @return 返回true表示存在，反之不存在
     */
    public static Boolean isExistById(@NonNull Object id, @NonNull IService service) {
        Class<?> entityClass = CrudUtil.getEntityClassByService(service);
        Field primaryField = CrudUtil.getPrimaryField(entityClass);
        if (primaryField == null) {
            throw new MisuseException(entityClass.getSimpleName() + "没有使用@TableId标识主键字段，无法使用CrudUtil.isExistById方法！");
        }
        String primaryName = CrudUtil.formatField(primaryField.getName());
        return CrudUtil.isExist(new String[]{primaryName}, new Object[]{id}, service);
    }

    /**
     * 根据一个参数查询数据是否存在
     *
     * @param param   参数名
     * @param value   参数值
     * @param service 服务类
     * @return 返回true表示存在，反之不存在
     */
    public static Boolean isExistByOne(@NonNull String param, @NonNull Object value, @NonNull IService service) {
        return CrudUtil.isExist(new String[]{param}, new Object[]{value}, service);
    }

    /**
     * 根据IService获得实体类类型
     *
     * @param serviceClass IService类
     * @return 返回实体类类型
     */
    public static Class<?> getEntityClassByService(@NonNull IService service) {

        Class<?> serviceClass = SpringUtil.getTarget(service).getClass();
        if (ServiceImpl.class.isAssignableFrom(serviceClass)) {
            Type genericClass = serviceClass.getSuperclass().getGenericSuperclass();
            if (!(genericClass instanceof ParameterizedType)) {
                genericClass = serviceClass.getGenericSuperclass();
            }
            ParameterizedType parameterizedType = (ParameterizedType) genericClass;
            return (Class<?>) parameterizedType.getActualTypeArguments()[1];
        } else if (IService.class.isAssignableFrom(serviceClass)) {
            Type genericClass = serviceClass.getGenericInterfaces()[0];
            if (!(genericClass instanceof ParameterizedType)) {
                genericClass = serviceClass.getSuperclass().getGenericSuperclass();
            }
            ParameterizedType parameterizedType = (ParameterizedType) genericClass;
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new WrongServiceException(service + "没有实现IService接口！");
    }

    /**
     * 通过service获得关联的表名
     *
     * @param service service
     * @return 返回表名
     */
    public static String getTableNameByService(@NonNull IService service) {
        Class<?> entityClass = getEntityClassByService(service);
        return getTableNameByEntityClass(entityClass);
    }

    /**
     * 通过实体类获得对应的表名
     *
     * @param service service
     * @return 返回表名
     */
    public static String getTableNameByEntityClass(@NonNull Class<?> entityClass) {
        TableName tableName = entityClass.getAnnotation(TableName.class);
        if (tableName != null) {
            return tableName.value();
        } else {
            String simpleName = entityClass.getSimpleName();
            return TextUtil.toUnderline(simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1));
        }
    }

    /**
     * 通过类型获得该类型的代理对象，通过该代理对象可以直接操作数据，获得数据库中的值
     *
     * @param service      实体类的服务类
     * @param primaryValue 主键值
     * @return 返回指定类型的代理对象
     */
    @SuppressWarnings("all")
    public static <T> T getSuperEntity(IService service, Object primaryValue) {
        Class<?> targetClass = CrudUtil.getEntityClassByService(service);
        return (T) new SuperEntityProxy(service, primaryValue, targetClass).getProxyInstance();
    }

    /**
     * 通过类型获得该类型的代理对象，通过该代理对象可以直接操作数据，获得数据库中的值
     *
     * @param service      实体类的服务类
     * @param targetClass  目标类型
     * @param primaryValue 主键值
     * @return 返回指定类型的代理对象
     */
    @SuppressWarnings("all")
    public static <T> T getSuperEntity(IService service, Object primaryValue, Class<T> targetClass) {
        return (T) new SuperEntityProxy(service, primaryValue, targetClass).getProxyInstance();
    }

    /**
     * 通过类型获得该类型的代理对象，通过该代理对象可以直接操作数据，获得数据库中的值
     *
     * @param service      实体类的服务类
     * @param primaryValue 主键值
     * @param targetClass  目标类型
     * @param primaryKey   主键名
     * @return 返回指定类型的代理对象
     */
    @SuppressWarnings("all")
    public static <T> T getSuperEntity(IService service, Object primaryValue, String primaryKey) {
        Class<?> targetClass = CrudUtil.getEntityClassByService(service);
        return (T) new SuperEntityProxy(service, primaryValue, targetClass, primaryKey).getProxyInstance();
    }

    /**
     * 通过类型获得该类型的代理对象，通过该代理对象可以直接操作数据，获得数据库中的值
     *
     * @param service      实体类的服务类
     * @param primaryValue 主键值
     * @param targetClass  目标类型
     * @param primaryKey   主键名
     * @return 返回指定类型的代理对象
     */
    @SuppressWarnings("all")
    public static <T> T getSuperEntity(IService service, Object primaryValue, String primaryKey, Class<T> targetClass) {
        return (T) new SuperEntityProxy(service, primaryValue, targetClass, primaryKey).getProxyInstance();
    }

    /**
     * 格式化字段
     *
     * @param field 原字段
     * @return 返回格式化后的字段
     */
    public static String formatField(String field) {
        if (StrUtil.isBlank(field)) {
            return field;
        }
        if (field.contains(STRING)) {
            return TextUtil.toUnderline(field);
        }
        return STRING + TextUtil.toUnderline(field) + STRING;
    }

    /**
     * 获得tClass中使用@TableId注解来标识的字段
     *
     * @return 返回主键字段，没有返回null
     */
    public static Field getPrimaryField(Class<?> tClass) {
        for (Field field : ReflectUtil.getFields(tClass)) {
            TableId annotation = field.getAnnotation(TableId.class);
            if (annotation != null) {
                return field;
            }
        }
        Field id = ReflectUtil.getField(tClass, FieldConstant.ID);
        if (id != null) {
            return id;
        }
        return null;
    }

    /**
     * 获得指定对象中主键字段的值
     *
     * @param object 指定的对象
     * @return 返回主键值，没有返回null
     */
    public static Object getPrimaryValue(Object object) {
        Field primaryField = CrudUtil.getPrimaryField(object.getClass());
        if (primaryField == null) {
            return null;
        }
        return ReflectUtil.getFieldValue(object, primaryField);
    }

    public static Integer getIntPrimaryValue(Object object) {
        Object primaryValue = getPrimaryValue(object);
        if (primaryValue == null) {
            return null;
        }
        return Integer.parseInt(primaryValue.toString());
    }

    public static String getStrPrimaryValue(Object object) {
        Object primaryValue = getPrimaryValue(object);
        if (primaryValue == null) {
            return null;
        }
        return primaryValue.toString();
    }

    /**
     * 获得指定对象中主键字段的名字
     *
     * @param tClass 指定的对象的类型
     * @return 返回主键字段名，没有返回null
     */
    public static String getPrimaryName(Class<?> tClass) {
        Field primaryField = CrudUtil.getPrimaryField(tClass);
        if (primaryField == null) {
            return null;
        }
        return primaryField.getName();
    }

    public static boolean isLogicDelete(@NonNull Class<?> entityClass) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        if (tableInfo == null) {
            return false;
        }
        return tableInfo.isWithLogicDelete();
    }


}
