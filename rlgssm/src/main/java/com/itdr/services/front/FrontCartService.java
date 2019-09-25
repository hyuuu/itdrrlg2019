package com.itdr.services.front;

import com.itdr.common.ResCode;

/**
 * Interface: FronCartService
 * version: JDK 1.8
 * create: 2019-09-18 09:35:40
 *
 * @author: Heyuu
 */
public interface FrontCartService {
    /**
     * 获取当前用户的购物车
     * @param userId 用户ID
     * @return ResCode
     */
    ResCode getCartVO(Integer userId);

    /**
     * 购物车添加商品
     * @param productId 商品ID
     * @param count 数量
     * @param userId 用户ID
     * @return ResCode
     */
    ResCode add(Integer productId, Integer count, Integer userId);

    /**
     * 更新购物车某个产品数量
     * @param productId 商品ID
     * @param count 数量
     * @param userId 用户ID
     * @return ResCode
     */
    ResCode update(Integer productId, Integer count, Integer userId);

    /**
     * 移除购物车某个产品
     * @param productIds 商品IDs
     * @param userId 用户ID
     * @return ResCode
     */
    ResCode deleteProduct(String productIds, Integer userId);

    /**
     * 购物车选中某个商品
     * @param productId 商品ID
     * @param userId 用户ID
     * @return ResCode
     */
    ResCode checkProduct(Integer productId, Integer userId);

    /**
     * 购物车取消选中某个商品
     * @param productId 商品ID
     * @param userId 用户ID
     * @return ResCode
     */
    ResCode unCheckProduct(Integer productId, Integer userId);

    /**
     * 查询在购物车里的产品数量
     * @param userId 用户ID
     * @return ResCode
     */
    ResCode getCartProductCount(Integer userId);

    /**
     * 购物车全选
     * @param userId 用户ID
     * @return ResCode
     */
    ResCode checkAll(Integer userId);

    /**
     * 购物车取消全选
     * @param userId 用户ID
     * @return ResCode
     */
    ResCode unCheckAll(Integer userId);
}
