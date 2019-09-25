package com.itdr.services.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.mappers.OrderItemMapper;
import com.itdr.mappers.OrderMapper;
import com.itdr.mappers.PayInfoMapper;
import com.itdr.mappers.ProductMapper;
import com.itdr.pojo.*;
import com.itdr.pojo.pay.Configs;
import com.itdr.utils.DateUtil;
import com.itdr.utils.JsonUtil;
import com.itdr.utils.PoToVoUtil;
import com.itdr.utils.ZxingUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class: AliPayServiceImpl
 * version: JDK 1.8
 * create: 2019-09-23 10:43:36
 *
 * @author: Heyuu
 */
@Service
@Transactional
public class AliPayServiceImpl implements AliPayService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private PayInfoMapper payInfoMapper;
    @Resource
    private ProductMapper productMapper;

    // 支付
    @Override
    public ResCode aliPay(Long orderNo, User user) {
        // 非空判断
        if (orderNo == null){
            return ResCode.error(Const.OrderEnum.ORDERNO_NULL.getMsg());
        }
        // 判断订单是否存在
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            return ResCode.error(Const.OrderEnum.ORDER_NOT_EXIST.getMsg());
        }
        // 判断订单状态
        if (order.getStatus() < Const.OrderEnum.STATUS_10.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_0.getMsg());
        }
        if (order.getStatus() > Const.OrderEnum.STATUS_10.getCode()){
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_20.getMsg());
        }
        // 判断订单和用户是否匹配
        if (!order.getUserId().equals(user.getId())){
            return ResCode.error(Const.PayEnum.ORDER_USER_ERROR.getMsg());
        }
        // 根据订单号查询对应商品详情
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        // 检查库存与购买数量的关系
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            if (product == null){
                return ResCode.error(Const.ProductEnum.PRODUCT_NOT_EXIST_2.getMsg());
            }
            if (product.getStock() < orderItem.getQuantity()){
                return ResCode.error(product.getName()+Const.ProductEnum.STOCK_INSUFFICIENT.getMsg());
            }
        }



        // 调用支付宝接口获取支付宝二维码信息
        AlipayTradePrecreateResponse response = null;
        try {
            response = test_trade_precreate(order, orderItemList);
        } catch (AlipayApiException e) {
            // 出现异常，预下单失败
            e.printStackTrace();
            System.out.println("================================================1");
            return ResCode.error(Const.PayEnum.PAY_ERROR_1.getMsg());
        }
        // 判断预下单是否成功
        if (!response.isSuccess()){
            System.out.println("================================================2");
            return ResCode.error(Const.PayEnum.PAY_ERROR_1.getMsg());
        }
        // 预下单成功
        // 将二维码信息串生成图片，并保存，（需要修改为运行机器上的路径）
        // 后期图片需要放到图片服务器上
        String filePath = String.format(Configs.getSavecode_test()+"qr-%s.png", response.getOutTradeNo());
        ZxingUtil.getQRCodeImage(response.getQrCode(), 256, filePath);

        // 返回数据 = 订单号 + 二维码图片地址
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("orderNo", order.getOrderNo());
        map.put("qrCode", filePath);
        return ResCode.success(map);
    }

    // 调用支付宝接口，返回订单二维码信息
    private AlipayTradePrecreateResponse test_trade_precreate(Order order, List<OrderItem> orderItems) throws AlipayApiException {
        //读取配置文件信息
        Configs.init("zfbinfo.properties");

        //实例化支付宝客户端
        AlipayClient alipayClient = new DefaultAlipayClient(Configs.getOpenApiDomain(),
                Configs.getAppid(), Configs.getPrivateKey(), "json", "utf-8",
                Configs.getAlipayPublicKey(), Configs.getSignType());

        //创建API对应的request类
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        //获取一个BizContent对象,并转换成json格式
        String str = JsonUtil.obj2String(PoToVoUtil.getBizContent(order, orderItems));
        request.setBizContent(str);
        //设置支付宝回调路径
        request.setNotifyUrl(Configs.getNotifyUrl_test());
        //获取响应,这里要处理一下异常
        AlipayTradePrecreateResponse response = alipayClient.execute(request);

        //返回响应的结果
        return response;
    }

    // 支付宝回调
    @Override
    public ResCode aliPayCallback(HashMap<String, String> map) {
        //step1:获取ordrNo
        Long orderNo = Long.parseLong(map.get("out_trade_no"));
        //step2:获取流水号
        String tarde_no = map.get("trade_no");
        //step3:获取支付状态
        String trade_status = map.get("trade_status");
        //step4:获取支付时间
        String payment_time = map.get("gmt_payment");
        //step5:获取订单金额
        BigDecimal total_amount = new BigDecimal(map.get("total_amount"));
/*
        //step6:获取用户id
        Integer userId = Integer.parseInt(map.get("userId"));
*/

        // 根据支付宝返回参数的订单号查询订单
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            System.out.println("=======================================6");
            return ResCode.error(Const.OrderEnum.ORDER_NOT_EXIST.getMsg());
        }
        // 根据支付宝返回参数的订单号查询订单详情
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        if (orderItemList.size() == 0){
            System.out.println("=======================================6-1");
            return ResCode.error(Const.OrderEnum.ORDER_NOT_EXIST.getMsg());
        }
        // 判断订单状态，避免不断回调
        if (order.getStatus() < Const.OrderEnum.STATUS_10.getCode()){
            System.out.println("=======================================7");
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_0.getMsg());
        }
        if (order.getStatus() > Const.OrderEnum.STATUS_10.getCode()){
            System.out.println("=======================================8");
            return ResCode.error(Const.OrderEnum.ORDER_STATUS_20.getMsg());
        }
        // 判断支付宝返回参数金额是否一致
        if (order.getPayment().compareTo(total_amount) != 0){
            System.out.println("=======================================9");
            return ResCode.error(Const.PayEnum.TOTAL_AMOUNT_ERROR.getMsg());
        }
