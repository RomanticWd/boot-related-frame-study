package org.liu.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.liu.demo.bean.User;

@Mapper
public interface UserDao {

    /**
     * 通过名字查询用户信息
     */
    User findUserByName(String name);

}
