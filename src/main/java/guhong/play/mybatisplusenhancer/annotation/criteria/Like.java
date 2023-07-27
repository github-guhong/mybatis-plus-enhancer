package guhong.play.mybatisplusenhancer.annotation.criteria;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用模糊查询匹配
 * %?%
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface Like {


    /**
     * 指定的条件列，默认使用参数名字
     */
    String[] columnNames() default "";

    /**
     * 条件字段所属的表，默认使用当前IService所指定的实体类所对应的表名
     */
    String tableName() default "";


    /**
     * 是否使用or条件，默认and
     */
    boolean or() default false;
}
