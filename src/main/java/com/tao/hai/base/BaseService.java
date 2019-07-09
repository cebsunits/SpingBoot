package com.tao.hai.base;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BaseService<T> {
    /**
     * 保存或更新方法
     */
    void save(T t);

    /**
     * 查询所有
     */
    List<T> findAll();

    /**
     * 按条件查询所有
     */
    List<T> findList(T t);

    /**
     * 根据ID查询
     */
    T getByKey(T t);

    /**
     * 根据ID查询
     */
    T get(T t);

    /**
     * 添加
     */
    void create(T t);

    /**
     * 修改
     */
    void update(T t);

    /**
     * 根据id批量删除
     */
    void delete(String... ids);

    /**
     * 删除
     */
    void del(T t);

    /**
     * 查询分页数据
     */
    PageInfo<T> queryByPage(Integer page, Integer rows, T t);
}
