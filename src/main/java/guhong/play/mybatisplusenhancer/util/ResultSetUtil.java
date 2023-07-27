package guhong.play.mybatisplusenhancer.util;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import guhong.play.mybatisplusenhancer.annotation.relation.Join;
import guhong.play.mybatisplusenhancer.exception.FieldNotFindException;
import guhong.play.mybatisplusenhancer.exception.MisuseException;
import org.omg.CORBA.SystemException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 结果集工具
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@SuppressWarnings("all")
public class ResultSetUtil {

    private final ResultSet resultSet;


    public ResultSetUtil(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    /**
     * 将结果集序列化转化为指定列表
     *
     * @param tClass 指定的类型
     * @param <T>
     * @return 返回列表
     */
    public <T> List<T> toList(Class<T> tClass) {
        List<T> result = CollectionUtil.newArrayList();
        if (resultSet == null) {
            return result;
        }
        if (this.isOneToMany(tClass)) {
            result = parseOneToMany(tClass);
        } else {
            result = toSourceList(tClass);
        }
        return result;
    }

    /**
     * 将结果集转为单个对象
     *
     * @param tClass 指定的类型
     * @param <T>
     * @return 返回单个对象
     */
    public <T> T toSingle(Class<T> tClass) {
        T result = ReflectUtil.newInstance(tClass);
        if (resultSet == null) {
            return result;
        }
        if (this.isOneToMany(tClass)) {
            List<T> resultList = this.parseOneToMany(tClass);
            if (CollectionUtil.isEmpty(resultList)) {
                return result;
            }
            return resultList.get(0);
        } else {
            try {
                resultSet.next();
            } catch (SQLException e) {
                return result;
            }
            fillInstance(result);
        }
        return result;
    }

    /**
     * 将结果集转为分页对象
     *
     * @param tClass 指定类型
     * @param page   分页对象
     * @param <T>
     * @return 返回分页对象
     */
    public <T> IPage<T> toPage(Class<T> tClass, IPage<T> page) {
        if (resultSet == null) {
            return new Page<>();
        }
        page.setRecords(toList(tClass));
        long size = page.getSize();
        long total = page.getTotal();
        long pages = 0;
        if (total % size == 0) {
            pages = total / size;
        } else {
            pages = (total / size) + 1;
        }
        page.setPages(pages);
        return page;
    }

    /**
     * 获得count查询数据条数
     *
     * @return 返回count()的数据
     */
    public Long toCount() {
        try {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            return 0L;
        }
        return 0L;
    }

    /**
     * 从结果集中填充一个对象
     *
     * @param instance 实例
     * @param <T>
     */
    private <T> void fillInstance(T instance) {
        Class<?> tClass = instance.getClass();
        Field[] fields = ReflectUtil.getFields(tClass);
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            String name = field.getName();
            String column = TextUtil.toUnderline(name);

            if (ToolKit.isBaseType(fieldType)) {
                // 基础类型
                Object value = getObject(field);
                ReflectUtil.setFieldValue(instance, field, value);
            } else if (Collection.class.isAssignableFrom(fieldType)) {

                // 列表类型
                Collection collection = CollectionUtil.newArrayList();
                ReflectUtil.setFieldValue(instance, field, collection);
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                Type actualTypeArguments = type.getActualTypeArguments()[0];
                // 拿到集合类型的泛型类型
                Class<?> genericityClass = null;
                try {
                    genericityClass = Class.forName(actualTypeArguments.getTypeName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if (ToolKit.isBaseType(genericityClass)) {
                    Object value = getObject(field);
                    if (ToolKit.isNotEmpty(value)) {
                        collection.add(value);
                    }
                } else {
                    Object genericityInstance = ReflectUtil.newInstance(genericityClass);
                    fillInstance(genericityInstance);
                    if (genericityInstance != null) {
                        collection.add(genericityInstance);
                    }
                }

            } else {
                // 单个对象类型
                Object fieldInstance = ReflectUtil.newInstance(fieldType);
                fillInstance(fieldInstance);
                ReflectUtil.setFieldValue(instance, field, fieldInstance);
            }

        }
    }

    /**
     * 将结果集转为指定类型的数据，不作任何其他的处理。数据库查出来是多少行就是多少行
     *
     * @param tClass 指定的类型
     * @param <T>
     * @return 返回源数据
     */
    private <T> List<T> toSourceList(Class<T> tClass) {
        if (resultSet == null) {
            return null;
        }
        List<T> result = CollectionUtil.newArrayList();
        try {
            while (resultSet.next()) {
                T instance = ReflectUtil.newInstance(tClass);
                fillInstance(instance);
                result.add(instance);
            }
            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * 将数据转为1对多的关系（我忘记自己是怎么写的了）
     *
     * @param tClass 指定的类型
     * @param <T>    泛型
     * @return 返回转化后的数据列表
     */
    private <T> List<T> parseOneToMany(Class<T> tClass) {
        List<T> listResult = CollectionUtil.newArrayList();
        Field idField = this.getIdField(tClass);
        String joinTableFieldName = this.getJoinTableField(tClass).getName();
        List<T> sourceList = this.toSourceList(tClass);

        for (T t1 : sourceList) {
            Object t1Id = ReflectUtil.getFieldValue(t1, idField.getName());
            // 如果已经存在了跳过
            boolean isExist = false;
            if (CollectionUtil.isNotEmpty(listResult)) {
                for (T r : listResult) {
                    Object rId = ReflectUtil.getFieldValue(r, idField.getName());
                    if (rId.equals(t1Id)) {
                        isExist = true;
                        break;
                    }
                }
            }
            if (isExist) {
                continue;
            }

            Collection newColleciton = CollectionUtil.newArrayList();
            // 合并相同项
            for (T t2 : sourceList) {

                Object t2Id = ReflectUtil.getFieldValue(t2, idField.getName());
                if (t1Id.equals(t2Id)) {
                    Collection t2Collection = (Collection) ReflectUtil.getFieldValue(t2, joinTableFieldName);
                    newColleciton.addAll(t2Collection);
                }
            }
            ReflectUtil.setFieldValue(t1, joinTableFieldName, newColleciton);

            // 添加到最终的结果中
            listResult.add(t1);
        }
        return listResult;
    }

    /**
     * 是否一对多操作
     *
     * @param tClass 类型
     * @return 存在返回true
     */
    private boolean isOneToMany(Class tClass) {
        return getJoinTableField(tClass) != null;
    }

    /**
     * 获得@Id字段
     *
     * @param tClass 类型
     * @return 获得@ID标识的字段
     */
    private Field getIdField(Class tClass) {
        for (Field field : ReflectUtil.getFields(tClass)) {
            TableId id = field.getAnnotation(TableId.class);
            if (id != null) {
                return field;
            }
        }
        if (isOneToMany(tClass)) {
            throw new MisuseException("使用@Join的一对多关系必须通过@TableId标识主键！");
        }
        return null;
    }

    /**
     * 获得指定Clas中@JoinTable所在的字段
     *
     * @param tClass 指定的类型
     * @return 返回字段，没有返回null
     */
    private Field getJoinTableField(Class tClass) {
        for (Field field : ReflectUtil.getFields(tClass)) {
            Join join = field.getAnnotation(Join.class);
            if (join != null) {
                Class<?> fieldType = field.getType();
                if (Collection.class.isAssignableFrom(fieldType)) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * 从结果集中获得指定字段的数据
     *
     * @param field 字段
     * @return 返回对应字段的值，没有返回null
     */
    private Object getObject(Field field) {
        String name = field.getName();
        String column = TextUtil.toUnderline(name);
        Object value = null;
        try {
            value = resultSet.getObject(column);
        } catch (SQLException e) {
            // 如果以下划线的方式没拿到值，则重新用帕斯卡命名拿值
            try {
                if (ToolKit.isEmpty(value)) {
                    value = resultSet.getObject(name);
                }
            } catch (SQLException e1) {
                throw new FieldNotFindException(e1);
            }

        }
        return value;
    }

}
