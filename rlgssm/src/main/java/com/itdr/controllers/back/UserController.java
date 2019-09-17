package com.itdr.controllers.back;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.common.TokenCache;
import com.itdr.pojo.User;
import com.itdr.services.back.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Class: UserController
 * version: JDK 1.8
 * create: 2019-09-05 20:18:51
 *
 * @author: Heyuu
 */

@Controller
@RequestMapping("/manage/user")
@ResponseBody
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 检查管理员用户是否登录
     * @param session
     * @return
     */
    private ResCode checkManagerUserLogin(HttpSession session){
        User managerUser = (User) session.getAttribute(Const.MANAGE_USER_LOGIN_SESSION);
        if (managerUser == null){
            return ResCode.error(Const.UserEnum.USER_NOT_LOGIN.getMsg());
        }
        return ResCode.success(managerUser);
    }

    /**
     * 获取用户列表
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping("/list.do")
    public ResCode userList(Integer pageNum, Integer pageSize, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return userService.userList(pageNum,pageSize);
    }

    /**
     * 后台管理员登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login.do")
    public ResCode login(String username, String password, HttpSession session){
        ResCode resCode = userService.login(username, password);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) TokenCache.get(Const.TOKEN_PREFIX + Const.MANAGE_USER_LOGIN_SESSION);
        TokenCache.clearCache(Const.TOKEN_PREFIX + Const.MANAGE_USER_LOGIN_SESSION);
        session.setAttribute(Const.MANAGE_USER_LOGIN_SESSION, user);
        return resCode;
    }

    /**
     * 禁用会员账户
     * @param uid
     * @param session
     * @return
     */
    @RequestMapping("/disableuser.do")
    public ResCode disableUser(Integer uid, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
//        当前登录用户信息写入TokenCache，以便业务层在比较用户角色的时候拿出来使用
//        User managerUser = (User) resCode.getData();
//        TokenCache.set(Const.TOKEN_PREFIX+"disableUser", managerUser);
        return userService.disableUser(uid);
    }
}
