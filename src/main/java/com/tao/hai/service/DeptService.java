package com.tao.hai.service;

import com.tao.hai.base.BaseService;
import com.tao.hai.base.TreeNode;
import com.tao.hai.bean.Dept;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeptService extends BaseService<Dept> {
    /**
     * 判断用户是否存在
     */
    boolean checkDeptExists(String deptName);

    /**
     * 获取部门信息
     */
    Dept getDept(String deptId);

    /**
     * 生成树
     */
    List<TreeNode<Dept>> buildDeptTree(Dept deptQuery);
}
