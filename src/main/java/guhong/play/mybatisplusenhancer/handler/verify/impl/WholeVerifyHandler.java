package guhong.play.mybatisplusenhancer.handler.verify.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.handler.verify.InsertVerifyHandler;
import guhong.play.mybatisplusenhancer.handler.verify.UpdateVerifyHandler;

/**
 * 添加/修改验证处理器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 * @param <T> 要验证的实体类型
 * @param <S> IService类型
 **/
public interface WholeVerifyHandler<T, S extends IService<?>> extends InsertVerifyHandler<T, S>, UpdateVerifyHandler<T, S> {
  

}
