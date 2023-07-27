package guhong.play.mybatisplusenhancer.exception;

/**
 * @author : 李双凯
 * @date : 2022/4/26 15:31
 **/
public class ComplexQueryException extends BaseException {


    public ComplexQueryException() {
        super("复杂查询出错");
    }

    public ComplexQueryException(String message) {
        super(message);
    }
}
