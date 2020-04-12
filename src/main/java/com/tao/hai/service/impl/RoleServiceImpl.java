package com.tao.hai.service.impl;

import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.bean.Role;
import com.tao.hai.dao.RoleDao;
import com.tao.hai.service.RoleService;
import com.tao.hai.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleDao, Role> implements RoleService {

    @Autowired
    RoleDao roleDao;

    public List<Role> userRoles(String userId) {
        return roleDao.userRoles(userId);
    }

    /**
     * 根据ID获取角色
     */
    public Role getRole(String roleId) {
        Role queryRole = new Role();
        queryRole.setRoleId(roleId);
        return super.getByKey(queryRole);
    }


    /**
     * 判断用户是否存在
     */
    public boolean checkRoleExists(String role) {
        boolean isExists = false;
        Role queryRole = new Role();
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
            role.setCreateId(ShiroUtils.getUserId());
            role.setCreateDate(new Date());
        } else {
            role.setNewRecord(false);
        }
        role.setUpdateDate(new Date());
        role.setUpdateId(ShiroUtils.getUserId());
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
    @CacheEvict(value = "menuCache", key = "#role.roleId")
    public void grant(Role role) {
        /**删除关联表*/
        roleDao.deleteRoleMenu(role);
        /**更新关联表*/
        roleDao.insertRoleMenu(role);
    }

}
