package com.itdr.common;

import lombok.Data;

/**
 * Class: ResCode
 * version: JDK 1.8
 * create: 2019-09-09 10:11:11
 *
 * @author: Heyuu
 */
@Data
public class ResCode<T> {
    private Integer status;
    private String msg;
    private T data;

    private ResCode(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }
    private ResCode(Integer status, T data){
            this.status = status;
            this.data =  data;
    }
    public static <T> ResCode success(Integer status, T data){
        ResCode resCode = new ResCode(status, data);
        return resCode;
    }
    public static ResCode error(Integer status, String msg){
        ResCode resCode = new ResCode(status, msg);
        return resCode;
    }
}
