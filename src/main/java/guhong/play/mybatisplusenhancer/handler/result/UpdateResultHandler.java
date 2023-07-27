package guhong.play.mybatisplusenhancer.handler.result;

/**
 * 更新结果处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 * @param <R> 返回的结果类型
 **/
public interface UpdateResultHandler<R> {


    /**
     * 构建更新结果
     * @param isSuccess 本次操作是否成功，成功返回true，反之返回false
     * @param entity 更新后的对象，可能是null
     * @return 返回结果
     */
    public R buildUpdateResult(boolean isSuccess, Object entity);

}
