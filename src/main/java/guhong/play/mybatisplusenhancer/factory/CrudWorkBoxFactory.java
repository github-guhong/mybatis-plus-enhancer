package guhong.play.mybatisplusenhancer.factory;

import guhong.play.mybatisplusenhancer.base.workbox.*;
import guhong.play.mybatisplusenhancer.constants.enums.CrudType;
import lombok.Data;
import org.aspectj.lang.JoinPoint;

/**
 * Crud工具箱工厂
 * @author : 李双凯
 * @date : 2022/3/19 20:37
 **/
@Data
public class CrudWorkBoxFactory {

    public static CrudWorkBox create(CrudType crudType, JoinPoint joinPoint) {
        switch (crudType) {
            case INSERT:
                return new InsertWorkBox(joinPoint);
            case DELETE:
                return new DeleteWorkBox(joinPoint);
            case UPDATE:
                return new UpdateWorkBox(joinPoint);
            case SELECT:
                return new SelectListWorkBox(joinPoint);
            case SELECT_ONE:
                return new SelectSingleWorkBox(joinPoint);
            case SELECT_EXIST:
                return new SelectExistWorkBox(joinPoint);
            default:
                return new CrudWorkBox(joinPoint);
        }
    }
}
