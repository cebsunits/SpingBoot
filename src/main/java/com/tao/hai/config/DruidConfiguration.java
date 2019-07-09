package com.tao.hai.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.sql.SQLException;

@Configuration
public class DruidConfiguration {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;
    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;
    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;
    @Value("${spring.datasource.druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.druid.filters}")
    private String filters;
    @Value("${spring.datasource.druid.connectionProperties}")
    private String connectionProperties;
    @Value("${spring.datasource.druid.ConnectionErrorRetryAttempts}")
    private int connectionErrorRetryAttempts;
    @Value("${spring.datasource.druid.BreakAfterAcquireFailure}")
    private boolean breakAfterAcquireFailure;
    @Value("${spring.datasource.druid.timeBetweenConnectErrorMillis}")
    private int timeBetweenConnectErrorMillis;

    @Value("${spring.datasource.druid.stat-view-servlet.url-pattern}")
    private String statUrl;
    @Value("${spring.datasource.druid.stat-view-servlet.allow}")
    private String statAllow;
    @Value("${spring.datasource.druid.stat-view-servlet.deny}")
    private String statDeny;
    @Value("${spring.datasource.druid.stat-view-servlet.reset-enable}")
    private String statResetEnable;
    @Value("${spring.datasource.druid.stat-view-servlet.login-username}")
    private String statUsername;
    @Value("${spring.datasource.druid.stat-view-servlet.login-password}")
    private String statPassword;

    @Value("${spring.datasource.druid.web-stat-filter.url-pattern}")
    private String statFilterUrl;
    @Value("${spring.datasource.druid.web-stat-filter.name}")
    private String statFilterName;
    @Value("${spring.datasource.druid.web-stat-filter.exclusions}")
    private String statFilterExclusions;
    /**
     * @return
     * @Bean 创建一个实例
     * @Primary 在同样的DataSource中，首先使用被标注的DataSource
     */
    @Bean(name = "dataSource")
    @Primary
    public DruidDataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        datasource.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
        datasource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
        datasource.setTimeBetweenConnectErrorMillis(timeBetweenConnectErrorMillis);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

    /**
     * 注册一个StatViewServlet
     */
    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), statUrl);
        //添加初始化参数：initParams
        //白名单：
        servletRegistrationBean.addInitParameter("allow", statAllow);
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        servletRegistrationBean.addInitParameter("deny", statDeny);
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", statUsername);
        servletRegistrationBean.addInitParameter("loginPassword", statPassword);
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", statResetEnable);// 禁用HTML页面上的“Reset All”功能
        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.setName(statFilterName);
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns(statFilterUrl);
        //添加忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", statFilterExclusions);
        return filterRegistrationBean;
    }

}