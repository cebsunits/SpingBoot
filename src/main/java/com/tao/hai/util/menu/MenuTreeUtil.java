package com.tao.hai.util.menu;

import com.tao.hai.bean.Menu;
import com.tao.hai.util.MyBeanUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuTreeUtil {
    /**树节点集合信息*/
    static List<Menu> allList;
    /**生成bootstrap树*/
    public static List<Menu> treeList(List<Menu> list){
        allList=list;
        List<Menu> treeList=new ArrayList<>();
        if(list!=null&&!list.isEmpty()){
            for(Menu treeNode:list){
                //获取父亲节点
                String parentId=treeNode.getParentId();
                //父亲节点信息
                if(StringUtils.isEmpty(parentId)){
                    try {
                        Menu node=createTreeNode(treeNode);
                        treeList.add(node);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
        }
        return treeList;
    }
    /**获取对应部门信息的子部门*/
    public static List<Menu> treeChild(String parentId){
        List<Menu> list=new ArrayList<>( );
        for(Menu treeNode:allList){
            if(parentId.equals(treeNode.getParentId())){
                try {
                    Menu node=createTreeNode(treeNode);
                    list.add(node);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    /**创建树节点信息*/
    public static Menu createTreeNode(Menu treeNode) throws Exception{
        Menu menu=new Menu();
        MyBeanUtils.copyBean2Bean(treeNode,menu);
        menu.setSubMenuList(treeChild(menu.getMenuId()));
        return menu;
    }
}
