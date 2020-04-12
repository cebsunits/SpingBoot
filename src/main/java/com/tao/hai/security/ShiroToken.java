package com.tao.hai.security;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户和密码（包含验证码）令牌类
 *
 * @author sunits
 * @version 2020-04-02
 */
public class ShiroToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    private String captcha;
    private boolean mobileLogin;

    public ShiroToken() {
        super();
    }

    public ShiroToken(String username, char[] password,
                      boolean rememberMe, String host, String captcha, boolean mobileLogin) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.mobileLogin = mobileLogin;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }

}