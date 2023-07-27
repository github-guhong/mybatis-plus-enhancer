package guhong.play.mybatisplusenhancer.util.proxy;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.util.CrudUtil;
import guhong.play.mybatisplusenhancer.util.TextUtil;
import lombok.Data;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 超级实体代理对象，可以直接使用原实体类的get/set方法操作数据
 * 不建议把代理对象到处传递，以防不必要的SQL操作。
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@SuppressWarnings("all")
public class SuperEntityProxy implements MethodInterceptor {

    /**
     * 目标对象的服务类
     */
    private IService service;

    /**
     * 目标对象
     */
    private Class target;

    /**
     * 主键值
     */
    private Object primaryValue;

    /**
     * 主键名
     */
    private String primaryKey = "id";

    public SuperEntityProxy(IService service, Object primaryValue, Class target) {
        this.service = service;
        this.target = target;
        this.primaryValue = primaryValue;
    }

    public SuperEntityProxy(IService service, Object primaryValue, Class target, String primaryKey) {
        this.service = service;
        this.target = target;
        this.primaryValue = primaryValue;
        this.primaryKey = CrudUtil.formatField(primaryKey);
    }

    /**
     * 给目标对象创建一个代理对象
     *
     * @return 返回代理对象
     */
    public Object getProxyInstance() {
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target);
        //3.设置回调函数
        en.setCallback(this);
        //4.创建子类(代理对象)
        return en.create();

    }


    @SuppressWarnings("all")
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        // 只处理get/set方法
        if (!this.isExecute(methodName, o)) {
            return null;
        }
        // 获得字段名：驼峰格式
        Field field = getFieldByMethodName(methodName, o);
        if (field == null) {
            return null;
        }
        // 获得数据库列名： 下划线格式
        String column = CrudUtil.formatField(field.getName());
        if (methodName.contains("get")) {
            // 查询
            return executeGet(column, field);
        } else if (methodName.contains("set")) {
            // 更新
            executeSet(column, objects);
        }
        return null;
    }

    private boolean isExecute(String methodName, Object o) {

        // 只执行get/set方法
        if (methodName.contains("get") || methodName.contains("set")) {
            // 判断get/set方法是否有对应字段的
            Field field = getFieldByMethodName(methodName, o);
            if (field == null) {
                return true;
            }
        }
        return false;

    }

    private Object executeGet(String column, Field field) {
        Object result = service.getOne(new QueryWrapper().select(column).eq(true, primaryKey, primaryValue));
        if (result == null) {
            return null;
        }
        return ReflectUtil.getFieldValue(result, field);
    }

    private void executeSet(String column, Object[] values) {
        Object value = values[0];
        if (value != null) {
            service.update(new UpdateWrapper().set(true, column, value).eq(true, primaryKey, primaryValue));
        }
    }

    private Field getFieldByMethodName(String methodName, Object o) {
        String fieldName = TextUtil.toCamel(methodName.replaceAll("get|set", "").trim());
        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        return ReflectUtil.getField(o.getClass(), fieldName);
    }
}
