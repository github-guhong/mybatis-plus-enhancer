package guhong.play.mybatisplusenhancer.annotation.relation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义列名
 * 这样可以不必依赖bean和数据库的字段名一样。但还是能一样就一样。
 * 使用该注解后会自动以字段名作为这个列的别名，此时使用{@link As}无效
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {

    /**
     * 指定列名
     */
    String name();
}
