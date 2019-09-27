package com.itdr.mappers;

import com.itdr.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    // 分页查询订单表
    List<Order> selectByNumAndSize(@Param("num") Integer num, @Param("size") Integer size);

    // 根据订单号查询一个订单信息
    Order selectByOrderNo(Long orderNo);

    // 根据订单号修改订单状态
    int updateStatusByOrderNo(@Param("orderNo") Long orderNo, @Param("status") Integer status);

    // 根据用户ID查询订单列表
    List<Order> selectByUserId(Integer userId);
    // 根据订单号和用户ID查询订单列表
    Order selectByOrderNoAndUserID(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);

    // 根据用户ID(userId)和订单号(orderNo)修改订单状态(status)
    int updateStatusByOrderNoAndUserId(@Param("status") Integer status, @Param("orderNo") Long orderNo, @Param("userId") Integer userId);
}