package guhong.play.mybatisplusenhancer.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 万能的工具箱
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@SuppressWarnings("all")
public class ToolKit {

    /**
     * 去掉毫秒
     * 前端的时间戳转换时间类型后，会有一个毫秒，如果要做一些精细的判断，这些毫秒就会出问题
     * 而json转换后的时间并不会去掉毫秒。
     */
    public static Date removeMillisecond(Date data) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        String format = DateUtil.format(data, dateFormat) + ".000";
        return DateUtil.parse(format);
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from((localDateTime).atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获得当前请求对象
     *
     * @return 返回请求对象
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    /**
     * 获得当前响应对象
     *
     * @return 返回详情对象
     */
    public static HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getResponse();
    }

    public static boolean isOptionsRequest(HttpServletRequest httpServletRequest) {
        return httpServletRequest != null && HttpMethod.OPTIONS.name().equals(httpServletRequest.getMethod());
    }

    public static boolean isOptionsRequest() {
        return ToolKit.isOptionsRequest(ToolKit.getHttpServletRequest());
    }

    /**
     * 获得父类的泛型类型
     *
     * @param classes 根据该类型获得其父类的泛型类型
     * @param index   泛型的位置
     * @return 返回泛型类型
     */
    public static <T> Class<T> geGenericBytSuperClass(Class<?> classes, int index) {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(classes, index);
    }

