package guhong.play.mybatisplusenhancer.handler.result;

/**
 *  构建查询列表数据结果处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 * @param <R> 返回的结果类型
 **/
public interface SelectListResultHandler<R> {


    /**
     * 构建查询列表数据结果
     * @param value 查询出来的值
     * @return 返回结果
     */
    public R buildSelectListResult(Object value);


}
