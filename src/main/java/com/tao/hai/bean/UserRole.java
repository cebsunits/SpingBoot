package com.tao.hai.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name="sys_user_role")
public class UserRole implements Serializable {
    @Id
    @Column(name="user_id")
    private String userId;
    @Id
    @Column(name="role_id")
    private String roleId;
}
