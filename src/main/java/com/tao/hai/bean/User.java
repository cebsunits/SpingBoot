package com.tao.hai.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tao.hai.base.BaseDataEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


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
     * 电话
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @Column(name="expired_date")
    private Date expiredDate;

    /**
     * 备注
     */
    @Column(name="remarks")
    private String remarks;
    /**
     * 部门
     */
    @Transient
    private List<Dept> deptList;
    /**
     * 角色
     */
    @Transient
    private List<Role> roleList;
    /**
     * 权限
     */
    @Transient
    private List<Permission> permissionList;
    /**加盐*/
    public  String getCredentialsSalt(){
      return this.loginName;
    }
}
