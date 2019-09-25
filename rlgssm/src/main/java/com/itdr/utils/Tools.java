package com.itdr.utils;

import java.util.regex.Pattern;

/**
 * Class: Tools
 * version: JDK 1.8
 * create: 2019-09-11 10:20:17
 *
 * @author: Heyuu
 */
public class Tools {

    /* 正则表达式匹配邮箱:
     * @左边：
     * 1.最少三位
     * 2.除了点(.)不能出现其他特殊符号
     * 3.如果出现点(.)，点左边必须有三位或以上的字母或数字，点右边@前必须有一位或以上字母或数字
     * @右边：
     * 1.必须有(.)，可以有多个
     * 2.在每一个(.)之前必须有一位或以上的字母或数字
     * 3.在最后一个(.)之后必须有两位或以上的字母
     * */
    public static boolean isEmail(String email){
        String regex = "^[a-z0-9A-Z]{3,}([\\.][a-z0-9A-Z]+)?@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return Pattern.matches(regex,email);
    }

    /* 正则表达式匹配手机号码：简单版
    * 1.十一位
    * 2.号码开头：13* 14* 15* 16* 17* 18* 19*
    * */
    public static boolean isMobilePhone(String number){
        String regex = "^[1][3|4|5|6|7|8|9][0-9]{9}$";
        return Pattern.matches(regex,number);
    }

    /* 生成六位数验证码 */
    public static String getVCode(){
        return ((int)((Math.random()*9+1)*100000))+"";
    }

    /* 生成三位随机数 */
    public static String get3Code(){
        return ((int)((Math.random()*9+1)*100))+"";
    }
}
