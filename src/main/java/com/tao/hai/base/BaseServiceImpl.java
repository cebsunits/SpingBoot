package com.tao.hai.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 */
public class BaseServiceImpl<M extends BaseDao<T>, T> implements BaseService<T> {
    @Autowired
    protected M mapper;

    /**
     * 保存或更新方法
     */
    public void save(T t) {
        T oldT = get(t);
        if (oldT != null) {
            update(t);
        } else {
            create(t);
        }
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> findList(T t) {
        return mapper.select(t);
    }

    @Override
    public T getByKey(T t) {
        return mapper.selectByPrimaryKey(t);
    }

    @Override
    public T get(T t) {
        return mapper.selectOne(t);
    }

    @Override
    public void create(T t) {
        mapper.insertSelective(t);
    }

    @Override
    public void delete(String... ids) {
        for (String id : ids) {
            mapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void update(T t) {
        mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public void del(T t) {
        mapper.delete(t);
    }

    @Override
    public PageInfo<T> queryByPage(Integer page, Integer rows, T t) {
        PageHelper.startPage(page, rows);
        List<T> list = mapper.select(t);
        PageInfo<T> pageInfoUser = new PageInfo<T>(list);
        return pageInfoUser;
    }

}
