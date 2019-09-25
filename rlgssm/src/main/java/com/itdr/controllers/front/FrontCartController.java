package com.itdr.controllers.front;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.pojo.User;
import com.itdr.services.front.FrontCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Class: FrontCartController
 * version: JDK 1.8
 * create: 2019-09-18 09:31:11
 *
 * @author: Heyuu
 */
@Controller
@ResponseBody
@RequestMapping("/cart")
public class FrontCartController {
    @Resource
    private FrontCartService frontCartService;
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
     * 获取当前用户的购物车信息列表
     * @param session session
     * @return ResCode
     */
    @RequestMapping("/list.do")
    public ResCode getCartVO(HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontCartService.getCartVO(user.getId());
    }

    /**
     * 购物车添加商品
     * @param productId 商品ID
     * @param count 数量
     * @param session session
     * @return ResCode
     */
    @RequestMapping("/add.do")
    public ResCode add(Integer productId, Integer count, HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontCartService.add(productId, count, user.getId());
    }

    /**
     * 更新购物车某个产品数量
     * @param productId 商品ID
     * @param count 数量
     * @param session session
     * @return ResCode
     */
    @RequestMapping("/update.do")
    public ResCode update(Integer productId, Integer count, HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontCartService.update(productId, count, user.getId());
    }

    /**
     * 移除购物车某个产品
     * @param productIds 商品IDs
     * @param session session
     * @return ResCode
     */
    @RequestMapping("/delete_product.do")
    public ResCode deleteProduct(String productIds, HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontCartService.deleteProduct(productIds, user.getId());
    }

    /**
     * 购物车选中某个商品
     * @param productId 商品ID
     * @param session session
     * @return ResCode
     */
    @RequestMapping("/select.do")
    public ResCode checkProduct(Integer productId, HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontCartService.checkProduct(productId, user.getId());
    }

    /**
     * 购物车取消选中某个商品
     * @param productId 商品ID
     * @param session session
     * @return ResCode
     */
    @RequestMapping("/un_select.do")
    public ResCode unCheckProduct(Integer productId, HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontCartService.unCheckProduct(productId, user.getId());
    }

    /**
     * 查询在购物车里的产品数量
     * @param session session
     * @return ResCode
     */
    @RequestMapping("/get_cart_product_count.do")
    public ResCode getCartProductCount(HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontCartService.getCartProductCount(user.getId());
    }

    /**
     * 购物车全选
     * @param session session
     * @return ResCode
     */
    @RequestMapping("/select_all.do")
    public ResCode checkAll(HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontCartService.checkAll(user.getId());
    }

    /**
     * 购物车取消全选
     * @param session session
     * @return ResCode
     */
    @RequestMapping("/un_select_all.do")
    public ResCode unCheckAll(HttpSession session){
        ResCode resCode = checkUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        User user = (User) resCode.getData();
        return frontCartService.unCheckAll(user.getId());
    }
}
