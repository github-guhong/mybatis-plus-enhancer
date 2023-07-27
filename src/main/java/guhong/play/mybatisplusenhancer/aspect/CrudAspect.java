package guhong.play.mybatisplusenhancer.aspect;

import guhong.play.mybatisplusenhancer.annotation.core.*;
import guhong.play.mybatisplusenhancer.constants.enums.CrudType;
import guhong.play.mybatisplusenhancer.constants.enums.ExecutionTime;
import guhong.play.mybatisplusenhancer.factory.CrudHandlerFactory;
import guhong.play.mybatisplusenhancer.handler.core.CrudHandler;
import com.alibaba.fastjson.JSONObject;
import guhong.play.mybatisplusenhancer.base.JoinPointWrapper;
import guhong.play.mybatisplusenhancer.util.ToolKit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 自动完成增删改查
 * @author 李双凯
 * @date 2019/9/18 21:23
 */
@Slf4j
@Component
@Aspect
@Order(1001)
public class CrudAspect {

    /**
     * 自动新增操作
     */
    @Around("@annotation(guhong.play.mybatisplusenhancer.annotation.core.AutomationInsert)")
    public Object insertEnhance(ProceedingJoinPoint joinPoint) throws Throwable {
        JoinPointWrapper joinPointWrapper = new JoinPointWrapper(joinPoint);
        AutomationInsert annotation = joinPointWrapper.getMethodAnnotation(AutomationInsert.class);
        ExecutionTime executionTime = annotation.executionTime();
        return doEnhance(joinPoint, CrudType.INSERT, executionTime);
    }

    /**
     * 自动删除操作
     */
    @Around("@annotation(guhong.play.mybatisplusenhancer.annotation.core.AutomationDelete)")
    public Object deleteEnhance(ProceedingJoinPoint joinPoint) throws Throwable {
        JoinPointWrapper joinPointWrapper = new JoinPointWrapper(joinPoint);
        AutomationDelete annotation = joinPointWrapper.getMethodAnnotation(AutomationDelete.class);
        ExecutionTime executionTime = annotation.executionTime();
        return doEnhance(joinPoint, CrudType.DELETE, executionTime);
    }

    /**
     * 自动修改操作
     */
    @Around("@annotation(guhong.play.mybatisplusenhancer.annotation.core.AutomationUpdate)")
    public Object updateEnhance(ProceedingJoinPoint joinPoint) throws Throwable {
        JoinPointWrapper joinPointWrapper = new JoinPointWrapper(joinPoint);
        AutomationUpdate annotation = joinPointWrapper.getMethodAnnotation(AutomationUpdate.class);
        ExecutionTime executionTime = annotation.executionTime();
        return doEnhance(joinPoint, CrudType.UPDATE, executionTime);
    }

    /**
     * 自动查询-列表操作
     */
    @Around("@annotation(guhong.play.mybatisplusenhancer.annotation.core.AutomationSelectList)")
    public Object selectListEnhance(ProceedingJoinPoint joinPoint) throws Throwable {
        JoinPointWrapper joinPointWrapper = new JoinPointWrapper(joinPoint);
        AutomationSelectList annotation = joinPointWrapper.getMethodAnnotation(AutomationSelectList.class);
        ExecutionTime executionTime = annotation.executionTime();
        return doEnhance(joinPoint, CrudType.SELECT, executionTime);
    }

    /**
     * 自动查询-是否存在操作
     */
    @Around("@annotation(guhong.play.mybatisplusenhancer.annotation.core.AutomationSelectExist)")
    public Object selectExistEnhance(ProceedingJoinPoint joinPoint) throws Throwable {
        JoinPointWrapper joinPointWrapper = new JoinPointWrapper(joinPoint);
        AutomationSelectExist annotation = joinPointWrapper.getMethodAnnotation(AutomationSelectExist.class);
        ExecutionTime executionTime = annotation.executionTime();
        return doEnhance(joinPoint, CrudType.SELECT_EXIST, executionTime);
    }

    /**
     * 自动查询-详情操作
     */
    @Around("@annotation(guhong.play.mybatisplusenhancer.annotation.core.AutomationSelectSingle)")
    public Object selectGetEnhance(ProceedingJoinPoint joinPoint) throws Throwable {
        JoinPointWrapper joinPointWrapper = new JoinPointWrapper(joinPoint);
        AutomationSelectSingle annotation = joinPointWrapper.getMethodAnnotation(AutomationSelectSingle.class);
        ExecutionTime executionTime = annotation.executionTime();
        return doEnhance(joinPoint, CrudType.SELECT_ONE, executionTime);
    }


    public Object doEnhance(ProceedingJoinPoint joinPoint, CrudType crudType, ExecutionTime executionTime) throws Throwable{
        // 不处理OptionsRequest，防止在Controller层使用时出现两次增强的情况。
        if (ToolKit.isOptionsRequest()) {
            return null;
        }
        CrudHandler crudHandler = CrudHandlerFactory.create(crudType, joinPoint);
        String methodName = crudHandler.getCrudWorkBox().getJoinPointWrapper().getMethodName();

        String executeName = "【自动" + crudType + "】：" + methodName;
        // 如果是后置，就先执行原方法
        if (ExecutionTime.AFTER.equals(executionTime)) {
            log.debug(executeName + "后置执行，先执行原方法");
            joinPoint.proceed();
        }

        log.debug(executeName + "开始执行增强");

        Object result = crudHandler.execute();


        // 如果是前置，就先执行自动化CURD
        if (ExecutionTime.BEFORE.equals(executionTime)) {
            log.debug(executeName + "增强执行完毕，执行原方法");
            joinPoint.proceed();
        }

        // 主动释放掉对象，方便垃圾回收，不过好像作用不大
        crudHandler.release();
        log.debug(executeName + "增强全部执行完毕，操作结果：" + JSONObject.toJSONString(result));

        return result;
    }


}
