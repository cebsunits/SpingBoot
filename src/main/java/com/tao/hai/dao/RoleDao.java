package com.tao.hai.dao;


import com.tao.hai.base.BaseDao;
import com.tao.hai.bean.Role;

import java.util.List;

public interface RoleDao extends BaseDao<Role> {
    /**
     * 获取用户对应的角色信息
     */
    List<Role> userRoles(String userId);

    /**
     * 更新角色关联表
     */
    int insertRoleMenu(Role role);

    /**
     * 删除角色菜单关联表
     */
    int deleteRoleMenu(Role role);
}