    /**
     * 获得父接口的泛型类型
     *
     * @param classes    根据该类型获得其父类的泛型类型
     * @param interfaces 指定哪个父接口（接口可以多个）
     * @param index      泛型的位置
     * @return 返回泛型类型
     */
    public static <T> Class<T> getGenericByInterface(Class<?> classes, Class<?> interfaces, int index) {
        Type[] genericInterfaces = classes.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (!(genericInterface instanceof ParameterizedType)) {
                continue;
            }
            Type rawType = ((ParameterizedType) genericInterface).getRawType();
            if (rawType.getTypeName().equals(interfaces.getTypeName())) {
                Class<T> tClass = (Class<T>) ((ParameterizedType) genericInterface).getActualTypeArguments()[index];
                return tClass;
            }
        }
        return null;
    }

    /**
     * 找寻某个类所实现的某个接口
     *
     * @param classes        类型
     * @param interfaceClass 接口类型
     * @return 返回接口类型
     */
    public static <T> Class<T> findInterfaceClassInImpl(Class classes, Class<T> interfaceClass) {
        Class[] interfaces = classes.getInterfaces();
        for (Class anInterface : interfaces) {
            if (anInterface.equals(interfaceClass)) {
                return (Class<T>) anInterface;
            }
        }
        return null;
    }

    /**
     * 找寻某个类所实现的某个接口
     *
     * @param classes 类型
     * @param index   接口类型
     * @return 返回接口类型
     */
    public static <T> Class<T> findInterfaceClassInImpl(Class classes, int index) {
        Class[] interfaces = classes.getInterfaces();
        return ArrayUtil.get(interfaces, index);
    }

    /**
     * 判断数组中的元素全部为null
     *
     * @param array 数组
     * @return 如果数组中的值全部是null则返回true。反之false
     */
    public static boolean isNullArray(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }

        for (int i = 0; i < array.length; i++) {
            Object value = array[i];
            if (value != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对象是否为空
     *
     * @param o 对象
     * @return 空返回true
     */
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return StrUtil.isBlank(o.toString());
        } else if (o instanceof Collection) {
            return CollectionUtil.isEmpty((Collection) o);
        } else if (o instanceof Map) {
            return CollectionUtil.isEmpty((Map) o);
        } else if (o instanceof IPage) {
            IPage page = (IPage) o;
            return CollectionUtil.isEmpty(page.getRecords());
        } else if (o instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) o;
            return bigDecimal.equals(BigDecimal.ZERO);
        }
        return false;
    }

    /**
     * 对象是否不为空
     *
     * @param o 对象
     * @return 不为空返回true
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 对象组中是否全是空
     *
     * @param params 数据
     * @return 全部是空返回true
     */
    public static boolean isAllEmpty(Object... params) {
        for (Object o : params) {
            if (isNotEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是“基础”数据类型，即不是自定义的类型。比如：String和Object也当做基础数据类型
     *
     * @param tClass 类型
     * @return 是基础类型返回true
     */
    public static boolean isBaseType(Class<?> tClass) {
        if (tClass.equals(String.class) ||
                tClass.equals(Integer.class) ||
                tClass.equals(Short.class) ||
                tClass.equals(Long.class) ||
                tClass.equals(Character.class) ||
                tClass.equals(Boolean.class) ||
                tClass.equals(Float.class) ||
                tClass.equals(Double.class) ||
                tClass.equals(Byte.class) ||
                tClass.equals(Object.class) ||
                tClass.equals(Date.class) ||
                tClass.equals(LocalDateTime.class)) {
            return true;
        }
        return false;
    }

    public static boolean isCustomtType(Class<?> tClass) {
        return !isBaseType(tClass);
    }

    /**
     * 获得指定Class中拥有指定注解的字段
     *
     * @param tClass 指定的类型
     * @return 返回字段，没有返回null
     */
    public static Field getAnnotationField(Class<?> tClass, Class<? extends Annotation> aClass) {
        for (Field field : ReflectUtil.getFields(tClass)) {
            Annotation fieldAnnotation = field.getAnnotation(aClass);
            if (fieldAnnotation != null) {
                return field;
            }
        }
        return null;
    }

    /**
     * 获得简单的异常信息
     *
     * @param exceptionName 异常名字， 如果为空则使用最顶级的异常类的类名
     * @param throwable     异常对象
     * @return 返回异常信息
     */
    public static String getSimpExcpetionMessage(String exceptionName, Throwable throwable) {
        Throwable rootCause = ExceptionUtil.getRootCause(throwable);
        if (StrUtil.isBlank(exceptionName)) {
            exceptionName = rootCause.getClass().getSimpleName();
        }
        StackTraceElement[] stackTrace = rootCause.getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[0];
        String className = stackTraceElement.getClassName();
        int lineNumber = stackTraceElement.getLineNumber();
        String methodName = stackTraceElement.getMethodName();
        String errorMessage = rootCause.getMessage();
        String exceptionClassName = rootCause.getClass().getSimpleName();
        return exceptionName + ":" + exceptionClassName + ":" + errorMessage + "。" + className + "." + methodName + "." + lineNumber;
    }


    /**
     * 获得简单的异常信息
     *
     * @param throwable 异常对象
     * @return 返回异常信息
     */
    public static String getSimpExcpetionMessage(Throwable throwable) {
        return ToolKit.getSimpExcpetionMessage(null, throwable);
    }


    /**
     * 生成一个临时目录
     */
    public static String generateTemporaryDir() {
        String systemTemporaryPath = System.getProperty("java.io.tmpdir");
        String bailuTemporaryPath = systemTemporaryPath + File.separator + "bailu";
        if (!FileUtil.exist(bailuTemporaryPath)) {
            FileUtil.mkdir(bailuTemporaryPath);
        }
        return bailuTemporaryPath;
    }

    public static File generateNewFile(String fileName, String suffix) {
        if (StrUtil.isBlank(fileName)) {
            fileName = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        }
        if (StrUtil.isBlank(suffix)) {
            throw new RuntimeException("the suffix is null or blank");
        }
        File file = new File(generateTemporaryDir(), fileName + "." + suffix);
        if (file.exists()) {
            file.delete();
        }
        FileUtil.mkParentDirs(file);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File generateNewFile(String suffix) {
        return generateNewFile(null, suffix);
    }


    public static boolean isNotAllEmpty(Object... params) {
        return !isAllEmpty(params);
    }

}
