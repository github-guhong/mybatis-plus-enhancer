package guhong.play.mybatisplusenhancer.interfaces;

/**
 * 表示可以释放一些资源
 *
 * @author : 李双凯
 * @date : 2022/4/19 11:09
 **/
public interface Releasable {

    /**
     * 释放一些资源
     */
    default void release() {
    }
}
