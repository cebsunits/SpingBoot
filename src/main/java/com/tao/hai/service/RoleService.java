package com.tao.hai.service;

import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.base.BootstrapUITreeNode;
import com.tao.hai.bean.Dept;
import com.tao.hai.bean.Role;
import com.tao.hai.bean.User;
import com.tao.hai.dao.DeptDao;
import com.tao.hai.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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


    /**
     * 判断用户是否存在
     */
    public boolean checkRoleExists(String role) {
        boolean isExists = false;
        Role queryRole=new Role();
        queryRole.setRole(role);
        Role roleObj = super.get(queryRole);
        if (roleObj != null) {
            isExists = true;
        }
        return isExists;
    }

    /**
     * 添加redis缓存
     */
    public void save(Role role) {
        if (StringUtils.isEmpty(role.getRoleId())) {
            role.setRoleId(UUID.randomUUID().toString());
        }
        super.save(role);
    }
    /**
     * 添加redis缓存
     */
    public void delete(String... ids) {
       super.delete(ids);
    }
    /**
     * 添加redis缓存
     */
    public void del(Role role) {
        super.del(role);
    }


    /**
     * 保存或更新方法
     */
    @CacheEvict(value ="menuCache",key = "#role.roleId")
    public void grant(Role role) {
        /**删除关联表*/
       roleDao.deleteRoleMenu(role);
       /**更新关联表*/
       roleDao.insertRoleMenu(role);
    }

}
