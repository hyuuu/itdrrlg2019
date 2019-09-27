package com.itdr.controllers.front;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.pojo.User;
import com.itdr.services.front.FrontOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * 获取订单详情：
     * 1：尚未产生订单号之前：根据用户ID在购物车(Cart)中取出选中的商品信息
     * 2：已经产生订单号之后：去订单详细(OrderItem)查
     * @param orderNo Long
     * @param session HttpSession
     * @return ResCode
     */
    @RequestMapping("get_order_cart_product.do")
    public ResCode getOrderCartProduct(@RequestParam(value = "orderNo", required = false) Long orderNo, HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontOrderService.getOrderCartProduct(user.getId(), orderNo);
    }

    /**
     * 获取订单列表
     * @param pageNum Integer
     * @param pageSize Integer
     * @param session HttpSession
     * @return ResCode
     */
    @RequestMapping("list.do")
    public ResCode orderList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontOrderService.orderList(user.getId(), pageNum, pageSize);
    }

    /**
     * 取消订单：只能取消 未付款 状态下的订单
     * @param orderNo Long
     * @param session HttpSession
     * @return ResCode
     */
    @RequestMapping("cancel.do")
    public ResCode cancelOrder(Long orderNo, HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontOrderService.cancelOrder(user.getId(), orderNo);
    }

    /**
     * 删除订单：只能删除 交易关闭和已取消 状态下的订单
     * @param orderNo Long
     * @param session HttpSession
     * @return ResCode
     */
    @RequestMapping("delete.do")
    public ResCode deleteOrder(Long orderNo, HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontOrderService.deleteOrder(user.getId(), orderNo);
    }
}
