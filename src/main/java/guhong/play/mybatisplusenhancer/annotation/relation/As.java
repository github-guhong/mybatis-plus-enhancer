package guhong.play.mybatisplusenhancer.annotation.relation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 别名注解
 * 和{@link Column}注解互斥，当使用@Column时@As不生效
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface As {

    /**
     * 别名值。如果不指定则用字段名作为别名
     */
    String name() default "";
}
