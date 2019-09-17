package com.itdr.controllers.back;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.pojo.Category;
import com.itdr.pojo.User;
import com.itdr.services.back.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Class: CategoryController
 * version: JDK 1.8
 * create: 2019-09-09 19:13:50
 *
 * @author: Heyuu
 */
@Controller
@RequestMapping("/manage/category")
@ResponseBody
public class CategoryController {
    @Resource
    private CategoryService categoryService;

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
     * 获取品类子节点(平级)
     * @param categoryId
     * @param session
     * @return
     */
    @RequestMapping("/get_category.do")
    public ResCode getCategory(Integer categoryId, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return categoryService.getCategory(categoryId);
    }

    /**
     * 增加节点
     * @param category
     * @param session
     * @return
     */
    @RequestMapping("/add_category.do")
    public ResCode addCategory(Category category, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return categoryService.addCategory(category);
    }

    /**
     * 修改品类名字
     * @param categoryId
     * @param categoryName
     * @param session
     * @return
     */
    @RequestMapping("/set_category_name.do")
    public ResCode setCategoryName(Integer categoryId, String categoryName, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return categoryService.setCategoryName(categoryId, categoryName);
    }

    /**
     * 获取当前分类及递归子节点
     * @param categoryId
     * @return
     */
    @RequestMapping("/get_deep_category.do")
    public ResCode getDeepCategory(Integer categoryId, HttpSession session){
        ResCode resCode = checkManagerUserLogin(session);
        if (!resCode.isSucess()){
            return resCode;
        }
        return categoryService.getDeepCategory(categoryId);
    }
}
