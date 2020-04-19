package com.tao.hai.service.impl;

import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.base.TreeNode;
import com.tao.hai.bean.Dept;
import com.tao.hai.dao.DeptDao;
import com.tao.hai.service.DeptService;
import com.tao.hai.util.BuildTree;
import com.tao.hai.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DeptServiceImpl extends BaseServiceImpl<DeptDao, Dept> implements DeptService {
    @Autowired
    DeptDao deptDao;

    /**
     * 保存方法
     */
    public int save(Dept dept) {
        if (StringUtils.isEmpty(dept.getDeptId())) {
            dept.setDeptId(UUID.randomUUID().toString());
            dept.setCreateDate(new Date());
            dept.setCreateId(ShiroUtils.getUserId());
        } else {
            dept.setNewRecord(false);
        }
        dept.setUpdateDate(new Date());
        dept.setUpdateId(ShiroUtils.getUserId());
        return super.save(dept);
    }

    /**
     * 判断用户是否存在
     */
    public boolean checkDeptExists(String deptCode) {
        boolean isExists = false;
        Dept queryDept = new Dept();
        queryDept.setDeptCode(deptCode);
        Dept dept = super.get(queryDept);
        if (dept != null) {
            isExists = true;
        }
        return isExists;
    }

    /**
     * 获取部门信息
     */
    public Dept getDept(String deptId) {
        Dept queryDept = new Dept();
        queryDept.setDeptId(deptId);
        Dept dept = super.getByKey(queryDept);
        return dept;
    }

    /**
     * 生成树
     */
    public List<TreeNode<Dept>> buildDeptTree(Dept deptQuery) {
        List<Dept> allList = super.findAll();
        /**组装树结构*/
        List<TreeNode<Dept>> trees = new ArrayList<TreeNode<Dept>>();
        for (Dept dept : allList) {
            TreeNode<Dept> tree = new TreeNode<Dept>();
            tree.setId(dept.getDeptId());
            tree.setParentId(dept.getParentId());
            tree.setText(dept.getDeptName());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        List<TreeNode<Dept>> list = BuildTree.buildList(trees, "0");

        return list;
    }
}
