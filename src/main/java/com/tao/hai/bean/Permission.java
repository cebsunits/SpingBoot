package com.tao.hai.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name="sys_permission")
public class Permission implements Serializable {
    @Id
    @Column(name="permission_Id")
    private String permissionId;//主键.
    @Column(name="permission_Name")
    private String permissionName;//名称.
    @Column(name="description")
    private String description;//描述
    @Column(name="resource_type")
    private String resourceType;//资源类型，[menu|button]
    @Column(name="url")
    private String url;//资源路径.
    @Column(name="permission")
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    @Column(name="parentId")
    private Long parentId; //父编号
    @Column(name="parentIds")
    private String parentIds; //父编号列表
    @Column(name="available")
    private Boolean available = Boolean.TRUE;
    @Transient
    private List<Role> roleList;
}
