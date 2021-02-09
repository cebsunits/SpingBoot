package com.tao.hai.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tao.hai.util.ExampleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BaseServiceImpl<M extends BaseDao<T>, T extends DataEntity<T>> implements BaseService<T> {
    @Autowired
    protected M mapper;

    /**
     * 保存或更新方法
     */
    public int save(T t) {
        if (t.isNewRecord()) {
            return create(t);
        } else {
            return update(t);
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
    public int create(T t) {
        return mapper.insertSelective(t);
    }

    @Override
    public int delete(String... ids) {
        int num = 0;
        for (String id : ids) {
            int thisNum = mapper.deleteByPrimaryKey(id);
            num = num + thisNum;
        }
        return num;
    }

    @Override
    public int update(T t) {
        return mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public int del(T t) {

        return mapper.delete(t);
    }

    @Override
    public PageInfo<T> queryByPage(Integer page, Integer rows, T t) {
        PageHelper.startPage(page, rows);
        List<T> list = mapper.select(t);
        PageInfo<T> pageInfoUser = new PageInfo<T>(list);
        return pageInfoUser;
    }

    @Override
    public PageInfo<T> getPageList(ParameterModelBean parameterModel) {
        PageInfo<T> pageInfo = null;
        if (parameterModel != null) {
            //获得超类的泛型参数的实际类型
            Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            Example example = ExampleUtil.getExample(clazz, parameterModel);
            if (parameterModel.getPage() != null && parameterModel.getRows() != null) {
                PageHelper.startPage(parameterModel.getPage(), parameterModel.getRows());
            }
            List<T> list = mapper.selectByExample(example);
            pageInfo = new PageInfo<T>(list);
        }
        return pageInfo;
    }

    /**
     * 查询所有数据
     */
    @Override
    public List<T> getList(ParameterModelBean parameterModel) {
        List<T> list;
        if (parameterModel != null) {
            //获得超类的泛型参数的实际类型
            Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            Example example = ExampleUtil.getExample(clazz, parameterModel);
            list = mapper.selectByExample(example);
        } else {
            list = new ArrayList<T>();
        }
        return list;
    }
}
