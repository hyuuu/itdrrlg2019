package com.itdr.services.back;

import com.itdr.common.ResCode;
import com.itdr.mappers.CategoryMapper;
import com.itdr.pojo.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: CategoryServiceImpl
 * version: JDK 1.8
 * create: 2019-09-09 19:15:00
 *
 * @author: Heyuu
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;


    // 递归求分类及子分类
    @Override
    public ResCode getDeepCategory(Integer categoryId) {
        ResCode resCode = null;
        if (categoryId == null){
            resCode = ResCode.error(1,"参数为空！");
            return resCode;
        }
        List<Category> list = new ArrayList<Category>();
        List<Category> deepCategory = getDeepCategory(list, categoryId);
        if (deepCategory.size() == 0){
            resCode = ResCode.error(1,"无此分类！");
            return resCode;
        }
        resCode = ResCode.success(0,deepCategory);
        return resCode;
    }
    // 递归方法
    private List<Category> getDeepCategory(List<Category> list, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            list.add(category);
        }
        List<Category> children = categoryMapper.selectChildrenByParentId(categoryId);
        if (children == null){
            return list;
        }
        for (Category child : children) {
            getDeepCategory(list,child.getId());
        }
        return list;
    }
}
