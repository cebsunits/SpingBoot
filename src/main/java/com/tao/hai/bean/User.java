package com.tao.hai.bean;

import com.tao.hai.base.BaseDataEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name="sys_user")
public class User extends BaseDataEntity<User> {
    /**
     * 用户唯一标识
     */
    @Id
    @Column(name="user_id")
    private String userId;
    /**
     * 用户名
     */
    @Column(name="user_name")
    private String userName;
    /**
     * 密码
     */
    @Column(name="password")
    private String password;
    /**
     * 登录名
     */
    @Column(name="login_name")
    private String loginName;
    /**
     * 邮箱
     */
    @Column(name="email")
    private String email;
    /**
     * 电话
     */
    @Column(name="phone")
    private String phone;
    /**
     * 备注
     */
    @Column(name="remarks")
    private String remarks;
}
