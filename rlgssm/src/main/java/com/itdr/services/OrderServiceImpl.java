package com.itdr.services;

import com.itdr.common.ResCode;
import com.itdr.mappers.OrderMapper;
import com.itdr.pojo.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: OrderServiceImpl
 * version: JDK 1.8
 * create: 2019-09-09 16:58:56
 *
 * @author: Heyuu
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Override
    public ResCode list(Integer pageNum, Integer pageSize) {
        ResCode resCode = null;
        Integer pNum = 0;
        Integer pSize = 10;
        if (pageNum != null && pageNum != 0){
            pNum = pageNum;
        }
        if (pageSize != null && pageSize != 10 ){
            pSize = pageSize;
        }
        List<Order> list = orderMapper.selectByNumAndSize(pNum, pSize);
        if (list == null){
            resCode = ResCode.error(1,"查询失败了");
            return resCode;
        }
        resCode = ResCode.success(0,list);
        return resCode;
    }

    @Override
    public ResCode search(Long orderNo) {
        ResCode resCode = null;
        if (orderNo == null){
            resCode = ResCode.error(1,"参数为空！");
            return resCode;
        }
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            resCode = ResCode.error(1,"无此订单号！");
            return resCode;
        }
        resCode = ResCode.success(0,order);
        return resCode;
    }

    @Override
    public ResCode sendGoods(Long orderNo) {
        ResCode resCode = null;
        if (orderNo == null){
            resCode = ResCode.error(1,"参数为空！");
            return resCode;
        }
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            resCode = ResCode.error(1,"无此订单号！");
            return resCode;
        }
        if (order.getStatus() == 40){
            resCode = ResCode.error(1,"订单已发货！");
            return resCode;
        }
        int i = orderMapper.sendGoods(orderNo);
        if ( i == 0){
            resCode = ResCode.error(1,"更新失败！");
            return resCode;
        }
        resCode = ResCode.success(0,"更新成功！");
        return resCode;
    }
}
