package com.itdr.pojo.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Class: CartOrderProductVO
 * version: JDK 1.8
 * create: 2019-09-25 10:08:41
 *
 * @author: Heyuu
 */
public class CartOrderProductVO {
    private List<OrderItemVO> orderItemVOList;
    private String imageHost;
    private BigDecimal productTotalPrice;

    public List<OrderItemVO> getOrderItemVOList() {
        return orderItemVOList;
    }

    public void setOrderItemVOList(List<OrderItemVO> orderItemVOList) {
        this.orderItemVOList = orderItemVOList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}
