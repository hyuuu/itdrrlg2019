package com.itdr.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Class: CartProductVo 购物车里每一条商品的实体类
 * version: JDK 1.8
 * create: 2019-09-18 17:56:15
 *
 * @author: Heyuu
 */
@Data
public class CartProductVO {
    // 购物车ID
    private Integer id;
    // 用户ID
    private Integer userId;
    // 商品ID
    private Integer productId;
    // 商品数量
    private Integer quantity;
    // 商品名称
    private String productName;
    // 商品副标题
    private String productSubtitle;
    // 产品主图，url相对地址
    private String productMainImage;
    // 价格，单位-元保留两位小数
    private BigDecimal productPrice;
    // 商品状态，1-在售 2-下架 3-删除
    private Integer productStatus;
    // 商品总价
    private BigDecimal productTotalPrice;
    // 库存
    private Integer productStock;
    // 是否被选中
    private Integer productChecked;
    // 商品数量是否超过库存的标记
    // 超过：LIMIT_NUM_FAIL
    // 否则：LIMIT_NUM_SUCCESS
    private String limitQuantity;
}
