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
}
