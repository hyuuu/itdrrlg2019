package com.itdr.services;

import com.itdr.common.ResCode;

/**
 * Interface: OrderService
 * version: JDK 1.8
 * create: 2019-09-09 16:57:47
 *
 * @author: Heyuu
 */
public interface OrderService {
    ResCode list(Integer pageNum, Integer pageSize);

    ResCode search(Long orderNo);

    ResCode sendGoods(Long orderNo);
}
