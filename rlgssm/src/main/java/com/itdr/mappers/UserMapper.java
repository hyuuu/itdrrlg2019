package com.itdr.mappers;

import com.itdr.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    // 插入一个用户
    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    /* 根据ID更新email phone question answer */
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    // 根据用户名和密码查找一个用户
    User selectByUsernameAndPassword(@Param("username")String username, @Param("password") String password);

    // 根据用户名修改密码
    Integer updateByUsernameAndPassword(@Param("username")String username, @Param("password")String password);

    List<User> selectAll();

    // 根据用户名或邮箱查找一个用户所有信息
    User selectByUsernameOrEmail(@Param("str") String str, @Param("type") String type);

    Integer updateStatusByUsername(@Param("username")String username, @Param("status")Integer status);
}