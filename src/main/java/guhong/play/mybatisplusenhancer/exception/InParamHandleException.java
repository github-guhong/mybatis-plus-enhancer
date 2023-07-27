package guhong.play.mybatisplusenhancer.exception;

/**
 * 入参处理器异常
 * @author : 李双凯
 * @date : 2022/4/26 15:29
 **/
public class InParamHandleException extends BaseException {

    public InParamHandleException() {
        super("入参处理器异常");
    }


    public InParamHandleException(String message) {
        super(message);
    }
}
