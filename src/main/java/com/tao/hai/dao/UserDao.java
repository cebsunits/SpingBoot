package com.tao.hai.dao;


import com.tao.hai.base.BaseDao;
import com.tao.hai.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao extends BaseDao<User> {

    /**更新部门关联表*/
    int insertUserDept(User user);
    /**更新角色关联表*/
    int insertUserRole(User user);
    /**删除部门关联表*/
    int deleteUserDept(User user);
    /**删除角色关联表*/
    int deleteUserRole(User user);
}
