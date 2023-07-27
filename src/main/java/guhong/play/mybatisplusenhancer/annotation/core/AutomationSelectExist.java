package guhong.play.mybatisplusenhancer.annotation.core;

import guhong.play.mybatisplusenhancer.base.service.SelfServiceImpl;
import guhong.play.mybatisplusenhancer.constants.enums.ExecutionTime;
import guhong.play.mybatisplusenhancer.handler.result.SelectExistResultHandler;
import guhong.play.mybatisplusenhancer.handler.result.impl.DefaultResultHandler;
import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动查询是否存在操作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AutomationSelectExist {


    /**
     * serviceImpl类型
     *
     * @return 返回serviceImpl类型
     */
    Class<? extends IService<?>> service() default SelfServiceImpl.class;

    /**
     * 结果处理器
     */
    Class<? extends SelectExistResultHandler<?>> resultHandler() default DefaultResultHandler.class;

    /**
     * 执行时机，即：增强的代码是执行在你写的代码之前还是在你之后
     * 也就是说你可以控制增强代码是前置增强还是后置增强
     */
    ExecutionTime executionTime() default ExecutionTime.BEFORE;
}
