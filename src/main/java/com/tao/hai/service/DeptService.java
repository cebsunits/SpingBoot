package com.tao.hai.service;

import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.base.BootstrapUITreeNode;
import com.tao.hai.bean.Dept;
import com.tao.hai.dao.DeptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeptService extends BaseServiceImpl<DeptDao, Dept> {
    @Autowired
    DeptDao deptDao;
    /**获取部门信息*/
    public Dept getDept(String deptId){
        Dept queryDept=new Dept();
        queryDept.setDeptId(deptId);
        Dept dept=super.getByKey( queryDept  );
        return  dept;
    }

    /**生成树*/
    public List<BootstrapUITreeNode> buildDeptTree(Dept deptQuery){
        List<BootstrapUITreeNode> list=new ArrayList<>( );
        List<Dept> parentList=deptDao.deptParent(deptQuery);
        for(Dept dept:parentList){
            BootstrapUITreeNode node=new BootstrapUITreeNode();
            node.setNodeId(dept.getDeptId());
            node.setParentId(dept.getParentId());
            node.setText(dept.getDeptName());
            node.setNodes(treeChild(node.getNodeId()));
            list.add(node);
        }
        return list;
    }
    /**获取对应部门信息的子部门*/
    public List<BootstrapUITreeNode> treeChild(String deptId){
        List<BootstrapUITreeNode> list=new ArrayList<>( );
        List<Dept> allList=super.findAll();
        for(Dept dept:allList){
            if(deptId.equals(dept.getParentId())){
                BootstrapUITreeNode node=new BootstrapUITreeNode();
                node.setNodeId(dept.getDeptId());
                node.setParentId(dept.getParentId());
                node.setText(dept.getDeptName());
                node.setNodes(treeChild(node.getNodeId()));
                list.add(node);
            }
        }
        return list;
    }
}
