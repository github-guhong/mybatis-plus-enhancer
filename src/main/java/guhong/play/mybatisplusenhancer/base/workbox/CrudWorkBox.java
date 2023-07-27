package guhong.play.mybatisplusenhancer.base.workbox;

import cn.hutool.extra.spring.SpringUtil;
import guhong.play.mybatisplusenhancer.base.service.SelfServiceImpl;
import guhong.play.mybatisplusenhancer.exception.ServiceInjectionFailureException;
import guhong.play.mybatisplusenhancer.exception.WrongServiceException;
import guhong.play.mybatisplusenhancer.util.CrudUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import guhong.play.mybatisplusenhancer.util.ToolKit;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;

/**
 * 增删改查的工具箱
 *
 * @author : 李双凯
 * @date : 2022/3/19 20:12
 **/
@Getter
@SuppressWarnings("all")
public class CrudWorkBox extends JoinPointWorkBox {

    protected IService service;

    protected Class<?> entityClass;

    public CrudWorkBox(JoinPoint joinPoint) {
        super(joinPoint);
    }

    protected void setServiceAndEntity(Class<? extends IService<?>> serviceClass) {
        IService<?> service = null;
        if (serviceClass.isAssignableFrom(SelfServiceImpl.class)) {
            service = tryAutomaticAcquisitionService();
        } else {
            service = tryInjectFromSpring(serviceClass);
        }
        this.service = service;
        this.entityClass = CrudUtil.getEntityClassByService(this.service);
    }

    private IService<?> tryAutomaticAcquisitionService() {
        try {
            return automaticAcquisitionService();
        } catch (Exception e) {
            String wholeMethodName = joinPointWrapper.getWholeMethodName();
            String message = "执行" + wholeMethodName + "时" +
                    "无法自动找到对应的IService，请指定具体的IService！";
            throw new ServiceInjectionFailureException(message);
        }
    }

    private IService<?> automaticAcquisitionService() {
        Class<?> declaringClass = joinPointWrapper.getMethod().getDeclaringClass();
        if (ServiceImpl.class.isAssignableFrom(declaringClass)) {
            Class<IService> serviceClass = ToolKit.findInterfaceClassInImpl(declaringClass, 0);
            return SpringUtil.getBean(serviceClass);
        } else if (IService.class.isAssignableFrom(declaringClass)) {
            return (IService) SpringUtil.getBean(declaringClass);
        } else {
            throw new WrongServiceException(declaringClass + "没有实现IService接口！");
        }
    }

    private IService<?> tryInjectFromSpring(Class<? extends IService<?>> serviceClass) {
        try {
            return SpringUtil.getBean(serviceClass);
        } catch (Exception e) {
            String message = "从Spring中注入：" + serviceClass + "失败。"+ ExceptionUtil.getRootCauseMessage(e);
            throw new ServiceInjectionFailureException(message);
        }
    }



    @Override
    public void release() {
        super.release();
        entityClass = null;
        service = null;
    }
}
