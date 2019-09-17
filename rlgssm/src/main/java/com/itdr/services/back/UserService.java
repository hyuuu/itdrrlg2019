package com.itdr.services.back;

import com.itdr.common.ResCode;

/**
 * Interface: UserService
 * version: JDK 1.8
 * create: 2019-09-05 20:28:41
 *
 * @author: Heyuu
 */
public interface UserService {
    // 用户列表
    ResCode userList(Integer pageNum, Integer pageSize);
    // 后台管理员登录
    ResCode login(String username, String password);
    // 禁用会员账户
    ResCode disableUser(Integer id);
}
