package com.itdr.pojo.vo;

import lombok.Data;

/**
 * Class: UserDetailVO
 * version: JDK 1.8
 * create: 2019-09-20 09:05:19
 *
 * @author: Heyuu
 */
@Data
// 可以继承UserVO的私有属性和私有方法，但私有属性和方法不可见，也就是不能直接访问，可以通过公有的get&set方法访问
public class UserDetailVO extends UserVO {
    private String question;

    private String answer;

    private Integer role;
}
