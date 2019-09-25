package com.itdr.services.pay;

import com.itdr.common.ResCode;
import com.itdr.pojo.User;

import java.util.HashMap;

/**
 * Interface: AliPayService
 * version: JDK 1.8
 * create: 2019-09-23 10:43:18
 *
 * @author: Heyuu
 */
public interface AliPayService {
    ResCode aliPay(Long orderNo, User user);

    ResCode aliPayCallback(HashMap<String, String> map);
}
