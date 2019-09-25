package com.itdr.services.front;

import com.itdr.common.ResCode;

/**
 * Interface: FrontProductService
 * version: JDK 1.8
 * create: 2019-09-17 19:28:53
 *
 * @author: Heyuu
 */
public interface FrontProductService {
    /**
     * 产品搜索及动态排序List
     * @param categoryId 分类ID
     * @param keyword 关键字
     * @param pageNum 页码
     * @param pageSize 数量
     * @param orderBy 排序方式
     * @return ResCode
     */
    ResCode list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
    /**
     * 商品详情
     * @param productId 商品ID
     * @param is_new 1为最新，0为否
     * @param is_hot 1为最热，0为否
     * @param is_banner 1为轮播，0为否
     * @return ResCode
     */
    ResCode detail(Integer productId, Integer is_new, Integer is_hot, Integer is_banner);
    /**
     * 获取产品分类，一级子节点
     * @param pid 节点ID
     * @return ResCode
     */
    ResCode topCategory(Integer pid);
}
