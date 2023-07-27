package guhong.play.mybatisplusenhancer.annotation.relation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义子查询sql
 * 注意：
 *  1、字段尽量使用绝对指定。即使用完整的表明指定字段。比如：t_user.user_name。这样会减少不必要的错误
 *      本项目中的复杂查询也会有这种规范。
 *  2、请配合{@link As} 使用，不然无法使用
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SubQuery {

    /**
     * 子查询sql
     */
    String sql();
}
