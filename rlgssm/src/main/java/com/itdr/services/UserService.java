package com.itdr.services;

import com.itdr.common.ResCode;

/**
 * Interface: UserService
 * version: JDK 1.8
 * create: 2019-09-05 20:28:41
 *
 * @author: Heyuu
 */
public interface UserService {
    ResCode login(String username, String password);
    ResCode changePasswordByUsernameAndPassword(String username, String password, String newPassword);

    ResCode userList();
}
