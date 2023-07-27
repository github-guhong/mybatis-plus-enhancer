package guhong.play.mybatisplusenhancer.exception;


/**
 * 插入业务异常
 * @author 李双凯
 * @date 2019/9/10 10:57
 */
public class InsertionFailureException extends BaseException {

    public InsertionFailureException(String message) {
        super(message);
    }

    public InsertionFailureException() {
        super("插入失败");
    }
}
