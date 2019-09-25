package com.itdr.utils;

import java.math.BigDecimal;

/**
 * Class: BigDecimalUtil
 * version: JDK 1.8
 * create: 2019-09-19 15:10:43
 *
 * @author: Heyuu
 */
public class BigDecimalUtil {

    /**
     * BigDecimal 加法
     * @param d1 Double
     * @param d2 Double
     * @return BigDecimal
     */
    public static BigDecimal add(Double d1, Double d2){
        BigDecimal bd1 = new BigDecimal(d1.toString());
        BigDecimal bd2 = new BigDecimal(d2.toString());
        return bd1.add(bd2);
    }

    /**
     * BigDecimal 减法
     * @param d1 Double
     * @param d2 Double
     * @return BigDecimal
     */
    public static BigDecimal sub(Double d1, Double d2){
        BigDecimal bd1 = new BigDecimal(d1.toString());
        BigDecimal bd2 = new BigDecimal(d2.toString());
        return bd1.subtract(bd2);
    }

    /**
     * BigDecimal 乘法
     * @param d1 Double
     * @param d2 Double
     * @return BigDecimal
     */
    public static BigDecimal mul(Double d1, Double d2){
        BigDecimal bd1 = new BigDecimal(d1.toString());
        BigDecimal bd2 = new BigDecimal(d2.toString());
        return bd1.multiply(bd2);
    }

    /**
     * BigDecimal 除法，保留两位小数，四舍五入
     * @param d1 Double
     * @param d2 Double
     * @return BigDecimal
     */
    public static BigDecimal div(Double d1, Double d2){
        BigDecimal bd1 = new BigDecimal(d1.toString());
        BigDecimal bd2 = new BigDecimal(d2.toString());
        return bd1.divide(bd2,2, BigDecimal.ROUND_HALF_UP);
    }
}
