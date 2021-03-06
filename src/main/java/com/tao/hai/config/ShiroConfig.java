package com.tao.hai.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.tao.hai.listener.ShiroSessionListener;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @description: shiro配置文件
 **/

@Configuration
public class ShiroConfig {

    // cas server地址
    @Value("${cas.server.url.prefix}")
    String casServerUrlPrefix = "http://127.0.0.1";
    // Cas登录页面地址
    @Value("${cas.server.loginUrl}")
    String casLoginUrl = casServerUrlPrefix + "/login";
    // Cas登出页面地址
    @Value("${cas.server.logoutUrl}")
    String casLogoutUrl = casServerUrlPrefix + "/logout";
    // 当前工程对外提供的服务地址
    @Value("${shiro.server.url.prefix}")
    public static final String shiroServerUrlPrefix = "http://127.0.1.28:8080";
    // casFilter UrlPattern
    @Value("${cas.filter.url.pattern}")
    public static final String casFilterUrlPattern = "/index";
    // 登录地址
    String loginUrl = casLoginUrl + "?service=" + shiroServerUrlPrefix + casFilterUrlPattern;
    // 登出地址（casserver启用service跳转功能，需在webapps\cas\WEB-INF\cas.properties文件中启用cas.logout.followServiceRedirects=true）
    String logoutUrl = casLogoutUrl + "?service=" + loginUrl;
    // 登录成功地址
    // public static final String loginSuccessUrl = "/index";
    // 权限认证失败跳转地址
    String unauthorizedUrl = "/error/403.html";

    /**
     * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
     *
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * 注入自定义的realm，告诉shiro如何获取用户信息来做登录认证和授权
     */
    @Bean
    public SpringBootShrioRealm customRealm() {
        SpringBootShrioRealm customRealm = new SpringBootShrioRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        customRealm.setCacheManager(cacheManager());
        customRealm.setCachingEnabled(true);
        return customRealm;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        // storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    @Bean
    @ConditionalOnMissingBean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        /**
         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole注解，会导致该方法无法映射请求，导致返回404。
         * 加入这项配置能解决这个bug
         */
        creator.setUsePrefix(true);
        return creator;
    }

    /**
     * 这里统一做鉴权，即判断哪些请求路径需要用户登录，哪些请求路径不需要用户登录。
     * 这里只做鉴权，不做权限控制，因为权限用注解来做。
     *
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        // 设置哪些请求可以匿名访问
        // 配置不会被拦截的链接 顺序判断
        chain.addPathDefinition("/login/**", "anon");
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        chain.addPathDefinition("/logout", "logout");
        chain.addPathDefinition("/error/401", "anon");
        chain.addPathDefinition("/error/403", "anon");
        // 验证码可以匿名访问
        chain.addPathDefinition("/getVerifyCode/**", "anon");
        /**静态资源*/
        chain.addPathDefinition("/webjars/**", "anon");
        chain.addPathDefinition("/static/**", "anon");
        chain.addPathDefinition("/css/**", "anon");
        chain.addPathDefinition("/fonts/**", "anon");
        chain.addPathDefinition("/images/**", "anon");
        chain.addPathDefinition("/js/**", "anon");
        // 配置记住我或认证通过可以访问的地址
//       chain.addPathDefinition("/", "user");
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        chain.addPathDefinition("/**", "authc");

        return chain;
    }


    /**
     * 开启shiro aop注解支持 使用代理方式所以需要开启代码支持
     * 一定要写入上面advisorAutoProxyCreator（）自动代理。不然AOP注解不会生效
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    /**
     * session监听
     */
    @Bean
    public ShiroSessionListener shiroSessionListener() {
        return new ShiroSessionListener();
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(shiroSessionListener());
        sessionManager.setSessionListeners(listeners);
        return sessionManager;
    }
    /**解决重复验证授权问题，注意名字更换，与spring-boot-autoconfig里面的名字重复，需要重命名*/
    @Bean(value="memoryConstrainedCacheManager")
    protected CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }
    /**
     * 未授权异常处理
     *
     * @return
     */
    @Bean
    public HandlerExceptionResolver solver() {
        HandlerExceptionResolver handlerExceptionResolver = new UnauthorizedExceptionResolver();
        return handlerExceptionResolver;
    }
    /**注册跨域过滤器**/
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        final UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration=new CorsConfiguration();
        //运行跨域
        configuration.setAllowCredentials(true);
        // 允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        List<String> allowedOrigins=new ArrayList<String>();
        allowedOrigins.add("*");
        configuration.setAllowedOriginPatterns(allowedOrigins);
        // 允许访问的头信息,*表示全部
        List<String> allowedHeaders=new ArrayList<String>();
        allowedHeaders.add("*");
        configuration.setAllowedHeaders(allowedHeaders);
        // 预检请求（options请求）的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        configuration.setMaxAge(1L);
        // 允许提交请求的方法，*表示全部允许
        List<String> allowedMethods=new ArrayList<String>();
        allowedMethods.add("*");

        /**可指定方法 start*/
//        allowedMethods.add("OPTIONS");
//        allowedMethods.add("HEAD");
//        allowedMethods.add("GET");
//        allowedMethods.add("PUT");
//        allowedMethods.add("POST");
//        allowedMethods.add("DELETE");
//        allowedMethods.add("PATCH");
        /**可指定方法 end*/
        configuration.setAllowedMethods(allowedMethods);
        //设置允许跨域的路径
        source.registerCorsConfiguration("/*",configuration);
        FilterRegistrationBean<CorsFilter> bean=new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        // 设过滤器的优先级
        bean.setOrder(0);
        return bean;
    }

}
