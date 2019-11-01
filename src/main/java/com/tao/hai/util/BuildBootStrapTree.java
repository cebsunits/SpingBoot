package com.tao.hai.util;

import com.tao.hai.base.BootstrapUITreeNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BuildBootStrapTree {
    /**树节点集合信息*/
    static List<BootstrapUITreeNode> allList;
    /**生成bootstrap树*/
    public static List<BootstrapUITreeNode> buildDeptTree(List<BootstrapUITreeNode> list){
        allList=list;
        List<BootstrapUITreeNode> treeList=new ArrayList<>();
        if(list!=null&&!list.isEmpty()){
            for(BootstrapUITreeNode treeNode:list){
                //获取父亲节点
                String parentId=treeNode.getParentId();
                //父亲节点信息
                if(StringUtils.isEmpty(parentId)){
                    BootstrapUITreeNode node=createTreeNode(treeNode);
                    treeList.add(node);
                    continue;
                }
            }
        }
        return treeList;
    }
    /**获取对应部门信息的子部门*/
    public static List<BootstrapUITreeNode> treeChild(String parentId){
        List<BootstrapUITreeNode> list=new ArrayList<>( );
        for(BootstrapUITreeNode treeNode:allList){
            if(parentId.equals(treeNode.getParentId())){
                BootstrapUITreeNode node=createTreeNode(treeNode);
                list.add(node);
            }
        }
        return list;
    }

    public static BootstrapUITreeNode createTreeNode(BootstrapUITreeNode treeNode){
        BootstrapUITreeNode node=new BootstrapUITreeNode();
        node.setId(treeNode.getId());
        node.setParentId(treeNode.getParentId());
        node.setText(treeNode.getText());
        node.setHref(treeNode.getHref());
        node.setBackColor(treeNode.getBackColor());
        node.setColor(treeNode.getColor());
        node.setIcon(treeNode.getIcon());
        node.setSelectedIcon(treeNode.getSelectedIcon());
        node.setSearchResult(treeNode.getSearchResult());
        node.setSelectable(treeNode.isSelectable());
        node.setState(treeNode.getState());
        node.setTags(treeNode.getTags());
        node.setNodes(treeChild(node.getId()));
        return node;
    }
}
