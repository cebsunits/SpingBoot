package com.tao.hai.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name="sys_role_permission")
public class RolePermission implements Serializable {
    @Id
    @Column(name="role_id")
    private String roleId;
    @Id
    @Column(name="permission_Id")
    private String permissionId;//主键.
}
