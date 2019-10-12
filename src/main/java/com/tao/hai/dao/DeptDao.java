package com.tao.hai.dao;


import com.tao.hai.base.BaseDao;
import com.tao.hai.bean.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DeptDao extends BaseDao<Dept> {

    /**获取最父级部门信息*/
    public List<Dept> deptParent(Dept dept);
}
