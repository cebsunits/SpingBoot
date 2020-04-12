package com.tao.hai.service;

import com.tao.hai.base.BaseService;
import com.tao.hai.bean.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService extends BaseService<Role> {

    /**
     * 获取用户角色信息
     */
    List<Role> userRoles(String userId);

    /**
     * 根据ID获取角色
     */
    Role getRole(String roleId);

    /**
     * 判断用户是否存在
     */
    boolean checkRoleExists(String role);

    /**
     * 保存或更新方法
     */
    void grant(Role role);

}
