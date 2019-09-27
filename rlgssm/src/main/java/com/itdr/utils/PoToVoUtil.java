package com.itdr.utils;

import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.itdr.common.Const;
import com.itdr.pojo.*;
import com.itdr.pojo.pay.BizContent;
import com.itdr.pojo.pay.PGoodsDetail;
import com.itdr.pojo.vo.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: PoToVoUtil
 * version: JDK 1.8
 * create: 2019-09-18 13:47:14
 *
 * @author: Heyuu
 */
public class PoToVoUtil {
    /**
     * 将Product包装成ProductVO
     * @param product Product
     * @return ProductVO
     */
    public static ProductVO ProductToProductVO(Product product){
        ProductVO productVO = new ProductVO();
        productVO.setImageHost(Const.imageHost);
        productVO.setId(product.getId());
        productVO.setCategoryId(product.getCategoryId());
        productVO.setName(product.getName());
        productVO.setSubtitle(product.getSubtitle());
        productVO.setMainImage(product.getMainImage());
        productVO.setSubImages(product.getSubImages());
        productVO.setDetail(product.getDetail());
        productVO.setPrice(product.getPrice());
        productVO.setStock(product.getStock());
        productVO.setStatus(product.getStatus());
        productVO.setIsNew(product.getIsNew());
        productVO.setIsHot(product.getIsHot());
        productVO.setIsBanner(product.getIsBanner());
        productVO.setCreateTime(product.getCreateTime());
        productVO.setUpdateTime(product.getUpdateTime());
        return productVO;
    }

    /**
     * 将List<Product>包装成List<ProductVO>
     * @param productList List<Product>
     * @return List<ProductVO>
     */
    public static List<ProductVO> ProductListToProductVOList(List<Product> productList){
        List<ProductVO> productVOList = new ArrayList<ProductVO>();
        for (Product product : productList) {
            ProductVO productVO = ProductToProductVO(product);
            productVOList.add(productVO);
        }
        return productVOList;
    }

    /**
     * 将Cart和Product包装成CartProductVO
     * @param cart Cart
     * @param product Product
     * @return CartProductVO
     */
    public static CartProductVO getCartProductVO(Cart cart, Product product){
        CartProductVO cartProductVO = new CartProductVO();
        cartProductVO.setId(cart.getId());
        cartProductVO.setUserId(cart.getUserId());
        cartProductVO.setProductId(cart.getProductId());
        cartProductVO.setProductName(product.getName());
        cartProductVO.setProductSubtitle(product.getSubtitle());
        cartProductVO.setProductMainImage(product.getMainImage());
        cartProductVO.setProductPrice(product.getPrice());
        cartProductVO.setProductStatus(product.getStatus());
        cartProductVO.setProductStock(product.getStock());
        if (cart.getQuantity() <= product.getStock()){
            // 选择商品数量未超过库存时
            cartProductVO.setQuantity(cart.getQuantity());
            cartProductVO.setLimitQuantity(Const.CartEnum.LIMIT_NUM_SUCCESS.getMsg());

        }else {
            // 选择商品数量已超过库存时
            cartProductVO.setQuantity(product.getStock());
            cartProductVO.setLimitQuantity(Const.CartEnum.LIMIT_NUM_FAIL.getMsg());
        }
        // 计算总价
        BigDecimal productTotalPrice = BigDecimalUtil.mul(cartProductVO.getProductPrice().doubleValue(), cartProductVO.getQuantity().doubleValue());
        cartProductVO.setProductTotalPrice(productTotalPrice);
        // 如果商品状态不是在售(1)，则强制取消当前商品的选中状态
        cartProductVO.setProductChecked(cart.getChecked());
        if (product.getStatus() != Const.ProductEnum.STATUS_NORMAL.getCode()){
            cartProductVO.setProductChecked(Const.CartEnum.PRODUCT_UNCHECKED.getCode());
        }
        return cartProductVO;
    }

