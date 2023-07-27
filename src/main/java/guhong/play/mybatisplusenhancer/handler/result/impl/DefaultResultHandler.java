package guhong.play.mybatisplusenhancer.handler.result.impl;

import guhong.play.mybatisplusenhancer.exception.InsertionFailureException;
import lombok.Data;

/**
 * 默认的结果处理器
 * 返回Object类型
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class DefaultResultHandler implements WholeResultHandler<Object> {

    /**
     * 构建添加结果
     *
     * @param isSuccess 是否成功
     * @param entity    添加操作后的对象,可能是null
     * @return 返回操作结果
     */
    @Override
    public Object buildInsertResult(boolean isSuccess, Object entity) {
        if (entity == null) {
            throw new InsertionFailureException();
        }
        return entity;
    }

    /**
     * 构建删除结果
     *
     * @param isSuccess 本次操作是否成功，成功返回true，反之返回false
     * @return 返回操作结果
     */
    @Override
    public Object buildDeleteResult(boolean isSuccess) {
        return isSuccess;
    }

    /**
     * 构建更新结果
     *
     * @param isSuccess 本次操作是否成功，成功返回true，反之返回false
     * @return 返回操作结果
     */
    @Override
    public Object buildUpdateResult(boolean isSuccess, Object entity) {
        return isSuccess;
    }

    /**
     * 构建查询数据存在结果
     *
     * @param isExist 数据是否存在，成功返回true，反之返回false
     * @return 返回查询结果
     */
    @Override
    public Object buildSelectExistResult(boolean isExist) {
        return isExist;
    }

    /**
     * 构建查询列表数据结果
     *
     * @param value 查询出来的值
     * @return 返回查询数据
     */
    @Override
    public Object buildSelectListResult(Object value) {
        return value;
    }

    /**
     * 构建查询单个数据结果
     *
     * @param value 查询出来的值
     * @return 返回查询数据
     */
    @Override
    public Object buildSelectOneResult(Object value) {
        return value;
    }
}
