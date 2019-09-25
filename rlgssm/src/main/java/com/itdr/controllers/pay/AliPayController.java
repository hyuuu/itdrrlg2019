package com.itdr.controllers.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.pojo.User;
import com.itdr.pojo.pay.Configs;
import com.itdr.services.pay.AliPayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class: AliPayController
 * version: JDK 1.8
 * create: 2019-09-23 10:07:13
 *
 * @author: Heyuu
 */
@Controller
@ResponseBody
@RequestMapping("/pay/")
public class AliPayController {

    @Resource
    private AliPayService aliPayService;

    @RequestMapping("alipay.do")
    public ResCode aliPay(Long orderNo, HttpSession session){
        User user = (User) session.getAttribute(Const.USER_LOGIN_SESSION);
        if (user == null){
            return ResCode.error(Const.UserEnum.USER_NOT_LOGIN.getMsg());
        }
        return aliPayService.aliPay(orderNo, user);
    }

    // 支付宝回调接口
    // 9.cookies、session 等在此页面会失效，即无法获取这些数据
    @RequestMapping("alipay_callback.do")
    public String aliPayCallback(HttpServletRequest request){
        System.out.println("=======================================alipay_callback.do");

        // 获取支付宝返回的参数集合
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 创建一个新的集合，存储过滤之后的返回参数
        HashMap<String, String> newMap = new HashMap<>();
        // 获取参数集合，遍历重组数据
        Iterator<String> iterator = parameterMap.keySet().iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            String[] strings = parameterMap.get(next);
            String values = "";
            for (int i = 0; i < strings.length; i++) {
                // 判断i是不是最后一个元素，是就直接加到values后边，不是就多加个逗号隔开
                values = (i == strings.length-1) ? values + strings[i] : values + strings[i] + ",";
            }
            newMap.put(next, values);
        }

        // 支付宝验签：验证是不是支付宝发来的请求
        try {
            // 不需要 sign、sign_type 两个参数，rsaCheckV2()里清除了sign，所以外面只需要清除sign_type
            newMap.remove("sign_type");
            // 官方验签
            boolean res = AlipaySignature.rsaCheckV2(newMap, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!res){
                System.out.println("=======================================2");
                return Const.CALLBACK_FAILED;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            System.out.println("=======================================3");
            return Const.CALLBACK_FAILED;
        }

        // 验签通过，执行业务
        ResCode resCode =  aliPayService.aliPayCallback(newMap);
        if (!resCode.isSucess()){
            System.out.println("=======================================4");
            return Const.CALLBACK_FAILED;
        }
        System.out.println("=======================================5");
        return Const.CALLBACK_SUCCESS;
    }
}
