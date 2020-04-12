package com.tao.hai.service.impl;

import com.github.pagehelper.PageInfo;
import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.base.TreeNode;
import com.tao.hai.bean.Menu;
import com.tao.hai.bean.User;
import com.tao.hai.dao.MenuDao;
import com.tao.hai.service.MenuService;
import com.tao.hai.util.BuildTree;
import com.tao.hai.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * Redis:
 * cacheNames 可以对一组进行通用配置，value可以不配置了@CacheConfig(cacheNames={userCache})
 * 参数value是缓存的名字，在执行的时候，会找叫这个名字的缓存使用/删除，注意如果使用，则必须在配置文件中进行配置
 * 参数key默认情况下是空串””,是Spring的一种表达式语言SpEL，我们这里可以随意指定，但是需要注意一定要加单引号
 * RedisConnectionFactory, StringRedisTemplate 和 RedisTemplate
 **/
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuDao, Menu> implements MenuService {
    /***缓存名称**/
    private static final String CACHE_NAME = "menuCache";
    @Autowired
    MenuDao menuDao;

    /**
     * 获取信息  第二次访问会取缓存
     */
    @Cacheable(value = CACHE_NAME, key = "#menu.menuId")
    public Menu getMenu(Menu menu) {
        Menu menuInfo = super.get(menu);
        return menuInfo;
    }

    /**
     * 获取信息  第二次访问会取缓存
     */
    public List<TreeNode<Menu>> listMenuTree(User user) {
        /**是否查询全部菜单*/
        boolean isMenuAll = user.isAdmin();
        /**查询全部菜单*/
        List<Menu> userMenuList = menuDao.getUserMenu(user.getUserId());
        /**组装树结构*/
        List<TreeNode<Menu>> trees = new ArrayList<TreeNode<Menu>>();
        for (Menu menu : userMenuList) {
            TreeNode<Menu> tree = new TreeNode<Menu>();
            tree.setId(menu.getMenuId());
            tree.setParentId(menu.getParentId());
            tree.setText(menu.getMenuName());
            tree.setHref(menu.getMenuUrl());
            tree.setIcon(menu.getMenuIcon());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        List<TreeNode<Menu>> list = BuildTree.buildList(trees, "0");
        return list;
    }

    /**
     * 获取信息  第二次访问会取缓存
     */
    public List<Menu> getRoleMenus(String roleId) {
        /**是否查询全部菜单*/
        List<Menu> roleMenuList = menuDao.getRoleMenu(roleId);
        return roleMenuList;
    }

    /**
     * 获取信息  第二次访问会取缓存
     */
    public List<TreeNode<Menu>> getRoleMenuTree(String roleId) {
        /**是否查询全部菜单*/
        List<Menu> roleMenuList = menuDao.getRoleMenu(roleId);
        /**组装树结构*/
        List<TreeNode<Menu>> trees = new ArrayList<TreeNode<Menu>>();
        for (Menu menu : roleMenuList) {
            TreeNode<Menu> tree = new TreeNode<Menu>();
            tree.setId(menu.getMenuId());
            tree.setParentId(menu.getParentId());
            tree.setText(menu.getMenuName());
            tree.setHref(menu.getMenuUrl());
            tree.setIcon(menu.getMenuIcon());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        List<TreeNode<Menu>> list = BuildTree.buildList(trees, "0");
        return list;
    }

    /**
     * 添加redis缓存
     */
    @CachePut(value = CACHE_NAME)
    public void save(Menu menu) {
        if (StringUtils.isEmpty(menu.getMenuId())) {
            menu.setMenuId(UUID.randomUUID().toString());
            menu.setCreateDate(new Date());
            menu.setCreateId(ShiroUtils.getUserId());
        }
        menu.setUpdateDate(new Date());
        menu.setUpdateId(ShiroUtils.getUserId());
        super.save(menu);
    }

    /**
     * 删除redis缓存
     */
    @CacheEvict(value = CACHE_NAME)
    public void delelte(Menu menu) {
        super.del(menu);
    }

    @Cacheable(value = CACHE_NAME)
    public List<Menu> findAll() {
        return super.findAll();
    }

    /**
     * 查询全部
     */
    @Cacheable(value = CACHE_NAME)
    public PageInfo<Menu> queryByPage(Integer page, Integer rows, Menu menu) {
        return super.queryByPage(page, rows, menu);
    }

    /**
     * 获取用户的所有菜单信息
     */
    public Set<String> listPerms(String userId) {
        List<String> perms = menuDao.listUserPerms(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }
}
