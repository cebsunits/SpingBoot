package com.tao.hai.service.impl;

import com.tao.hai.bean.LoginUser;
import com.tao.hai.bean.User;
import com.tao.hai.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    /**
     * 登录
     */
    public LoginUser login(LoginUser loginUser) {

        if (StringUtils.isEmpty(loginUser.getLoginName())) {
            loginUser.setLogin(false);
            loginUser.setResult("用户名为空");
            return loginUser;
        }
        if (StringUtils.isEmpty(loginUser.getPassword())) {
            loginUser.setLogin(false);
            loginUser.setResult("用户密码为空");
            return loginUser;
        }
        String msg = "";
        // 1、获取Subject实例对象
        Subject currentUser = SecurityUtils.getSubject();
        String md5Password = loginUser.getPassword();
        // 2、将用户名和密码封装到UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(loginUser.getLoginName(), md5Password);
        // 4、认证
        try {
            currentUser.login(token);// 传到MyAuthorizingRealm类中的方法进行认证
            Session session = currentUser.getSession();
            session.setAttribute("userName", loginUser.getLoginName());
            loginUser.setLogin(true);
            return loginUser;
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            msg = "UnknownAccountException -- > 账号不存在：";
        } catch (IncorrectCredentialsException e) {
            msg = "IncorrectCredentialsException -- > 密码不正确：";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            msg = "用户验证失败";
        }

        loginUser.setLogin(false);
        loginUser.setResult(msg);
        return loginUser;
    }

    /**
     * 登出
     */
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    /**
     * 获取当前登录用户名
     */
    public String getCurrentUserName() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return (String) session.getAttribute("userName");
    }

    /**
     * 获取当前登录用户
     */
    public User getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        User principal = (User) subject.getPrincipal();
        return principal;
    }

    /**
     * 获取当前登录用户的Session
     */
    public Session getSession() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return session;
    }
}
