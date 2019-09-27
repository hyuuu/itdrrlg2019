package com.itdr.services.front;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.mappers.*;
import com.itdr.pojo.*;
import com.itdr.pojo.vo.CartOrderProductVO;
import com.itdr.pojo.vo.OrderItemVO;
import com.itdr.pojo.vo.OrderVO;
import com.itdr.pojo.vo.ShippingVO;
import com.itdr.utils.BigDecimalUtil;
import com.itdr.utils.PoToVoUtil;
import com.itdr.utils.Tools;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class: FrontOrderServiceImpl
 * version: JDK 1.8
 * create: 2019-09-23 16:50:02
 *
 * @author: Heyuu
 */
@Service
@Transactional
public class FrontOrderServiceImpl implements FrontOrderService {

    @Resource
    private ShippingMapper shippingMapper;
    @Resource
    private CartMapper cartMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;

    // 创建一个新订单
    private Order createOrder(Integer userId, Integer shippingId, BigDecimal payment){
        Order order = new Order();
        order.setUserId(userId);
        // 订单号 = 时间戳 + 随机三位数
        order.setOrderNo(Long.parseLong(System.currentTimeMillis()+Tools.get3Code()));
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(Const.PayEnum.PLATFORM_1.getCode());
        order.setPostage(Const.PayEnum.POSTAGE_0.getCode());
        order.setStatus(Const.OrderEnum.STATUS_10.getCode());
        return order;
    }
    // 创建一组新订单详细列表
    private List<OrderItem> createOrderItemList(List<Product> productList, Order order, List<Cart> cartList){
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Product product : productList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(order.getUserId());
            orderItem.setOrderNo(order.getOrderNo());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            // 时价
            orderItem.setCurrentUnitPrice(product.getPrice());
            for (Cart cart : cartList) {
                if (product.getId().equals(cart.getProductId())){
                    // 当前商品ID的购买数量
                    orderItem.setQuantity(cart.getQuantity());
                    // 金额 = 购买数量 * 时价
                    BigDecimal mul = BigDecimalUtil.mul(cart.getQuantity().doubleValue(), orderItem.getCurrentUnitPrice().doubleValue());
                    orderItem.setTotalPrice(mul);
                }
            }
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }
    // List<OrderItem> To List<OrderItemVO>
    private List<OrderItemVO> getOrderItemVOList(List<OrderItem> orderItemList){
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO = PoToVoUtil.OrderItemToOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        return orderItemVOList;
    }

