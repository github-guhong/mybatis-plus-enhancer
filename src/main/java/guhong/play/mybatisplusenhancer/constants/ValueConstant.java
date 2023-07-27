package guhong.play.mybatisplusenhancer.constants;

/**
 * 通用的常量池
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface ValueConstant {

    /**
     * string类型的父级id
     */
    String PARENT_ID_STR = "0";

    /**
     * int类型的父级id
     */
    Integer PARENT_ID_INT = 0;

    /**
     * 逻辑删除：0表示没删除
     */
    Integer LOGIC_NOT_DELETE = 0;

    /**
     * 逻辑删除：1表示删除
     */
    Integer LOGIC_DELETE = 1;

    /**
     * 默认sort值，降序
     */
    Integer DEFAULT_SORT = 1;


    /**
     * user角色的id值
     */
    Integer USER_ROLE_ID = 1;

    /**
     * root角色的id值
     */
    Integer ROOT_ROLE_ID = 0;

}
