package com.tao.hai.service;

import com.tao.hai.bean.LoginUser;
import com.tao.hai.bean.User;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    /**
     * 登录
     */
    LoginUser login(LoginUser loginUser);

    /**
     * 登出
     */
    void logout();

    /**
     * 获取当前登录用户名
     */
    String getCurrentUserName();

    /**
     * 获取当前登录用户
     */
    User getCurrentUser();

    /**
     * 获取当前登录用户的Session
     */
    Session getSession();
}
