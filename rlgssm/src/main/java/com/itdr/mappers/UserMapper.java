package com.itdr.mappers;

import com.itdr.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    // 插入一个用户
    int insert(User record);

    int insertSelective(User record);
    // 根据ID查询一个用户
    User selectByPrimaryKey(Integer id);

    /* 根据ID更新email phone question answer */
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    // 根据用户名和密码查找一个用户
    User selectByUsernameAndPassword(@Param("username")String username, @Param("password") String password);

    // 根据用户名修改密码
    Integer updateByUsernameAndPassword(@Param("username")String username, @Param("password")String password);

    // 分页查询用户列表
    List<User> selectAll(@Param("num")int num, @Param("size")int size);

    // 根据用户名或邮箱查找一个用户所有信息
    User selectByUsernameOrEmail(@Param("str") String str, @Param("type") String type);
    // 根据用户名更新用户状态
    Integer updateStatusByUsername(@Param("username")String username, @Param("status")Integer status);
    // 根据ID更新用户状态
    Integer updateStatusById(@Param("id")Integer id, @Param("status")Integer status);
}