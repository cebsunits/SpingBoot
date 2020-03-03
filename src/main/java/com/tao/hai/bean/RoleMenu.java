package com.tao.hai.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name="sys_role_menu")
public class RoleMenu implements Serializable {
    @Id
    @Column(name="role_id")
    private String roleId;
    @Id
    @Column(name="menu_Id")
    private String meunId;//主键.
}
