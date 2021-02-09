package com.tao.hai.service.impl;

import com.github.pagehelper.PageInfo;
import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.bean.Menu;
import com.tao.hai.bean.Role;
import com.tao.hai.bean.User;
import com.tao.hai.dao.MenuDao;
import com.tao.hai.dao.RoleDao;
import com.tao.hai.dao.UserDao;
import com.tao.hai.service.UserService;
import com.tao.hai.util.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Redis:
 * cacheNames 可以对一组进行通用配置，value可以不配置了@CacheConfig(cacheNames={userCache})
 * 参数value是缓存的名字，在执行的时候，会找叫这个名字的缓存使用/删除，注意如果使用，则必须在配置文件中进行配置
 * 参数key默认情况下是空串””,是Spring的一种表达式语言SpEL，我们这里可以随意指定，但是需要注意一定要加单引号
 * RedisConnectionFactory, StringRedisTemplate 和 RedisTemplate
 **/
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, User> implements UserService {
    /***缓存名称**/
    private static final String CACHE_NAME = "userCache";
    @Autowired
    UserDao userDao;
    @Autowired
    MenuDao menuDao;
    @Autowired
    RoleDao roleDao;

    /**
     * 获取信息  第二次访问会取缓存
     */
    @Cacheable(value = CACHE_NAME, key = "#user.getUserId()")
    public User getUser(User user) {
        User userInfo = super.get(user);
        return userInfo;
    }

    /**
     * 获取信息  第二次访问会取缓存
     */
    @Cacheable(value = CACHE_NAME, key = "#loginName")
    public User getUser(String loginName) {
        User user = new User();
        user.setLoginName(loginName);
        User userInfo = super.get(user);
        return userInfo;
    }

    /**
     * 获取信息  第二次访问会取缓存
     */
    @Cacheable(value = CACHE_NAME, key = "#userId")
    public User get(String userId) {
        User user = new User();
        user.setUserId(userId);
        User userInfo = super.getByKey(user);
        return userInfo;
    }

    /**
     * 添加redis缓存
     */
    @CachePut(value = CACHE_NAME, key = "#user.getUserId()")
    public int save(User user) {
        if (StringUtils.isEmpty(user.getUserId())) {
            user.setUserId(UUID.randomUUID().toString());
            user.setCreateId(ShiroUtils.getUserId());
            user.setCreateDate(new Date());
        }
        user.setUpdateId(ShiroUtils.getUserId());
        user.setUpdateDate(new Date());
        int num = super.save(user);
        /**删除部门信息**/
        userDao.deleteUserDept(user);
        /**删除角色信息**/
        userDao.deleteUserRole(user);
        /**新增部门信息**/
        if (!user.getDeptList().isEmpty()) {
            userDao.insertUserDept(user);
        }
        /**新增角色信息**/
        if (!user.getRoleList().isEmpty()) {
            userDao.insertUserRole(user);
        }
        return num;
    }

    /**
     * 删除用户信息
     */
    public int delete(String... ids) {
        int num = 0;
        for (String id : ids) {
            User user = userDao.selectByPrimaryKey(id);
            if (user != null) {
                /**删除部门信息**/
                userDao.deleteUserDept(user);
                /**删除角色信息**/
                userDao.deleteUserRole(user);
                /**删除用户*/
                int thisNum = userDao.deleteByPrimaryKey(id);
                num = num + thisNum;
            }
        }
        return num;
    }

    /**
     * 删除redis缓存
     */
    @CacheEvict(value = CACHE_NAME, key = "#user.getUserId()")
    public int delelte(User user) {

        return super.del(user);
    }

    @Cacheable(value = CACHE_NAME)
    public List<User> findAll() {
        return super.findAll();
    }

    /**
     * 查询全部
     */
    @Cacheable(value = CACHE_NAME)
    public PageInfo<User> queryByPage(Integer page, Integer rows, User user) {
        return super.queryByPage(page, rows, user);
    }

    /**
     * 查询用户对应的权限信息
     */
    public List<Menu> findUserRolePermission(String userId) {

        return menuDao.getUserMenu(userId);
    }

    /**
     * 查询用户对应的角色信息
     */
    public Set<String> findUserRole(String userId) {
        List<Role> roles = roleDao.userRoles(userId);
        Set<String> rolesSet = new HashSet<String>();
        List list = new ArrayList();
        if (roles != null) {
            for (Role role : roles) {
                rolesSet.addAll(Arrays.asList(role.getRole().trim().split(",")));
            }
        }
        return rolesSet;
    }

    public void deleteAllUserRole(String userId) {

    }

    public void grantUserRole(String userId, String[] roleList) {

    }

    public String getDbVersion() {

        return "";
    }

    /**
     * 判断用户是否存在
     */
    @Cacheable(value = CACHE_NAME, key = "#loginName")
    public boolean checkUserExists(String loginName) {
        boolean isExists = false;
        User user = getUser(loginName);
        if (user != null) {
            isExists = true;
        }
        return isExists;
    }

    /**
     * 判断用户是否存在
     */
    @Cacheable(value = CACHE_NAME, key = "#userName")
    public boolean checkUserExists2(String userName, String userName2) {
        boolean isExists = false;
        User user = getUser(userName);
        if (user != null) {
            isExists = true;
        }
        return isExists;
    }

    /**
     * 添加redis缓存
     */
    public void updatePassword(User user) {
        super.save(user);
    }
    /**获取不同的用户名称信息*/
    public List listUsers(){
        return userDao.listUsers();
    }
}
