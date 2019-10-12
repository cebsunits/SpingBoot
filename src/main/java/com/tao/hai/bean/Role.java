package com.tao.hai.bean;

import com.tao.hai.base.BaseDataEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="sys_role")
public class Role extends BaseDataEntity<Role> {
    /**
     * 角色唯一标识
     */
    @Id
    @Column(name="role_id")
    private String roleId;
    /**
     * 角色代码,如"admin",这个是唯一的:
     */
    @Column(name="role")
    private String role;
    /**
     * 角色名称
     */
    @Column(name="role_name")
    private String roleName;
    /**
     * 角色描述,UI界面显示使用
     */
    @Column(name="description")
    private String description;
    /**
     * 是否可用,如果不可用将不会添加给用户
     */
    @Column(name="available")
    private Boolean available = Boolean.TRUE;
    //角色 -- 权限关系：多对多关系;
    @Transient
    private List<Permission> permissionList;

}