    // 创建一个新订单(主)
    @Override
    public ResCode create(Integer userId, Integer shippingId) {
        // 非空判断
        if (shippingId == null){
            return ResCode.error(Const.ShippingEnum.ID_NULL.getMsg());
        }
        // 检查收货地址
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if (shipping == null){
            return ResCode.error(Const.ShippingEnum.SHIPPING_NOT_EXIST.getMsg());
        }
        if (!shipping.getUserId().equals(userId)){
            return ResCode.error(Const.ShippingEnum.USER_NOT_SAME.getMsg());
        }
        // 获取用户选中的购物车商品
        List<Cart> cartList = cartMapper.selectCheck(userId, Const.CartEnum.PRODUCT_CHECKED.getCode());
        if (cartList.size() == 0){
            return ResCode.error(Const.CartEnum.CHECKED_NULL.getMsg());
        }
        // 遍历商品ID查询商品信息
        List<Product> productList = new ArrayList<>();
        // 存放购物车中的商品ID，下边用得到
        List<Integer> productIDs = new ArrayList<>();
        for (Cart cart : cartList) {
            productIDs.add(cart.getProductId());
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product == null){
                return ResCode.error("ID："+cart.getProductId()+Const.ProductEnum.PRODUCT_NOT_EXIST.getMsg());
            }
            if (!product.getStatus().equals(Const.ProductEnum.PRODUCT_NOT_EXIST_2.getCode())){
                return ResCode.error("ID："+cart.getProductId()+Const.ProductEnum.PRODUCT_NOT_EXIST_2.getMsg());
            }
            productList.add(product);
        }
        // 判断库存与购物数量的关系，计算订单总价
        BigDecimal payment = new BigDecimal("0");
        for (Cart cart : cartList) {
            for (Product product : productList) {
                if (cart.getProductId().equals(product.getId())){
                    // 如果购买数量大于库存
                    if (cart.getQuantity() > product.getStock()){
                        return ResCode.error(product.getName()+Const.ProductEnum.STOCK_INSUFFICIENT.getMsg());
                    }else {
                        // 购物车中需要购买的每一条商品的 总价 = 数量 * 价格
                        BigDecimal mul = BigDecimalUtil.mul(cart.getQuantity().doubleValue(), product.getPrice().doubleValue());
                        // 需要购买的视频的总金额
                        payment = BigDecimalUtil.add(payment.doubleValue(), mul.doubleValue());
                    }
                }
            }
        }
        // 生成订单
        Order order = this.createOrder(userId, shippingId, payment);
        // 保存订单
        int orderInsert = orderMapper.insert(order);
        if (orderInsert <= 0){
            // 失败
            return ResCode.error(Const.PayEnum.ORDER_CREATE_ERROR.getMsg());
        }
        // 生成订单详细
        List<OrderItem> orderItemList = this.createOrderItemList(productList, order, cartList);
        int orderItemListInsert = orderItemMapper.insertList(orderItemList);
        if (orderItemListInsert <= 0){
            // 失败
            return ResCode.error(Const.PayEnum.ORDER_CREATE_ERROR.getMsg());
        }
        // 删除购物车
        int cartListDelete = cartMapper.deleteList(productIDs, userId);
        if (cartListDelete <= 0){
            // 删除购物车失败咋整
            return ResCode.error(Const.CartEnum.UPDATE_ERROR.getMsg()+"=="+Const.PayEnum.ORDER_CREATE_ERROR.getMsg());
        }
        // 库存等到支付成功之后再更新？（已经在回调函数做了更新操作）

        // 返回数据
        ShippingVO shippingVO = PoToVoUtil.ShippingToShippingVO(shipping);
        List<OrderItemVO> orderItemVOList = getOrderItemVOList(orderItemList);
        OrderVO orderVO = PoToVoUtil.getOrderVO(order, orderItemVOList, shippingVO);

