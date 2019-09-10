package com.itdr.mappers;

import com.itdr.pojo.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectByNumAndSize(Integer num, Integer size);

    Order selectByOrderNo(Long orderNo);

    int sendGoods(Long orderNo);
}