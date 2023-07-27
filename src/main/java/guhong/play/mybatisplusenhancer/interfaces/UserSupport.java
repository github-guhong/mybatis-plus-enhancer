package guhong.play.mybatisplusenhancer.interfaces;

import java.io.Serializable;

/**
 * 用户支持接口
 * 实现接口后需要将其注入spring中，否则无法使用到
 * @author 李双凯
 * @date 2019/10/22 9:38
 */
public interface UserSupport {


    /**
     * 获得当前用户的id
     * @return 返回当前用户的id
     */
    public Serializable getCurrentUserId();


}
