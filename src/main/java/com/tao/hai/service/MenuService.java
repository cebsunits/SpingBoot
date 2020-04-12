package com.tao.hai.service;

import com.tao.hai.base.BaseService;
import com.tao.hai.base.TreeNode;
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
public interface MenuService extends BaseService<Menu> {
    /**
     * 获取信息  第二次访问会取缓存
     */
    Menu getMenu(Menu menu);

    /**
     * 获取信息  第二次访问会取缓存
     */
    List<TreeNode<Menu>> listMenuTree(User user);

    /**
     * 获取信息  第二次访问会取缓存
     */
    List<TreeNode<Menu>> getRoleMenuTree(String roleId);

    /**
     * 获取信息  第二次访问会取缓存
     */
    List<Menu> getRoleMenus(String roleId);

    /**
     * 获取用户菜单信息
     */
    Set<String> listPerms(String userId);
}
