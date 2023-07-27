package guhong.play.mybatisplusenhancer.handler.result;

/**
 * 查询数据存在结果处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 * @param <R> 返回的结果类型
 **/
public interface SelectExistResultHandler<R> {


    /**
     * 构建查询数据存在结果
     * @param isExist 数据是否存在，成功返回true，反之返回false
     * @return 返回结果
     */
    public R buildSelectExistResult(boolean isExist);

}
