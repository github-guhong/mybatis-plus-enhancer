package guhong.play.mybatisplusenhancer.handler.result.impl;

import guhong.play.mybatisplusenhancer.handler.result.*;

/**
 * 全部结果处理器
 * 因为不同项目、甚至不同的方法返回类型可能都不一样，所以增强后的返回没办法构建出返回的结果。
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 * @param <R> 返回的结果类型
 **/
public interface WholeResultHandler<R> extends InsertResultHandler<R>, DeleteResultHandler<R>,
        UpdateResultHandler<R>, SelectExistResultHandler<R>, SelectOneResultHandler<R>, SelectListResultHandler<R> {




}
