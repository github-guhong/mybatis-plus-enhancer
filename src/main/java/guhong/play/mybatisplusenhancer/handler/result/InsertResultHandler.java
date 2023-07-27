package guhong.play.mybatisplusenhancer.handler.result;

/**
 * 插入结果处理器
 *
 * @param <R> 返回的结果类型
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface InsertResultHandler<R> {

    /**
     * 构建添加结果
     * @param isSuccess 是否成功
     * @param entity 添加操作后的对象,可能是null
     * @return 返回结果
     */
    public R buildInsertResult(boolean isSuccess, Object entity);

}
