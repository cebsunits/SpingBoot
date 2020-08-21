package com.tao.hai.service;

import com.tao.hai.base.BaseService;
import com.tao.hai.bean.Menu;
import com.tao.hai.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * Redis:
 * cacheNames 可以对一组进行通用配置，value可以不配置了@CacheConfig(cacheNames={userCache})
 * 参数value是缓存的名字，在执行的时候，会找叫这个名字的缓存使用/删除，注意如果使用，则必须在配置文件中进行配置
 * 参数key默认情况下是空串””,是Spring的一种表达式语言SpEL，我们这里可以随意指定，但是需要注意一定要加单引号
 * RedisConnectionFactory, StringRedisTemplate 和 RedisTemplate
 **/
@Service
public interface UserService extends BaseService<User> {
    /**
     * 获取信息  第二次访问会取缓存
     */

    User getUser(User user);

    /**
     * 获取信息  第二次访问会取缓存
     */

    User getUser(String loginName);

    /**
     * 获取信息  第二次访问会取缓存
     */

    User get(String userId);


    /**
     * 查询用户对应的权限信息
     */
    List<Menu> findUserRolePermission(String userId);

    /**
     * 查询用户对应的角色信息
     */
    Set<String> findUserRole(String userId);

    void deleteAllUserRole(String userId);

    void grantUserRole(String userId, String[] roleList);

    String getDbVersion();

    /**
     * 判断用户是否存在
     */

    boolean checkUserExists(String loginName);

    /**
     * 判断用户是否存在
     */

    boolean checkUserExists2(String userName, String userName2);

    /**
     * 添加redis缓存
     */
    void updatePassword(User user);
    /**获取不同的用户名称信息*/
    List listUsers();
}
