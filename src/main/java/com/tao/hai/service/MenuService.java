package com.tao.hai.service;

import com.github.pagehelper.PageInfo;
import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.bean.Menu;
import com.tao.hai.dao.MenuDao;
import com.tao.hai.util.menu.MenuTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;


/**
 * Redis:
 * cacheNames 可以对一组进行通用配置，value可以不配置了@CacheConfig(cacheNames={userCache})
 * 参数value是缓存的名字，在执行的时候，会找叫这个名字的缓存使用/删除，注意如果使用，则必须在配置文件中进行配置
 * 参数key默认情况下是空串””,是Spring的一种表达式语言SpEL，我们这里可以随意指定，但是需要注意一定要加单引号
 * RedisConnectionFactory, StringRedisTemplate 和 RedisTemplate
 **/
@Service
public class MenuService extends BaseServiceImpl<MenuDao, Menu> {
    /***缓存名称**/
    private static final String CACHE_NAME = "menuCache";
    @Autowired
    MenuDao menuDao;
    /**
     * 获取信息  第二次访问会取缓存
     */
    @Cacheable(value=CACHE_NAME)
    public Menu getMenu(Menu menu) {
        Menu menuInfo = super.get(menu);
        return menuInfo;
    }
    /**
     * 获取信息  第二次访问会取缓存
     */
    @Cacheable(value=CACHE_NAME,key="#userId")
    public List<Menu> getUserList(String userId){
        List<Menu> userMenuList= menuDao.getUserMenu(userId);
        MenuTreeUtil.treeList(userMenuList);
        return userMenuList;
    }
    /**
     * 添加redis缓存
     */
    @CachePut(value = CACHE_NAME, key = "#menu.getMenuId()")
    public void save(Menu menu) {
        if(StringUtils.isEmpty(menu.getMenuId())){
            menu.setMenuId(UUID.randomUUID().toString());
        }
        super.save(menu);
    }

    /**
     * 删除redis缓存
     */
    @CacheEvict(value = CACHE_NAME, key = "#menu.getMenuId()")
    public void delelte(Menu menu) {
        super.del(menu);
    }

    @Cacheable(value=CACHE_NAME)
    public List<Menu> findAll() {
        return super.findAll();
    }
    /**
     * 查询全部
     */
    @Cacheable(value=CACHE_NAME)
    public PageInfo<Menu> queryByPage(Integer page, Integer rows,Menu menu) {
        return super.queryByPage(page, rows,menu);
    }
}
