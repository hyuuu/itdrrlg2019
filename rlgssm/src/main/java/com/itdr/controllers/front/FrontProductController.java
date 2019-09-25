package com.itdr.controllers.front;

import com.itdr.common.ResCode;
import com.itdr.services.front.FrontProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Class: FronProductController
 * version: JDK 1.8
 * create: 2019-09-17 18:02:28
 *
 * @author: Heyuu
 */
@Controller
@ResponseBody
@RequestMapping("/product")
public class FrontProductController {
    @Resource
    private FrontProductService frontProductService;

    /**
     * 产品搜索及动态排序List
     * @param categoryId 分类ID
     * @param keyword 关键字
     * @param pageNum 页码
     * @param pageSize 数量
     * @param orderBy 排序方式
     * @return ResCode
     */
    @RequestMapping("/list.do")
    public ResCode list(Integer categoryId, String keyword,
                        @RequestParam(value = "pageNum",required = false,defaultValue = "0") Integer pageNum,
                        @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                        @RequestParam(value = "orderBy",required = false,defaultValue = "") String orderBy){
        return frontProductService.list(categoryId, keyword, pageNum, pageSize, orderBy);
    }
    /**
     * 商品详情
     * @param productId 商品ID
     * @param is_new 1为最新，0为否
     * @param is_hot 1为最热，0为否
     * @param is_banner 1为轮播，0为否
     * @return ResCode
     */
    @RequestMapping("/detail.do")
    public ResCode detail(Integer productId, Integer is_new , Integer is_hot , Integer is_banner){
        return frontProductService.detail(productId, is_new, is_hot, is_banner);
    }

    /**
     * 获取产品分类，一级子节点
     * @param pid 节点ID
     * @return ResCode
     */
    @RequestMapping("/topcategory.do")
    public ResCode topCategory(Integer pid){
        return frontProductService.topCategory(pid);
    }
    /**
     * 调用日志空接口
     * @return
     */
    @RequestMapping("/logempty.do")
    public ResCode logEmpty(){
        return ResCode.success(0,null,"调用成功！");
    }
}
