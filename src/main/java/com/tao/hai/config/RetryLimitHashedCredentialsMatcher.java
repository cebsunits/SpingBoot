package com.tao.hai.config;

import com.tao.hai.bean.User;
import com.tao.hai.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;
    /***缓存名称**/
    public static final String DEFAULT_RETRY_LIMIT_CACHE = "passwordRetryCache";
    @Autowired
    UserService userService;
    @Value("${shiro.retryLimit}")
    private int limitTimes;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache(DEFAULT_RETRY_LIMIT_CACHE);
    }

    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken tokens = (UsernamePasswordToken) token;
        String userName = tokens.getUsername();
        AtomicInteger retryCount = passwordRetryCache.get(userName);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(userName, retryCount);
        }
        if (retryCount.incrementAndGet() > limitTimes) {
            User user = userService.getUser(userName);
            if (user != null) {
                user.setStatus(false);
                userService.save(user);
            }
            /**用户锁定*/
            throw new LockedAccountException();
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            /**密码验证正确，清除缓存*/
            passwordRetryCache.remove(userName);
        }
        return matches;
    }
}
