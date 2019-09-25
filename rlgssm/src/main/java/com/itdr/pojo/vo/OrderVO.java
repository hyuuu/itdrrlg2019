package com.itdr.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Class: OrderVO
 * version: JDK 1.8
 * create: 2019-09-23 17:01:13
 *
 * @author: Heyuu
 */
@Data
public class OrderVO {
    // 订单号
    private Long orderNo;
    // 付款金额
    private BigDecimal payment;
    // 付款方式
    private Integer paymentType;
    // 运费(单位：元)
    private Integer postage;
    // 状态
    private Integer status;
    // 付款时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;
    // 发货时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;
    // 交易完成时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    // 交易关闭时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeTime;
    // 订单创建时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    // 订单详细列表
    private List<OrderItemVO> orderItemVOList;
    // 收获地址ID
    private Integer shippingId;
    // 收获地址VO
    private ShippingVO shippingVO;

}
