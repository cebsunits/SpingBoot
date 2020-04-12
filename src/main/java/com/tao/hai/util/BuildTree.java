package com.tao.hai.util;

import com.tao.hai.base.TreeNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BuildTree {

    public static <T> List<TreeNode<T>> buildList(List<TreeNode<T>> nodes, String idParam) {
        if (nodes == null) {
            return null;
        }
        /**避免为null情况导致报错*/
        if (StringUtils.isEmpty(idParam)) {
            idParam = "";
        }
        List<TreeNode<T>> topNodes = new ArrayList<>();
        for (TreeNode<T> children : nodes) {
            /**父节点*/
            String parentId = children.getParentId();
            /**组织父节点*/
            if (StringUtils.isEmpty(parentId) || idParam.equals(parentId)) {
                topNodes.add(children);
                continue;
            }
            /**组织子节点*/
            for (TreeNode<T> parent : nodes) {
                String id = parent.getId();
                if (StringUtils.isNotEmpty(id) && id.equals(parentId)) {
                    parent.getNodes().add(children);
                    children.setHasParent(true);
                    parent.setHasChildren(true);
                    continue;
                }
            }
        }
        return topNodes;

    }
}
