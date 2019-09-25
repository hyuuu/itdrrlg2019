package com.itdr.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Class: CartVO 购物车类
 * version: JDK 1.8
 * create: 2019-09-18 18:08:25
 *
 * @author: Heyuu
 */
@Data
public class CartVO {
    /**
     * 图片服务器地址
     */
    private String imageHost;
    /**
     * 商品详情
     */
    private List<CartProductVO> cartProductVoList;
    /**
     * 是否全部选中
     */
    private boolean allChecked;
    /**
     * 购物车所有商品总价
     */
    private BigDecimal cartTotalPrice;
}