        return ResCode.success(orderVO);
    }

    // 获取订单详情
    @Override
    public ResCode getOrderCartProduct(Integer userId, Long orderNo) {
        CartOrderProductVO cartOrderProductVO = new CartOrderProductVO();
        BigDecimal productTotalPrice = new BigDecimal("0");
        String imageHost = Const.imageHost;
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        List<OrderItem> orderItemList = new ArrayList<>();
        if (orderNo == null){
            // 订单没产生，OrderItem表无数据
            // 获取用户选中的购物车商品
            List<Cart> cartList = cartMapper.selectCheck(userId, Const.CartEnum.PRODUCT_CHECKED.getCode());
            if (cartList.size() == 0){
                return ResCode.error(Const.CartEnum.CHECKED_NULL.getMsg());
            }
            // 遍历商品ID查询商品信息
            List<Product> productList = new ArrayList<>();
            for (Cart cart : cartList) {
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product == null){
                    return ResCode.error("ID："+cart.getProductId()+Const.ProductEnum.PRODUCT_NOT_EXIST.getMsg());
                }
                if (!product.getStatus().equals(Const.ProductEnum.PRODUCT_NOT_EXIST_2.getCode())){
                    return ResCode.error("ID："+cart.getProductId()+Const.ProductEnum.PRODUCT_NOT_EXIST_2.getMsg());
                }
                productList.add(product);
            }
            Order order = new Order();
            order.setUserId(userId);
            orderItemList = this.createOrderItemList(productList, order, cartList);
        }else {
            // 订单已产生，OrderItem表有数据
            orderItemList = orderItemMapper.selectByOrderNoAndUserID(orderNo, userId);
            if (orderItemList.size() == 0){
                return ResCode.error(Const.OrderEnum.ORDER_NOT_EXIST.getMsg());
            }
        }
        orderItemVOList = getOrderItemVOList(orderItemList);
        for (OrderItemVO orderItemVO : orderItemVOList) {
            productTotalPrice = BigDecimalUtil.add(productTotalPrice.doubleValue(), orderItemVO.getTotalPrice().doubleValue());
        }
        cartOrderProductVO.setImageHost(imageHost);
        cartOrderProductVO.setProductTotalPrice(productTotalPrice);
        cartOrderProductVO.setOrderItemVOList(orderItemVOList);
        return ResCode.success(cartOrderProductVO);
    }

    // 获取订单列表
    @Override
    public ResCode orderList(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoAndUserID(order.getOrderNo(), userId);
            List<OrderItemVO> orderItemVOList = this.getOrderItemVOList(orderItemList);
            Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
            ShippingVO shippingVO = PoToVoUtil.ShippingToShippingVO(shipping);
            OrderVO orderVO = PoToVoUtil.getOrderVO(order, orderItemVOList, shippingVO);
            orderVOList.add(orderVO);
        }
        // 先分页，再装数据
        // 要把开启分页的原始数据放到PageInfo才会执行分页操作，然后再通过set方法把最终的数据装入PageInfo
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return ResCode.success(pageInfo);
    }

    // 取消订单
    @Override
    public ResCode cancelOrder(Integer userId, Long orderNo) {
        if (orderNo == null){
            return ResCode.error(Const.OrderEnum.ORDERNO_NULL.getMsg());
        }
        Order order = orderMapper.selectByOrderNoAndUserID(orderNo, userId);
        if (order == null){
            return ResCode.error(Const.OrderEnum.ORDER_NOT_EXIST.getMsg());
        }
        if (order.getStatus() < Const.OrderEnum.STATUS_10.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_CANCEL_0.getMsg());
        }
        if (order.getStatus() > Const.OrderEnum.STATUS_50.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_CANCEL_60.getMsg());
        }
        if (order.getStatus() > Const.OrderEnum.STATUS_40.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_CANCEL_50.getMsg());
        }
        if (order.getStatus() > Const.OrderEnum.STATUS_20.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_CANCEL_40.getMsg());
        }
        if (order.getStatus() > Const.OrderEnum.STATUS_10.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_CANCEL_20.getMsg());
        }
        int update = orderMapper.updateStatusByOrderNoAndUserId(Const.OrderEnum.STATUS_0.getCode(), orderNo, userId);
        if (update <= 0){
            return ResCode.error(Const.OrderEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null, Const.OrderEnum.UPDATE_SUCCESS.getMsg());
    }

    // 删除订单
    @Override
    public ResCode deleteOrder(Integer userId, Long orderNo) {
        if (orderNo == null){
            return ResCode.error(Const.OrderEnum.ORDERNO_NULL.getMsg());
        }
        Order order = orderMapper.selectByOrderNoAndUserID(orderNo, userId);
        if (order == null){
            return ResCode.error(Const.OrderEnum.ORDER_NOT_EXIST.getMsg());
        }
        if ( !order.getStatus().equals(Const.OrderEnum.STATUS_0.getCode()) &&
                !order.getStatus().equals(Const.OrderEnum.STATUS_60.getCode()) ){
            return ResCode.error(Const.OrderEnum.ORDER_DELETE_ERROR.getMsg());
        }
        // 删除订单
        int deleteOrder = orderMapper.deleteByPrimaryKey(order.getId());
        if (deleteOrder <= 0){
            return ResCode.error(Const.OrderEnum.UPDATE_ERROR.getMsg());
        }
        // 删除订单详细
        int deleteOrderItem = orderItemMapper.deleteByOrderNoAndUserId(orderNo, userId);
        if (deleteOrderItem <= 0){
            return ResCode.error(Const.OrderEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null, Const.OrderEnum.UPDATE_SUCCESS.getMsg());
    }


}