/*
        // 判断用户ID，（实际上应该判断买家支付宝账户是否一致）
        if (!order.getUserId().equals(userId)){
            System.out.println("=======================================10");
            return ResCode.error(Const.PayEnum.USER_ID_ERROR.getMsg());
        }
*/

        // 判断支付宝返回参数支付状态
        if (Const.TRADE_STATUS_3.equals(trade_status)){
            // 支付成功，更新订单状态，付款时间
            order.setStatus(Const.OrderEnum.STATUS_20.getCode());
            order.setPaymentTime(DateUtil.strToDate(payment_time));
            int i = orderMapper.updateByPrimaryKeySelective(order);
            if (i <= 0){
                System.out.println("=======================================11");
                return ResCode.error(Const.PayEnum.UPDATE_ERROR.getMsg());
            }
            // 支付成功，更新库存
            for (OrderItem orderItem : orderItemList) {
                Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
                if (product == null){
                    System.out.println("=======================================11-1");
                    return ResCode.error(Const.ProductEnum.PRODUCT_NOT_EXIST.getMsg());
                }
                Integer stock = product.getStock() - orderItem.getQuantity();
                if (stock < 0){
                    // 会有库存少于零的情况吗？理不清楚了。。。要不然丢个异常出来吸引事务救救我吧，笑哭。。。
                }
                int update = productMapper.updateStockById(orderItem.getProductId(), stock);
                if (update <= 0){
                    System.out.println("=======================================11-2");
                    return ResCode.error(Const.ProductEnum.STOCK_UPDATE_ERROR.getMsg());
                }
            }

            // 删除二维码
            String filePath = String.format(Configs.getSavecode_test() + "qr-%s.png", orderNo);
            File file = new File(filePath);
            boolean delete = file.delete();
            if (!delete){
                // 就算删除失败，不应该影响到支付业务的继续
                System.out.println("=======================================12");
            }
        }
        // 不论支付宝返回参数支付状态是否成功，都要保存支付信息
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(orderNo);
        payInfo.setPayPlatform(Const.PayEnum.PLATFORM_1.getCode());
        payInfo.setPlatformNumber(tarde_no);
        payInfo.setPlatformStatus(trade_status);
        // 保存支付信息记录
        int i = payInfoMapper.insert(payInfo);
        if (i <= 0){
            System.out.println("=======================================13");
            return ResCode.error(Const.PayInfoEnum.INSERT_ERROR.getMsg());
        }
        System.out.println("=======================================14");
        return ResCode.success("SUCCESS");
    }
}
