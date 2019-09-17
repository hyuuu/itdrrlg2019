package com.itdr.controllers.back;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.pojo.Product;
import com.itdr.pojo.User;
import com.itdr.services.back.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Class: ProductController
 * version: JDK 1.8
 * create: 2019-09-09 11:25:21
 *
 * @author: Heyuu
 */
@Controller
@RequestMapping("/manage/product")
@ResponseBody
public class ProductController {
    @Resource
    private ProductService productService;

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
     * 产品list
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
        return productService.list(pageNum, pageSize);
    }

    /**
     * 产品搜索
     * @param productId
     * @param productName
     * @param session
     * @return
     */
    @RequestMapping("/search.do")
    public ResCode search(Integer productId, String productName, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return productService.search(productId, productName);
    }

    /**
     * 产品详情
     * @param productId
     * @param session
     * @return
     */
    @RequestMapping("/detail.do")
    public ResCode detail(Integer productId, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return productService.detail(productId);
    }

    /**
     * 产品上下架
     * @param productId
     * @param session
     * @return
     */
    @RequestMapping("/set_sale_status.do")
    public ResCode setSaleStatus(Integer productId, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return productService.setSaleStatus(productId);
    }

    /**
     * 新增OR更新产品
     * @param product
     * @param session
     * @return
     */
    @RequestMapping("/save.do")
    public ResCode setSaleStatus(Product product, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return productService.save(product);
    }
}
