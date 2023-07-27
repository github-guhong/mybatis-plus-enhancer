package guhong.play.mybatisplusenhancer.constants.enums;

/**
 * 增删改查类型枚举
 * @author 李双凯
 * @date 2019/9/20 21:00
 */
public enum CrudType {
    /**
     * 添加操作
     */
    INSERT,
    /**
     * 删除操作
     */
    DELETE,

    /**
     * 更新操作
     */
    UPDATE,

    /**
     * 查询操作
     */
    SELECT,
    /**
     * 查询是否存在操作
     */
    SELECT_EXIST,
    /**
     * 查询单个操作
     */
    SELECT_ONE,

    ;



    public boolean isSelect() {
        return this.equals(CrudType.SELECT) || this.equals(CrudType.SELECT_EXIST) || this.equals(CrudType.SELECT_ONE);
    }
}
