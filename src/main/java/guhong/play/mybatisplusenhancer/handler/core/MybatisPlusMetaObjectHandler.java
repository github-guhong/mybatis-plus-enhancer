package guhong.play.mybatisplusenhancer.handler.core;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import guhong.play.mybatisplusenhancer.constants.FieldConstant;
import guhong.play.mybatisplusenhancer.constants.ValueConstant;
import guhong.play.mybatisplusenhancer.interfaces.UserSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * mybatis plus 属性填充处理器
 *
 * @author 李双凯
 * 注意事项：
 * 字段必须声明TableField注解，属性fill选择对应策略，该申明告知 Mybatis-Plus 需要预留注入 SQL 字段
 * @date 2021/2/10 15:59
 **/
@Component
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Autowired(required = false)
    private UserSupport userSupport;

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setValueIfNull(FieldConstant.CREATE_TIME, LocalDateTime.now(), metaObject);
        this.setValueIfNull(FieldConstant.UPDATE_TIME, LocalDateTime.now(), metaObject);
        this.setValueIfNull(FieldConstant.IS_DELETE, ValueConstant.LOGIC_NOT_DELETE, metaObject);
        this.setValueIfNull(FieldConstant.SORT, ValueConstant.DEFAULT_SORT, metaObject);
        this.setUserId(FieldConstant.CREATE_USER, metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {

        // 设置更新时间和更新人
        this.setValueIfNull(FieldConstant.UPDATE_TIME, LocalDateTime.now(), metaObject);
        this.setUserId(FieldConstant.UPDATE_USER, metaObject);
    }

    /**
     * 为指定字段设置当前用户id
     *
     * @param fieldName  字段名
     * @param metaObject 对象
     */
    private void setUserId(String fieldName, MetaObject metaObject) {
        Object originalObject = metaObject.getOriginalObject();
        Field field = ReflectUtil.getField(originalObject.getClass(), fieldName);
        Object fieldValue = ReflectUtil.getFieldValue(originalObject, field);
        if (field != null && fieldValue == null && userSupport != null) {
            Serializable currentUserId = userSupport.getCurrentUserId();
            if (currentUserId != null) {
                this.setFieldValByName(fieldName, currentUserId, metaObject);
            } else {
                throw new RuntimeException("请登录！");
            }
        }
    }

    private void setValueIfNull(String fieldName, Object fieldVal, MetaObject metaObject) {
        Object value = this.getFieldValByName(fieldName, metaObject);
        if (value == null) {
            this.setFieldValByName(fieldName, fieldVal, metaObject);
        }
    }


}