    /**
     * 将User包装成UserVO
     * @param user User
     * @return UserVO
     */
    public static UserVO UserToUserVO(User user){
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setEmail(user.getEmail());
        userVO.setPhone(user.getPhone());
        userVO.setCreateTime(user.getCreateTime());
        userVO.setUpdateTime(user.getUpdateTime());
        return userVO;
    }

    /**
     * 将 ser 包装成 UserDetailVO
     * @param user User
     * @return UserDetailVO
     */
    public static UserDetailVO UserToUserDetailVO(User user){
        UserDetailVO userDetailVO = new UserDetailVO();
        userDetailVO.setId(user.getId());
        userDetailVO.setUsername(user.getUsername());
        userDetailVO.setEmail(user.getEmail());
        userDetailVO.setPhone(user.getPhone());
        userDetailVO.setQuestion(user.getQuestion());
        userDetailVO.setAnswer(user.getAnswer());
        userDetailVO.setRole(user.getRole());
        userDetailVO.setCreateTime(user.getCreateTime());
        userDetailVO.setUpdateTime(user.getUpdateTime());
        return userDetailVO;
    }

    /*商品详情和支付宝商品类转换*/
    public static PGoodsDetail getNewPay(OrderItem orderItem){
        PGoodsDetail info = new PGoodsDetail();
        info.setGoods_id(orderItem.getProductId().toString());
        info.setGoods_name(orderItem.getProductName());
        info.setPrice(orderItem.getCurrentUnitPrice().toString());
        info.setQuantity(orderItem.getQuantity().longValue());
        return info;
    }
    /*获取一个BizContent对象*/
    public static BizContent getBizContent(Order order, List<OrderItem> orderItems){
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(order.getOrderNo());

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "睿乐GO在线平台";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = String.valueOf(order.getPayment());

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品"+orderItems.size()+"件共"+order.getPayment()+"元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "001";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "001";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        for (OrderItem orderItem : orderItems) {
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = getNewPay(orderItem);
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        BizContent biz = new BizContent();
        biz.setSubject(subject);
        biz.setTotal_amount(totalAmount);
        biz.setOut_trade_no(outTradeNo);
        biz.setUndiscountable_amount(undiscountableAmount);
        biz.setSeller_id(sellerId);
        biz.setBody(body);
        biz.setOperator_id(operatorId);
        biz.setStore_id(storeId);
        biz.setExtend_params(extendParams);
        biz.setTimeout_express(timeoutExpress);
        //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
        //biz.setNotify_url(Configs.getNotifyUrl_test()+"portal/order/alipay_callback.do");
        biz.setGoods_detail(goodsDetailList);

        return biz;
    }

    public static ShippingVO ShippingToShippingVO(Shipping shipping){
        ShippingVO shippingVO = new ShippingVO();
        shippingVO.setId(shipping.getId());
        shippingVO.setUserId(shipping.getUserId());
        shippingVO.setReceiverName(shipping.getReceiverName());
        shippingVO.setReceiverPhone(shipping.getReceiverPhone());
        shippingVO.setReceiverMobile(shipping.getReceiverMobile());
        shippingVO.setReceiverProvince(shipping.getReceiverProvince());
        shippingVO.setReceiverCity(shipping.getReceiverCity());
        shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVO.setReceiverAddress(shipping.getReceiverAddress());
        shippingVO.setReceiverZip(shipping.getReceiverZip());
        return shippingVO;
    }

    public static OrderItemVO OrderItemToOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO = new OrderItemVO();
        orderItemVO.setOrderNo(orderItem.getOrderNo());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        orderItemVO.setCreateTime(orderItem.getCreateTime());
        return orderItemVO;
    }

    public static OrderVO getOrderVO(Order order, List<OrderItemVO> orderItemVOList, ShippingVO shippingVO){
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        orderVO.setPaymentTime(order.getPaymentTime());
        orderVO.setSendTime(order.getSendTime());
        orderVO.setEndTime(order.getEndTime());
        orderVO.setCloseTime(order.getCloseTime());
        orderVO.setCreateTime(order.getCreateTime());
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setImageHost(Const.imageHost);
        orderVO.setShippingId(shippingVO.getId());
        orderVO.setShippingVO(shippingVO);
        return orderVO;
    }
}
