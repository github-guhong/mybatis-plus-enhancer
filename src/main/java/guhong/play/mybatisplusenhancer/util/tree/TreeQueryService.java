package guhong.play.mybatisplusenhancer.util.tree;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import javax.validation.constraints.NotNull;
import guhong.play.mybatisplusenhancer.constants.FieldConstant;
import guhong.play.mybatisplusenhancer.constants.ValueConstant;
import guhong.play.mybatisplusenhancer.constants.enums.OrderType;
import guhong.play.mybatisplusenhancer.exception.TreeQueryException;
import guhong.play.mybatisplusenhancer.util.CrudUtil;
import guhong.play.mybatisplusenhancer.util.TextUtil;
import guhong.play.mybatisplusenhancer.util.ToolKit;

import java.util.List;

/**
 * 树结构的查询服务
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@SuppressWarnings("all")
public interface TreeQueryService {

    /**
     * 树结构查询
     *
     * @param page         分页对象
     * @param queryWrapper 条件构造器
     * @return 返回数据
     */
    public IPage<? extends Node> doQuery(IPage<? extends Node> page, QueryWrapper<? extends Node> queryWrapper);

    /**
     * 执行查询
     *
     * @param page           分页对象
     * @param queryWrapper   条件构造器
     * @param orderFieldList 排序字段列表。因为子节点的筛选条件需要与父节点独立，但排序条件时通用的。所以独立处理
     * @return 返回数据
     */
    public default IPage<? extends Node> buildTree(IPage<? extends Node> page, QueryWrapper<? extends Node> queryWrapper,
                                                   String... orderFieldList) {
        // 如果没有任何查询条件则加上父级id作为条件
        if (MapUtil.isEmpty(queryWrapper.getParamNameValuePairs())) {
            queryWrapper.eq(FieldConstant.PARENT_ID_, ValueConstant.PARENT_ID_INT);
        }
        if (ArrayUtil.isNotEmpty(orderFieldList)) {
            setOrder(queryWrapper, orderFieldList);
        }
        IPage<? extends Node> result = this.doQuery(page, queryWrapper);
        if (ToolKit.isEmpty(result)) {
            return result;
        }
        List newList = CollectionUtil.newArrayList();
        for (Node vo : result.getRecords()) {
            // 向上查，查到最顶部
            Node root = getParents(vo, orderFieldList);
            // 向下查，查到最底部
            getChildren(vo, orderFieldList);
            newList.add(root);
        }
        // 去重
        newList = CollectionUtil.newArrayList(CollectionUtil.newHashSet(newList));
        result.setRecords(newList);
        return result;
    }

    /**
     * 根据条件向上查询数据
     *
     * @param node 树对象
     * @return 返回数据
     */
    public default Node getParents(@NotNull Node node, String... orderList) {
        Object parentId = node.getParentIdValue();
        if (ValueConstant.PARENT_ID_INT.equals(parentId)) {
            // 如果是0，表示已经是最顶级了
            return node;
        }
        QueryWrapper<? extends Node> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TextUtil.toUnderline(node.getIdName()), parentId);
        if (ArrayUtil.isNotEmpty(orderList)) {
            setOrder(queryWrapper, orderList);
        }

        List<? extends Node> upTreeList = this.doQuery(CrudUtil.newMaxPage(), queryWrapper).getRecords();
        if (CollectionUtil.isEmpty(upTreeList)) {
            throw new TreeQueryException(node.getIdValue() + "的父级不存在，请检查数据完整性！");
        }
        Node parent = upTreeList.get(0);
        parent.addChildren(node);
        Object grandParentId = parent.getParentIdValue();
        if (grandParentId.equals(ValueConstant.PARENT_ID_INT)) {
            return parent;
        } else {
            Node grandParent = getParents(parent);
            return grandParent;
        }
    }


    /**
     * 根据条件向下查询数据
     *
     * @param node 树对象
     */
    public default void getChildren(@NotNull Node node, String... orderList) {
        QueryWrapper<? extends Node> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TextUtil.toUnderline(node.getParentIdName()), node.getIdValue());
        if (ArrayUtil.isNotEmpty(orderList)) {
            setOrder(queryWrapper, orderList);
        }

        List<? extends Node> children = this.doQuery(CrudUtil.newMaxPage(), queryWrapper).getRecords();
        if (CollectionUtil.isEmpty(children)) {
            return;
        }
        node.setChildren(children);
        for (Node child : children) {
            getChildren(child);
        }
    }


    /**
     * 设置排序
     *
     * @param queryWrapper 条件构造器
     * @param orderList    排序列表：create_time desc
     */
    public default void setOrder(QueryWrapper<? extends Node> queryWrapper, String... orderList) {
        for (String order : orderList) {
            String[] split = order.split(" ");
            if (split.length != 2) {
                throw new TreeQueryException(order + "是个非法的排序值。应该使用【排序字段 排序方式】的方式。如：create_time desc");
            }
            String orderField = split[0];
            String orderWay = split[1];
            queryWrapper.orderBy(true, OrderType.isAsc(orderWay), TextUtil.toUnderline(orderField));
        }
    }


}
