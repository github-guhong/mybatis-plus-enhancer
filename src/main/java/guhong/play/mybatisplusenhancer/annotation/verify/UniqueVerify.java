package guhong.play.mybatisplusenhancer.annotation.verify;

import guhong.play.mybatisplusenhancer.handler.verify.InsertVerifyHandler;
import guhong.play.mybatisplusenhancer.handler.verify.UpdateVerifyHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 唯一验证注解，表示某一个值是唯一，添加/修改时会根据该注解去验证数据是否唯一
 * 如果不唯一则会抛出异常。
 * 当标识在class上时，可以设置多个验证列，实现多字段的唯一验证
 * 当标识在单个字段上时，只判断该字段值的唯一
 *
 * 在自动CRUD处理中，使用的位置取决于{@link InsertVerifyHandler} 或{@link UpdateVerifyHandler}接口中的<T>泛型类型。
 * @author 李双凯
 * @date 2019/10/13 18:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface UniqueVerify {


    /**
     * 验证的列名，默认使用注解标识的字段名作为验证的列名
     * 标识在类上的时候可以指定多个，但标识在单个字段时，只使用默认值或数组的第一个。
     * 标识在类的时候必填！
     */
    String[] columns() default {};

    /**
     * 验证不通过时的错误信息
     */
    String errorMessage() default "";

}
