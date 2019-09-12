package com.itdr.services.back;

import com.itdr.common.ResCode;

/**
 * Class: CategoryService
 * version: JDK 1.8
 * create: 2019-09-09 19:14:35
 *
 * @author: Heyuu
 */
public interface CategoryService {
    ResCode getDeepCategory(Integer categoryId);
}
