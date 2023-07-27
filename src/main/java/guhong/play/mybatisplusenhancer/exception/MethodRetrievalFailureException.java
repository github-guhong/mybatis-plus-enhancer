package guhong.play.mybatisplusenhancer.exception;

/**
 * @author : 李双凯
 * @date : 2023/6/10 16:36
 **/
public class MethodRetrievalFailureException extends BaseException {

    public MethodRetrievalFailureException(String message) {
        super(message);
    }

    public MethodRetrievalFailureException() {
        super("AOP无法获得目标方法");
    }
}
