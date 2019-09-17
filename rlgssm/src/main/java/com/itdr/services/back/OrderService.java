package com.itdr.services.back;

import com.itdr.common.ResCode;

/**
 * Interface: OrderService
 * version: JDK 1.8
 * create: 2019-09-09 16:57:47
 *
 * @author: Heyuu
 */
public interface OrderService {
    // 订单List
    ResCode list(Integer pageNum, Integer pageSize);
    // 按订单号查询
    ResCode search(Long orderNo);
    // 订单发货
    ResCode sendGoods(Long orderNo);
}
