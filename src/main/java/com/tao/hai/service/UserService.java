package com.tao.hai.service;

import com.github.pagehelper.PageInfo;
import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.bean.User;
import com.tao.hai.dao.UserDao;
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
public class UserService extends BaseServiceImpl<UserDao,User> {
    /***缓存名称**/
    private static final String CACHE_NAME = "userCache";
    @Autowired
    UserDao userDao;
    /**
     * 获取信息  第二次访问会取缓存
     */
    @Cacheable(value=CACHE_NAME)
    public User getUser(User user) {
        User userInfo = super.get(user);
        return userInfo;
    }

    /**
     * 添加redis缓存
     */
    @CachePut(value = CACHE_NAME, key = "#user.getUserId()")
    public void save(User user) {
        if(StringUtils.isEmpty(user.getUserId())){
            user.setUserId(UUID.randomUUID().toString());
        }
        super.save(user);
    }

    /**
     * 删除redis缓存
     */
    @CacheEvict(value = CACHE_NAME, key = "#user.getUserId()")
    public void delelte(User user) {
        super.del(user);
    }

    @Cacheable(value=CACHE_NAME)
    public List<User> findAll() {
        return super.findAll();
    }
    /**
     * 查询全部
     */
    @Cacheable(value=CACHE_NAME)
    public PageInfo<User> queryByPage(Integer page, Integer rows,User user) {
        return super.queryByPage(page, rows,user);
    }
}
