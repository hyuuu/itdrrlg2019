package com.itdr.services.back;

import com.itdr.common.ResCode;
import com.itdr.pojo.Category;

/**
 * Class: CategoryService
 * version: JDK 1.8
 * create: 2019-09-09 19:14:35
 *
 * @author: Heyuu
 */
public interface CategoryService {
    // 获取品类子节点(平级)
    ResCode getCategory(Integer categoryId);
    // 增加节点
    ResCode addCategory(Category category);
    // 修改品类名字
    ResCode setCategoryName(Integer categoryId, String categoryName);
    // 获取当前分类及递归子节点
    ResCode getDeepCategory(Integer categoryId);
}
