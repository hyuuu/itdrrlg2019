package com.itdr.services.front;

import com.itdr.common.ResCode;

/**
 * Interface: FrontOrderService
 * version: JDK 1.8
 * create: 2019-09-23 16:49:43
 *
 * @author: Heyuu
 */
public interface FrontOrderService {
    // 创建订单
    ResCode create(Integer userId, Integer shippingId);
    // 获取订单详情
    ResCode getOrderCartProduct(Integer userId, Long orderNo);
    // 获取订单列表
    ResCode orderList(Integer userId, Integer pageNum, Integer pageSize);
    // 取消订单
    ResCode cancelOrder(Integer id, Long orderNo);
    // 删除订单
    ResCode deleteOrder(Integer userId, Long orderNo);
}
