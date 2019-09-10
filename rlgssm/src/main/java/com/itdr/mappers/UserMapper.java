package com.itdr.mappers;

import com.itdr.pojo.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUsernameAndPassword(String username, String password);
    Integer updateByUsernameAndPassword(String username, String password);

    List<User> selectAll();
}