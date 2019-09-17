package com.itdr.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;

/**
 * Class: ResCode
 * version: JDK 1.8
 * create: 2019-09-09 10:11:11
 *
 * @author: Heyuu
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResCode<T> implements Serializable {
    // 状态码
    private Integer status;
    // 状态信息
    private String msg;
    // 数据
    private T data;
    // 通用成功状态码
    private static final Integer SUCCESS_CODE = Const.SUCCESS_CODE;
    // 通用失败状态码
    private static final Integer ERROR_CODE = Const.ERROR_CODE;

    // error
    private ResCode(String msg){
        this.status = ERROR_CODE;
        this.msg = msg;
    }
    private ResCode(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }
    // success
    private ResCode(T data){
        this.status = SUCCESS_CODE;
        this.data =  data;
    }
    private ResCode(Integer status, T data){
        this.status = status;
        this.data =  data;
    }
    private ResCode(Integer status, T data, String msg){
        this.status = status;
        this.data =  data;
        this.msg = msg;
    }

    public static ResCode error(String msg){
        return new ResCode(msg);
    }
    public static ResCode error(Integer status, String msg){
        return new ResCode(status, msg);
    }

    public static <T> ResCode success(T data){
        return new ResCode(data);
    }
    public static <T> ResCode success(Integer status, T data){
        return new ResCode(status, data);
    }
    public static <T> ResCode success(Integer status, T data, String msg){
        return new ResCode(status, data, msg);
    }

    /**
     * 判断ResCode的状态码是否是成功状态码
     * @return boolean
     */
    @JsonIgnore
    public boolean isSucess(){
        return this.status == SUCCESS_CODE;
    }
}
