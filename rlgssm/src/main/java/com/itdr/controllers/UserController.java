package com.itdr.controllers;

import com.itdr.common.ResCode;
import com.itdr.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Class: UserController
 * version: JDK 1.8
 * create: 2019-09-05 20:18:51
 *
 * @author: Heyuu
 */

@Controller
@RequestMapping("/manage/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/test.do")
    @ResponseBody
    public String test(){
        return "UserController Test";
    }

    @RequestMapping("/disableuser.do")
    @ResponseBody
    public ResCode disableUser(Integer id){
        ResCode resCode = null;
        resCode = ResCode.error(1,"没写业务");
        return resCode;
    }

    @RequestMapping("/login.do")
    @ResponseBody
    public ResCode login(String username, String password, Model model){
        ResCode resCode = userService.login(username, password);
//        model.addAttribute("user",user);
        return resCode;
    }

    @RequestMapping("/changePassword.do")
    @ResponseBody
    public ResCode changePassword(String username, String password, String newPassword){
        ResCode resCode = userService.changePasswordByUsernameAndPassword(username, password, newPassword);
        return resCode;
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public ResCode userList(){
        ResCode resCode = userService.userList();
        return resCode;
    }
}
