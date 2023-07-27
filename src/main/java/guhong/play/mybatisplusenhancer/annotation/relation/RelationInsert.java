package guhong.play.mybatisplusenhancer.annotation.relation;

import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联插入
 *
 * 使用该注解时，最好同步使用
 *     @TableField(exist = false)
 * 注解来忽略关联字段，以防出现问题
 * @author : 李双凯
 * @date : 2023/3/18 17:34
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RelationInsert {

    /**
     * 【关联表】所对应的Service
     */
    Class<? extends IService<?>> relationService();


    /**
     * 比如有a,b两张表
     * a表添加的时候，需要和b表关联
     * 那么a就是发起人，b就是a的链接人
     */

    /**
     * 发起者
     */
    String originator();


    /**
     * 链接人
     */
    String linker();

}
