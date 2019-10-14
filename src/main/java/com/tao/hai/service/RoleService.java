package com.tao.hai.service;

import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.base.BootstrapUITreeNode;
import com.tao.hai.bean.Dept;
import com.tao.hai.bean.Role;
import com.tao.hai.dao.DeptDao;
import com.tao.hai.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService extends BaseServiceImpl<RoleDao, Role> {

    @Autowired
    RoleDao roleDao;
    public List<Role> userRoles(String userId){
        return roleDao.userRoles(userId);
    }
    /**根据ID获取角色*/
    public Role getRole(String roleId){
        Role queryRole=new Role();
        queryRole.setRoleId(roleId);
       return super.getByKey(queryRole);
    }
}
