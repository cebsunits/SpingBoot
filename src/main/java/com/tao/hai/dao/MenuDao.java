package com.tao.hai.dao;


import com.tao.hai.base.BaseDao;
import com.tao.hai.bean.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuDao extends BaseDao<Menu> {
    /**获取用户对应的菜单信息*/
    List<Menu> getUserMenu(String userId);
}
