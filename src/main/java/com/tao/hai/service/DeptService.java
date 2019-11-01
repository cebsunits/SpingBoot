package com.tao.hai.service;

import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.base.BootstrapUITreeNode;
import com.tao.hai.bean.Dept;
import com.tao.hai.bean.User;
import com.tao.hai.dao.DeptDao;
import com.tao.hai.util.BuildBootStrapTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DeptService extends BaseServiceImpl<DeptDao, Dept> {
    @Autowired
    DeptDao deptDao;

    /**保存方法*/
    public void save(Dept dept){
        if (StringUtils.isEmpty(dept.getDeptId())) {
            dept.setDeptId(UUID.randomUUID().toString());
        }
        super.save(dept);
    }

    /**
     * 判断用户是否存在
     */
    public boolean checkDeptExists(String deptName) {
        boolean isExists = false;
        Dept queryDept=new Dept();
        queryDept.setDeptName(deptName);
        Dept dept=super.get(queryDept);
        if (dept != null) {
            isExists = true;
        }
        return isExists;
    }
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
        List<Dept> allList=super.findAll();
        for(Dept dept:allList){
            BootstrapUITreeNode node=new BootstrapUITreeNode();
            node.setId(dept.getDeptId());
            node.setParentId(dept.getParentId());
            node.setText(dept.getDeptName());
            list.add(node);
        }
        return BuildBootStrapTree.buildDeptTree(list);
    }
}
