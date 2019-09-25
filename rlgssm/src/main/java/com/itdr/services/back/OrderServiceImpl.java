package com.itdr.services.back;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.mappers.OrderMapper;
import com.itdr.pojo.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    /**
     * 订单List
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResCode list(Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageNum<0){
            return ResCode.error(Const.ProductEnum.PAGE_NUM_LESS_ZERO.getMsg());
        }
        if (pageSize != null && pageSize<0){
            return ResCode.error(Const.ProductEnum.PAGE_SIZE_LESS_ZERO.getMsg());
        }
        Integer pNum = Const.OrderEnum.PAGE_NUM.getCode();
        Integer pSize = Const.OrderEnum.PAGE_SIZE.getCode();
        if (pageNum != null && pageNum != Const.OrderEnum.PAGE_NUM.getCode()){
            pNum = pageNum;
        }
        if (pageSize != null && pageSize != Const.OrderEnum.PAGE_SIZE.getCode()){
            pSize = pageSize;
        }
        List<Order> list = orderMapper.selectByNumAndSize(pNum, pSize);
        if (list == null){
            return ResCode.error(Const.OrderEnum.SELECT_ERROR.getMsg());
        }
        return ResCode.success(list);
    }

    /**
     * 按订单号查询
     * @param orderNo
     * @return
     */
    @Override
    public ResCode search(Long orderNo) {
        if (orderNo == null){
           return ResCode.error(Const.OrderEnum.ORDERNO_NULL.getMsg());
        }
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            return ResCode.error(Const.OrderEnum.ORDER_NOT_EXIST.getMsg());
        }
        return ResCode.success(order);
    }

    /**
     * 订单发货
     * @param orderNo
     * @return
     */
    @Override
    public ResCode sendGoods(Long orderNo) {
        ResCode resCode = null;
        if (orderNo == null){
            return ResCode.error(Const.OrderEnum.ORDERNO_NULL.getMsg());
        }
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            return ResCode.error(Const.OrderEnum.ORDER_NOT_EXIST.getMsg());
        }
        if (order.getStatus() == Const.OrderEnum.STATUS_0.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_0.getMsg());
        }
        if (order.getStatus() == Const.OrderEnum.STATUS_10.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_10.getMsg());
        }
        if (order.getStatus() == Const.OrderEnum.STATUS_40.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_40.getMsg());
        }
        if (order.getStatus() == Const.OrderEnum.STATUS_50.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_50.getMsg());
        }
        if (order.getStatus() == Const.OrderEnum.STATUS_60.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_60.getMsg());
        }
        // 检查订单是否处在已付款状态
        if (order.getStatus() != 20){
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_ERROR.getMsg());
        }
        int i = orderMapper.updateStatusByOrderNo(orderNo,Const.OrderEnum.ORDER_STATUS_40.getCode());
        if ( i == 0){
            return ResCode.error(Const.OrderEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null,Const.OrderEnum.UPDATE_SUCCESS.getMsg());
    }
}
