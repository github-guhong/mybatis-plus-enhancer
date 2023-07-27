package guhong.play.mybatisplusenhancer.factory;

import guhong.play.mybatisplusenhancer.base.workbox.CrudWorkBox;
import guhong.play.mybatisplusenhancer.constants.enums.CrudType;
import guhong.play.mybatisplusenhancer.handler.core.CrudHandler;
import guhong.play.mybatisplusenhancer.handler.core.impl.*;
import org.aspectj.lang.JoinPoint;

/**
 * 增删改查处理器工厂
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class CrudHandlerFactory {


    public static CrudHandler create(CrudType crudType, JoinPoint joinPoint) {
        CrudWorkBox crudWorkBox = CrudWorkBoxFactory.create(crudType, joinPoint);
        switch (crudType) {
            case INSERT:
                return new InsertHandler(crudWorkBox);
            case DELETE:
                return new DeleteHandler(crudWorkBox);
            case UPDATE:
                return new UpdateHandler(crudWorkBox);
            case SELECT:
                return new SelectListHandler(crudWorkBox);
            case SELECT_ONE:
                return new SelectSingleHandler(crudWorkBox);
            case SELECT_EXIST:
                return new SelectExistHandler(crudWorkBox);
            default:
                return new CrudHandler(crudWorkBox);
        }
    }

}
