package com.tao.hai.dao;


import com.tao.hai.base.BaseDao;
import com.tao.hai.bean.Dept;

import java.util.List;

public interface DeptDao extends BaseDao<Dept> {

    /**
     * 获取最父级部门信息
     */
    List<Dept> deptParent(Dept dept);
}
