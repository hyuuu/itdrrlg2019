package com.itdr.controllers.front;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.pojo.User;
import com.itdr.services.front.FrontOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Class: FrontOrderController
 * version: JDK 1.8
 * create: 2019-09-23 16:48:55
 *
 * @author: Heyuu
 */
@Controller
@ResponseBody
@RequestMapping("/order/")
public class FrontOrderController {
    @Resource
    private FrontOrderService frontOrderService;

    /**
     * 检查用户是否登录
     * @param session session
     * @return ResCode
     */
    private ResCode checkUserLogin(HttpSession session){
        User user = (User) session.getAttribute(Const.USER_LOGIN_SESSION);
        if (user == null){
            return ResCode.error(Const.UserEnum.USER_NOT_LOGIN.getMsg());
        }
        return ResCode.success(user);
    }

    /**
     * 创建订单
     * @param shippingId Integer
     * @param session HttpSession
     * @return ResCode
     */
    @RequestMapping("create.do")
    public ResCode create(Integer shippingId, HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontOrderService.create(user.getId(), shippingId);
    }
}
