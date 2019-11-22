package com.tao.hai.dao;


import com.tao.hai.base.BaseDao;
import com.tao.hai.bean.Role;

import java.util.List;

public interface RoleDao extends BaseDao<Role> {
    /**获取用户对应的角色信息*/
    public List<Role> userRoles(String userId);
}
