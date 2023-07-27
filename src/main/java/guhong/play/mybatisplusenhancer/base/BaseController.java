package guhong.play.mybatisplusenhancer.base;

import lombok.Data;

/**
 * 基础控制器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class BaseController {



    /**
     * 构建成功的结果
     * @param data 成功数据
     * @return 返回成功的结果
     */
    protected Result success(Object data) {
        return Result.buildSuccess(data);
    }


    /**
     * 构建严重失败的结果
     * @param errMessage 失败的信息
     * @return 返回失败的结果
     */
    protected Result error(String errMessage) {
        return Result.buildError(errMessage);
    }

    /**
     * 构建业务失败的结果
     * @param errMessage 失败的信息
     * @return 返回失败的结果
     */
    protected Result biz(String errMessage) {
        return Result.buildBizError(errMessage);
    }

    /**
     * 构建空的结果
     * @param emptyObject 空对象，是空对象，不是null!
     * @return 返回失败的结果
     */
    protected Result empty(Object emptyObject) {
        return Result.buildEmpty(emptyObject);
    }

    /**
     * 未授权结果
     * @param errorMessage 错误信息
     * @return 返回未授权结果
     */
    protected Result unauthorized(String errorMessage) {
        return Result.buildNotLogin(errorMessage);
    }


    /**
     * 构建成功的结果
     * @return 返回成功的结果
     */
    protected Result success() {
        return Result.buildSuccess("操作成功！");
    }

    /**
     * 构建失败的结果
     * @return 返回失败的结果
     */
    protected Result error() {
        return Result.buildError("操作失败！");
    }


    /**
     * 构建空的结果
     * @return 返回失败的结果
     */
    protected Result empty() {
        return Result.buildEmpty(new Object());
    }

    /**
     * 构建成功或空结果
     * @param data 数据，如果数据为空则返回空结果，反之返回成功结果
     * @return 返回成功或空结果
     */
    protected Result successOrEmpty(Object data) {
        return Result.buildSuccessOrEmpty(data);
    }

    /**
     * 构建成功或失败的结果
     * @param flag true表示成功，false表示失败
     * @param successData 成功消息
     * @param errorMessage 错误信息
     * @return 返回成功或失败的结果
     */
    protected Result successOrError(boolean flag, Object successData, String errorMessage) {
        return Result.buildSuccessOrError(flag, successData, errorMessage);
    }

    /**
     * 构建成功或失败的结果
     * @param flag true表示成功，false表示失败
     * @return 返回成功或失败的结果
     */
    protected Result successOrError(boolean flag) {
        return Result.buildSuccessOrError(flag, null, null);
    }

    /**
     * 构建成功或失败的结果
     * @param data 如果为空则表示失败，反之成功
     * @return 返回成功或失败的结果
     */
    protected Result successOrError(Object data) {
        return Result.buildSuccessOrError(data != null, data, null);
    }

}
