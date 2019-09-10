package com.itdr.services;

import com.itdr.common.ResCode;
import com.itdr.mappers.UserMapper;
import com.itdr.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Class: UserServiceImpl
 * version: JDK 1.8
 * create: 2019-09-05 20:32:44
 *
 * @author: Heyuu
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public ResCode login(String username, String password) {
        ResCode resCode = null;
        if (username == null || username.equals("") || password == null || password.equals("")){
            resCode = ResCode.error(1,"用户名和密码不能为空");
            return resCode;
        }
        User user = userMapper.selectByUsernameAndPassword(username, password);
        if (user == null){
            resCode = ResCode.error(1,"密码错误");
            return resCode;
        }
        resCode = ResCode.success(0,user);
        return resCode;
    }

    @Override
    public ResCode changePasswordByUsernameAndPassword(String username, String password, String newPassword) {
        ResCode resCode = null;
        User user = userMapper.selectByUsernameAndPassword(username, password);
        if (user == null){
            resCode = ResCode.error(1,"用户不存在");
            return resCode;
        }
        Integer row = userMapper.updateByUsernameAndPassword(username, newPassword);
        if (row == 0){
            resCode = ResCode.error(1,"更新失败");
            return resCode;
        }

/*
        // 测试事务
        int i = 1/0;
*/
        resCode = ResCode.success(0,"更新成功");
        return resCode;
    }

    @Override
    public ResCode userList() {
        ResCode resCode = null;
        List<User> userList = userMapper.selectAll();
        if (userList == null){
            resCode = ResCode.error(1,"异常出现了");
        }
        resCode = ResCode.success(0,userList);
        return resCode;
    }
}
