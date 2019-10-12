package com.tao.hai.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    //验证码过期时间默认60秒
    @Value("${login.verifyTTL}")
    private long verifyTTL=60;
	@Override  
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 在这里进行验证码的校验  
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();
        // 取出页面的验证码
        // 输入的验证和session中的验证进行对比  
        String verifyCode = httpServletRequest.getParameter("verifyCode");
        // 验证码
        String rightCode = (String)session.getAttribute("verifyCode");
        Long verifyCodeTTL = (Long) session.getAttribute("verifyCodeTTL");
        Long currentMillis = System.currentTimeMillis();
        if (rightCode == null || verifyCodeTTL == null) {
            //自定义登录异常
            httpServletRequest.setAttribute("shiroLoginFailure", "verifyCodeNull");
            return true;
        }
        Long expiredTime = (currentMillis - verifyCodeTTL) / 1000;
        if (expiredTime > this.verifyTTL) {
            //自定义登录异常
            httpServletRequest.setAttribute("shiroLoginFailure", "verifyCodeValidateTimeout");
            return true;
        }
        if (!verifyCode.equalsIgnoreCase(rightCode)) {
            //自定义登录异常
            httpServletRequest.setAttribute("shiroLoginFailure", "verifyCodeValidateFailed");
            return true;
        }
        return super.onAccessDenied(request, response);  
    }

    /**
     * 因为发现设置的successUrl没生效，所以追踪源码发现如果SavedRequest对象不为null,则它会覆盖掉我们设置
     * 的successUrl，所以我们要重写onLoginSuccess方法，在它覆盖掉我们设置的successUrl之前，去除掉
     * SavedRequest对象,SavedRequest对象的获取方式为：
     * savedRequest = (SavedRequest) session.getAttribute(SAVED_REQUEST_KEY);
     * public static final String SAVED_REQUEST_KEY = "shiroSavedRequest";
     * 解决方案：从session对象中移出shiroSavedRequest
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        if (!StringUtils.isEmpty(getSuccessUrl())) {
            // getSession(false)：如果当前session为null,则返回null,而不是创建一个新的session
            Session session = subject.getSession(false);
            if (session != null) {
                session.removeAttribute("shiroSavedRequest");
            }
        }
        return super.onLoginSuccess(token, subject, request, response);
    }
}