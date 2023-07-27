package guhong.play.mybatisplusenhancer.util.tree;

import com.alibaba.fastjson.annotation.JSONField;
import guhong.play.mybatisplusenhancer.constants.FieldConstant;

import java.util.Collection;

/**
 * 表示一个树形结构数据
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface Node {

    /**
     * 获得树id
     *
     * @return 返回id
     */
    @JSONField(serialize = false)
    public Object getIdValue();

    /**
     * 获得父级id
     *
     * @return 父级id
     */
    @JSONField(serialize = false)
    public Object getParentIdValue();


    /**
     * 获得id字段名
     *
     * @return 返回id字段名
     */
    @JSONField(serialize = false)
    public default String getIdName() {
        return FieldConstant.ID;
    }

    /**
     * 获得父级id字段名
     *
     * @return 父级id
     */
    @JSONField(serialize = false)
    public default String getParentIdName() {
        return FieldConstant.PARENT_ID;
    }

    /**
     * 获得树的子节点
     *
     * @return 返回字节点
     */
    public Collection<? extends Node> getChildren();

    /**
     * 设置子节点
     *
     * @param nodeList 子节点
     */
    public void setChildren(Collection<? extends Node> nodeList);

    /**
     * 添加子节点
     *
     * @param child 字节点
     */
    public void addChildren(Node child);
}
