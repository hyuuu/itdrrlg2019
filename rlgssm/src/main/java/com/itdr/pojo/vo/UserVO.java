package com.itdr.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Class: UserVO
 * version: JDK 1.8
 * create: 2019-09-20 08:44:33
 *
 * @author: Heyuu
 */
@Data
public class UserVO {
    private Integer id;

    private String username;

    private String email;

    private String phone;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
