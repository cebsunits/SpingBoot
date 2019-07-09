package com.tao.hai.dao;


import com.tao.hai.base.BaseDao;
import com.tao.hai.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao extends BaseDao<User> {

}
