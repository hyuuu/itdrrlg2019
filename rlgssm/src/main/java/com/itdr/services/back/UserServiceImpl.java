package com.itdr.services.back;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.common.TokenCache;
import com.itdr.mappers.UserMapper;
import com.itdr.pojo.User;
import com.itdr.utils.MD5Util;
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


    /**
     * 获取用户列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResCode userList(Integer pageNum, Integer pageSize) {
        int num = 0;
        int size = 10;
        if (pageNum != null && pageNum != 0){
            num = pageNum;
        }
        if (pageSize != null && pageSize != 10 ){
            size = pageSize;
        }
        List<User> userList = userMapper.selectAll(num, size);
        if (userList == null){
            return ResCode.error(Const.UserEnum.SELECT_ERROR.getMsg());
        }
        return ResCode.success(userList);
    }

    /**
     * 后台管理员登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResCode login(String username, String password) {
        if (username == null || "".equals(username)){
            return ResCode.error(Const.UserEnum.USERNAME_NULL.getMsg());
        }
        if (password == null || "".equals(password)){
            return ResCode.error(Const.UserEnum.PASSWORD_NULL.getMsg());
        }
        String md5Password = MD5Util.getMD5Code(password);
        User user = userMapper.selectByUsernameAndPassword(username,md5Password);
        if (user == null){
            return ResCode.error(Const.UserEnum.PASSWORD_ERROR.getMsg());
        }
        if (user.getRole() != Const.UserEnum.ROLE_ADMIN.getCode()){
            return ResCode.error(Const.UserEnum.PERMISSION_DENIED.getMsg());
        }
        // user存入session，loginResponse作为返回对象
        TokenCache.set(Const.TOKEN_PREFIX + Const.MANAGE_USER_LOGIN_SESSION, user);
        User loginResponse = new User();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setCreateTime(user.getCreateTime());
        loginResponse.setUpdateTime(user.getUpdateTime());
        return ResCode.success(loginResponse);
    }

    /**
     * 禁用会员账户
     * @param id
     * @return
     */
    @Override
    public ResCode disableUser(Integer id) {
        if (id == null){
            return ResCode.error(Const.UserEnum.PARAMETER_NULL.getMsg());
        }
        if (id < 0){
            return ResCode.error(Const.UserEnum.PARAMETER_LESS_ZERO.getMsg());
        }
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null){
            return ResCode.error(Const.UserEnum.USER_NOT_EXIST.getMsg());
        }
        // 如果被禁用的对象不是普通用户对象，则没有权限禁用该对象
        if (user.getRole() != Const.UserEnum.ROLE_COMMON.getCode()){
            return ResCode.error(Const.UserEnum.PERMISSION_DENIED.getMsg());
        }
        Integer row = null;
        // 启用变禁用
        if (user.getStatus() == Const.UserEnum.STATUS_NORMAL.getCode()){
            row = userMapper.updateStatusById(id, Const.UserEnum.STATUS_DISABLE.getCode());
        }
        // 禁用变启用
        if (user.getStatus() == Const.UserEnum.STATUS_DISABLE.getCode()){
            row = userMapper.updateStatusById(id, Const.UserEnum.STATUS_NORMAL.getCode());
        }
        if (row == null || row <= 0){
            return ResCode.error(Const.UserEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null,Const.UserEnum.UPDATE_SUCCESS.getMsg());
    }
}
