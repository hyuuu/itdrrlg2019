package com.itdr.controllers.back;

import com.itdr.common.ResCode;
import com.itdr.services.back.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Class: ProductController
 * version: JDK 1.8
 * create: 2019-09-09 11:25:21
 *
 * @author: Heyuu
 */
@Controller
@RequestMapping("/manage/product")
public class ProductController {
    @Resource
    private ProductService productService;

    @RequestMapping("/list.do")
    @ResponseBody
    public ResCode list(Integer pageNum, Integer pageSize){
        ResCode resCode = productService.list(pageNum, pageSize);
        return resCode;
    }

    @RequestMapping("/search.do")
    @ResponseBody
    public ResCode search(Integer productId, String productName){
        ResCode resCode = productService.search(productId, productName);
        return resCode;
    }
    @RequestMapping("/detail.do")
    @ResponseBody
    public ResCode detail(Integer productId){
        ResCode resCode = productService.selectById(productId);
        return resCode;
    }
    @RequestMapping("/set_sale_status.do")
    @ResponseBody
    public ResCode set_sale_status(Integer productId){
        ResCode resCode = productService.set_sale_status(productId);
        return resCode;
    }
}
