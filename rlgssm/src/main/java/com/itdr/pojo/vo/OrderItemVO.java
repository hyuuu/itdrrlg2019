package com.itdr.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Class: OrderItemVO
 * version: JDK 1.8
 * create: 2019-09-23 17:01:30
 *
 * @author: Heyuu
 */
@Data
public class OrderItemVO {
    // 订单号
    private Long orderNo;
    // 商品ID
    private Integer productId;
    // 商品名称
    private String productName;
    // 商品图片
    private String productImage;
    // 时价
    private BigDecimal currentUnitPrice;
    // 数量
    private Integer quantity;
    // 总价
    private BigDecimal totalPrice;
    // 创建时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
