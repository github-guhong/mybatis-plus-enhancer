package guhong.play.mybatisplusenhancer.annotation.core;

import guhong.play.mybatisplusenhancer.base.service.SelfServiceImpl;
import guhong.play.mybatisplusenhancer.constants.enums.ExecutionTime;
import guhong.play.mybatisplusenhancer.handler.param.UpdateInParamHandler;
import guhong.play.mybatisplusenhancer.handler.param.impl.EntityInParamHandler;
import guhong.play.mybatisplusenhancer.handler.result.UpdateResultHandler;
import guhong.play.mybatisplusenhancer.handler.result.impl.DefaultResultHandler;
import guhong.play.mybatisplusenhancer.handler.verify.UpdateVerifyHandler;
import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动更新操作
 * 如果这个操作需要事务，请自行处理
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AutomationUpdate {

    /**
     * serviceImpl类型
     * @return 返回serviceImpl类型
     */
    Class<? extends IService<?>> service() default SelfServiceImpl.class;

    /**
     * 入参处理器，表示修改的传参方式
     * 这里提供了一个默认的实现{@link EntityInParamHandler}，表示将整个实体类传递的传参方式
     */
    Class<? extends UpdateInParamHandler> inParamHandler() default EntityInParamHandler.class;

    /**
     * 验证处理器
     */
    Class<? extends UpdateVerifyHandler> verifyHandler() default SelfServiceImpl.class;

    /**
     * 结果处理器
     */
    Class<? extends UpdateResultHandler<?>> resultHandler() default DefaultResultHandler.class;

    /**
     * 执行时机，即：增强的代码是执行在你写的代码之前还是在你之后
     * 也就是说你可以控制增强代码是前置增强还是后置增强
     */
    ExecutionTime executionTime() default ExecutionTime.BEFORE;
}

