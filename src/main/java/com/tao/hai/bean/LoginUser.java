package com.tao.hai.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUser implements Serializable {
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String verifyCode;
    /**
     * 验证码时间
     */
    private Long verifyCodeTTL;
    /**
     * 是否登录成功
     */
    private boolean isLogin = false;
    /**
     * 登录结果
     */
    private String result;
}
