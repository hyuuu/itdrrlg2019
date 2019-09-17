package com.itdr.controllers.back;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.pojo.User;
import com.itdr.services.back.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Class: OrderController
 * version: JDK 1.8
 * create: 2019-09-09 16:56:54
 *
 * @author: Heyuu
 */
@Controller
@RequestMapping("/manage/order")
@ResponseBody
public class OrderController {
    @Resource
    private OrderService orderService;
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
     * 订单List
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping("/list.do")
    public ResCode list(Integer pageNum, Integer pageSize, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return orderService.list(pageNum, pageSize);
    }

    /**
     * 按订单号查询
     * @param orderNo
     * @param session
     * @return
     */
    @RequestMapping("/search.do")
    public ResCode search(Long orderNo, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return orderService.search(orderNo);
    }

    /**
     * 订单发货
     * @param orderNo
     * @param session
     * @return
     */
    @RequestMapping("/send_goods.do")
    public ResCode sendGoods(Long orderNo, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return orderService.sendGoods(orderNo);
    }

}
