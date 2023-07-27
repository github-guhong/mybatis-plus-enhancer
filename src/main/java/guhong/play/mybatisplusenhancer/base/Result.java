package guhong.play.mybatisplusenhancer.base;

import cn.hutool.core.util.StrUtil;
import guhong.play.mybatisplusenhancer.constants.ResultCode;
import guhong.play.mybatisplusenhancer.util.ToolKit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * web 响应结果对象
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@NoArgsConstructor
@Slf4j
public class Result {

    private int code;

    private String errorMessage;

    private Object data;

    public Result(int code, String errorMessage, Object data) {
        this.code = code;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public Result(int code, String errorMessage) {
        this(code, errorMessage, null);
    }

    /**
     * 构建成功的结果
     * @param data 成功数据
     * @return 返回成功的结果
     */
    public static Result buildSuccess(Object data) {
        return new Result(ResultCode.SUCCESS, "", data);
    }


    /**
     * 构建失败的结果
     * @param errorMessage 失败的信息
     * @return 返回失败的结果
     */
    public static Result buildError(String errorMessage) {
        log.error(errorMessage);
        return new Result(ResultCode.ERROR, errorMessage, new Object());
    }

    /**
     * 构建业务失败的结果
     * @param errorMessage 失败的信息
     * @return 返回失败的结果
     */
    public static Result buildBizError(String errorMessage) {
        log.error(errorMessage);
        return new Result(ResultCode.BIZ, errorMessage, new Object());
    }

    /**
     * 构建未登录的结果
     * @return 返回失败的结果
     */
    public static Result buildNotLogin(String errorMessage) {
        return new Result(ResultCode.NOT_LOGIN, errorMessage , new Object());
    }

    /**
     * 构建空的结果
     * @return 返回失败的结果
     */
    public static Result buildEmpty(Object emptyObject) {
        return new Result(ResultCode.EMPTY, "没有任何信息", emptyObject);
    }


    /**
     * 构建成功或空结果
     * @param data 数据，如果数据为空则返回空结果，反之返回成功结果
     * @return 返回成功或空结果
     */
    public static Result buildSuccessOrEmpty(Object data) {
        if (ToolKit.isEmpty(data)) {
            return Result.buildEmpty(data);
        }
        return Result.buildSuccess(data);
    }

    /**
     * 构建成功或失败结果
     * @param isSuccess 如果是true表示成功，反之表示失败
     * @return 返回成功或空结果
     */
    public static Result buildSuccessOrError(boolean isSuccess, Object successData, String errorMessage) {
        if (successData == null) {
            successData = "操作成功！";
        }
        if (StrUtil.isBlank(errorMessage)) {
            errorMessage = "操作失败！";
        }

        if (isSuccess) {
            return Result.buildSuccess(successData);
        }
        return Result.buildError(errorMessage);
    }

    public int toHttpCode() {
        switch (code) {
            case 50000:
                return HttpStatus.INTERNAL_SERVER_ERROR.value();
            case 40100:
                return HttpStatus.UNAUTHORIZED.value();
            case 40300:
                return HttpStatus.FORBIDDEN.value();
            default:
                return HttpStatus.OK.value();
        }
    }
}
