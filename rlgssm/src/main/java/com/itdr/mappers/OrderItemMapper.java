package com.itdr.mappers;

import com.itdr.pojo.Order;
import com.itdr.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    // 根据订单号查询订单详情
    List<OrderItem> selectByOrderNo(Long orderNo);

    // 批量插入
    int insertList(@Param("orderItemList") List<OrderItem> orderItemList);
}