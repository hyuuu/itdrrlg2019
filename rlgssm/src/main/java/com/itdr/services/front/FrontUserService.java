package com.itdr.services.front;

import com.itdr.common.ResCode;
import com.itdr.pojo.User;

/**
 * Interface: UserService
 * version: JDK 1.8
 * create: 2019-09-10 18:50:16
 *
 * @author: Heyuu
 */
public interface FrontUserService {
    /* 用户登录 */
    ResCode login(String username, String password);
    /* 检查用户名或邮箱是否存在 */
    ResCode checkUserByUsernameOrEmail(String str, String type);
    /* 用户注册 */
    ResCode register(User user);
    /* 用户激活 */
    ResCode activateUser(String checkUsername, String vcode);
    /* 登录状态更新个人信息 */
    ResCode updateInformation(User user);
    /* 忘记密码 */
    ResCode forgetGetQuestion(String username);
    /* 忘记密码提交问题答案 */
    ResCode forgetCheckAnswer(String username, String question, String answer);
    /* 忘记密码重置密码 */
    ResCode forgetResetPassword(String username, String passwordNew, String forgetToken);

    ResCode resetPassword(User user, String passwordOld, String passwordNew);
}
