package com.tao.hai.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***bootstrap tree node 节点实体类*/
@Data
public class TreeNode<T> implements Serializable {
    //节点id
    private String id;
    //父节点id
    private String parentId;
    //说明
    private String text;
    //指定列表树的节点是否可选择。设置为false将使节点展开，并且不能被选择。
    private boolean selectable;
    //查找结果
    private String searchResult;
    //结合全局enableLinks选项为列表树节点指定URL。
    private String href;
    /**
     * Object 一个节点的初始状态。
     * state.checked	Boolean，默认值false	指示一个节点是否处于checked状态，用一个checkbox图标表示。
     * state.disabled	Boolean，默认值false	指示一个节点是否处于disabled状态。（不是selectable，expandable或checkable）
     * state.expanded	Boolean，默认值false	指示一个节点是否处于展开状态。
     * state.selected	Boolean，默认值false	指示一个节点是否可以被选择。
     */
    private JSONObject state;
    /**
     * 节点属性
     */
    private Map<String, Object> attributes;
    //列表树节点上的图标，通常是节点左边的图标。
    private String icon;
    //当某个节点被选择后显示的图标，通常是节点左边的图标。
    private String selectedIcon;
    //节点的前景色，覆盖全局的前景色选项。
    private String color;
    //节点的背景色，覆盖全局的背景色选项。
    private String backColor;
    //子节点信息
    private List<TreeNode<T>> nodes = new ArrayList<TreeNode<T>>();
    //通过结合全局showTags选项来在列表树节点的右边添加额外的信息。
    private List<String> tags = new ArrayList<String>();
    /**
     * 是否有父节点
     */
    private boolean hasParent = false;
    /**
     * 是否有子节点
     */
    private boolean hasChildren = false;
}
