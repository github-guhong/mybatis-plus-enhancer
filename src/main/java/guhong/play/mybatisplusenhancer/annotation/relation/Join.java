package guhong.play.mybatisplusenhancer.annotation.relation;

import guhong.play.mybatisplusenhancer.constants.enums.JoinType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联表，表示要进行连接操作，通过该注解设置表连接的信息
 * 注意： 不建议使用该注解，因为要做表连接的查询往往会比较复杂。对于过于复杂的SQL还是自己手写比较清晰
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Join {

    /**
     * 连接类型
     */
    JoinType jonType() default JoinType.INNER;


    /**
     * 外键表名
     */
    String foreignTable();

    /**
     * 外键表关联的key
     */
    String foreignKey();

    /**
     * 主表名，默认用IService的泛型作为主表
     */
    String primaryTable() default "";

    /**
     * 主键字段，默认为id
     */
    String primaryKey() default "id";
}
