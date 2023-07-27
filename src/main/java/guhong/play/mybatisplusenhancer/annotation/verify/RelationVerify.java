package guhong.play.mybatisplusenhancer.annotation.verify;

import guhong.play.mybatisplusenhancer.handler.verify.InsertVerifyHandler;
import guhong.play.mybatisplusenhancer.handler.verify.UpdateVerifyHandler;
import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联验证注解，表示与其他实体（表）数据的有关联关系
 * 用于实体类的字段上，添加/修改时会根据该注解去验证数据是否存在
 * 如果关联的数据如果不存在则会抛出异常。
 *
 * 在自动CRUD处理中，使用的位置取决于{@link InsertVerifyHandler} 或{@link UpdateVerifyHandler}接口中的<T>泛型类型。
 * @author 李双凯
 * @date 2019/10/13 18:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RelationVerify {

    /**
     * 关联表的主键名
     * @return 默认以指定关联表的id作为验证字段。
     */
    String relationPrimaryName() default "id";

    /**
     * 与当前操作的实体类相关联的IService类型
     *
     * 用于添加/修改时存在主外键关联的对象，通过该属性可以自动完成与其关联的数据进行验证
     */
    Class<? extends IService<?>> relationService();


    /**
     * 验证不通过时的错误信息
     */
    String errorMessage() default "";

}
