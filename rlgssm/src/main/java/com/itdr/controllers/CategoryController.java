package com.itdr.controllers;

import com.itdr.common.ResCode;
import com.itdr.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Class: CategoryController
 * version: JDK 1.8
 * create: 2019-09-09 19:13:50
 *
 * @author: Heyuu
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @RequestMapping("/get_deep_category.do")
    @ResponseBody
    public ResCode getDeepCategory(Integer categoryId){
        ResCode resCode = categoryService.getDeepCategory(categoryId);
        return resCode;
    }
}
