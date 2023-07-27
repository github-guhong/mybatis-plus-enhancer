package guhong.play.mybatisplusenhancer.constants;

/**
 * 响应code
 * 20000 ： 正常
 * 50000 ： 服务端错误，不宜展示给用户看错误信息
 * 12345 ： 业务错误，可以展示给用户看错误信息
 * 0 ： 正常，但是没有数据返回
 * 40100 ： 未授权，需要登录重新进行授权。
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface ResultCode {

    /**
     * 正常结果
     */
    int SUCCESS = 20000;


    /**
     * 失败结果。服务端内部错误，既服务端报错。
     */
    int ERROR = 50000;

    /**
     * 失败结果。指可以提示给用户看的一些信息。
     */
    int BIZ = 12345;

    /**
     * 正常结果，但是没有数据
     */
    int EMPTY = 0;

    /**
     * 未登录状态，表示用户需要登录认证
     */
    int NOT_LOGIN = 40100;

    /**
     * 没有权限访问
     */
    int UNAUTHORIZED = 40300;


}
