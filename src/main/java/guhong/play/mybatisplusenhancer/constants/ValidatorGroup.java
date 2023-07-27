package guhong.play.mybatisplusenhancer.constants;

/**
 * 验证组接口
 * @author 李双凯
 * @date 2019/9/24 19:52
 */
public interface ValidatorGroup {

    /**
     * 新增组
     */
    interface Insert extends ValidatorGroup{}


    /**
     * 更新组
     */
    interface Update extends ValidatorGroup{}



}
