package com.itdr.controllers.back;

import com.itdr.common.ResCode;
import com.itdr.services.back.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Class: OrderController
 * version: JDK 1.8
 * create: 2019-09-09 16:56:54
 *
 * @author: Heyuu
 */
@Controller
@RequestMapping("/manage/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @RequestMapping("/list.do")
    @ResponseBody
    public ResCode list(Integer pageNum, Integer pageSize){
        ResCode resCode = orderService.list(pageNum, pageSize);
        return resCode;
    }

    @RequestMapping("/search.do")
    @ResponseBody
    public ResCode search(Long orderNo){
        ResCode resCode = orderService.search(orderNo);
        return resCode;
    }

    @RequestMapping("/send_goods.do")
    @ResponseBody
    public ResCode sendGoods(Long orderNo){
        ResCode resCode = orderService.sendGoods(orderNo);
        return resCode;
    }

}
