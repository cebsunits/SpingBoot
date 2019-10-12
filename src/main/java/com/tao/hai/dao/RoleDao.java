package com.tao.hai.dao;


import com.tao.hai.base.BaseDao;
import com.tao.hai.bean.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleDao extends BaseDao<Role> {
    /**获取用户对应的角色信息*/
    public List<Role> userRoles(String userId);
}
