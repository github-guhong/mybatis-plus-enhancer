package guhong.play.mybatisplusenhancer.handler.result;

/**
 * 删除结果处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 * @param <R> 返回的结果类型
 **/
public interface DeleteResultHandler<R> {

    /**
     * 构建删除结果
     * @param isSuccess 本次操作是否成功，成功返回true，反之返回false
     * @return 返回结果
     */
    public R buildDeleteResult(boolean isSuccess);

}
