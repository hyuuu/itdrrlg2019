package com.itdr.controllers.front;

import com.itdr.common.ResCode;
import com.itdr.common.TokenCache;
import com.itdr.pojo.User;
import com.itdr.services.front.FrontUserService;
import com.itdr.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class: UserController
 * version: JDK 1.8
 * create: 2019-09-10 17:41:46
 *
 * @author: Heyuu
 */
@Controller
@RequestMapping("/user/")
@ResponseBody
public class FrontUserController {
    @Resource
    private FrontUserService frontUserService;

    /* 用户登录 */
    @PostMapping("login.do")
    public ResCode login(String username, String password, HttpSession session){
        ResCode resCode = frontUserService.login(username, password);
        if (!resCode.isSucess()){
            return resCode;
        }
        Map data = (Map) resCode.getData();
        session.setAttribute("user",data.get("user"));
        resCode.setData(data.get("loginResponse"));
        return resCode;
    }

    /* 用户注册 */
    @PostMapping("register.do")
    public ResCode register(User user, HttpSession session){
        return frontUserService.register(user);
    }

    /* 用户激活 */
    @RequestMapping("activate.do")
    public ResCode activateUser(String checkUsername, String vcode){
        return frontUserService.activateUser(checkUsername,vcode);
    }

    /* 检查用户名或邮箱是否存在 */
    @RequestMapping("checkUser.do")
    public ResCode checkUserByUsernameOrEmail(String str, String type){
        return frontUserService.checkUserByUsernameOrEmail(str, type);
    }

    /* 获取当前登录用户信息 */
    @RequestMapping("get_user_info.do")
    public ResCode getUserInfo(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return ResCode.error("用户未登录，无法获取当前用户信息！");
        }
        User resUser = new User();
        resUser.setId(user.getId());
        resUser.setUsername(user.getUsername());
        resUser.setEmail(user.getEmail());
        resUser.setPhone(user.getPhone());
        resUser.setCreateTime(user.getCreateTime());
        resUser.setUpdateTime(user.getUpdateTime());
        return ResCode.success(resUser);
    }

    /* 获取当前登录用户的详细信息 */
    @RequestMapping("get_inforamtion.do")
    public ResCode getInforamtion(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return ResCode.error(10,"用户未登录，无法获取当前用户信息！");
        }
        return ResCode.success(user);
    }

    /* 退出登录 */
    @RequestMapping("logout.do")
    public ResCode logout(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return ResCode.error("未登录！");
        }
        session.removeAttribute("user");
        return ResCode.success(0,null,"退出成功");
    }

    /* 登录状态更新个人信息 */
    @PostMapping("update_information.do")
    public ResCode updateInformation(User user, HttpSession session){
        User user1 = (User) session.getAttribute("user");
        if (user1 == null){
            return ResCode.error("未登录！");
        }
        user.setId(user1.getId());
        user.setUsername(user1.getUsername());
        return frontUserService.updateInformation(user);
    }

    /* 忘记密码 */
    @RequestMapping("forget_get_question.do")
    public ResCode forgetGetQuestion(String username){
        return frontUserService.forgetGetQuestion(username);
    }

    /* 忘记密码提交问题答案 */
    @PostMapping("forget_check_answer.do")
    public ResCode forgetCheckAnswer(String username, String question, String answer){
        return frontUserService.forgetCheckAnswer(username,question,answer);
    }

    /* 忘记密码重置密码 */
    @PostMapping("forget_reset_password.do")
    public ResCode forgetResetPassword(String username, String passwordNew, String forgetToken){
        return frontUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /* 登录中状态重置密码 */
    @PostMapping("reset_password.do")
    public ResCode resetPassword(String passwordOld, String passwordNew, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return ResCode.error("用户未登录，无法获取当前用户信息！");
        }
        ResCode resCode = frontUserService.resetPassword(user, passwordOld, passwordNew);
        // 修改密码成功，清除session
        if (resCode.isSucess()){
            session.removeAttribute("user");
        }
        return resCode;
    }
}
