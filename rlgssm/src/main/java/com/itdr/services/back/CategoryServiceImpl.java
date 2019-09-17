package com.itdr.services.back;

import com.itdr.common.Const;
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


    /**
     * 获取品类子节点(平级)
     * @param categoryId
     * @return
     */
    @Override
    public ResCode getCategory(Integer categoryId) {
        if (categoryId == null){
            return ResCode.error(Const.CategoryEnum.ID_NULL.getMsg());
        }
        if (categoryId < 0){
            return ResCode.error(Const.CategoryEnum.ID_LESS_ZERO.getMsg());
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            return ResCode.error(Const.CategoryEnum.CATEGORY_NOT_EXIST.getMsg());
        }
        List<Category> list = categoryMapper.selectChildrenByParentId(categoryId);
        if (list == null){
            return ResCode.error(Const.CategoryEnum.SELECT_ERROR.getMsg());
        }
        return ResCode.success(list);
    }

    /**
     * 增加节点
     * @param category
     * @return
     */
    @Override
    public ResCode addCategory(Category category) {
        if (category.getName() == null || "".equals(category.getName())){
            return ResCode.error(Const.CategoryEnum.NAME_NULL.getMsg());
        }
        if (category.getParentId() == null){
            return ResCode.error(Const.CategoryEnum.PARENT_ID_NULL.getMsg());
        }
        if (category.getParentId() < 0){
            return ResCode.error(Const.CategoryEnum.PARENT_LESS_ZERO.getMsg());
        }
        Category parentCategory = categoryMapper.selectByPrimaryKey(category.getParentId());
        if (parentCategory == null){
            return ResCode.error(Const.CategoryEnum.PARENT_CATEGORY_NOT_EXIST.getMsg());
        }
        if (category.getStatus() == null){
            category.setStatus(Const.CategoryEnum.STATUS_1.getCode());
        }
        int i = categoryMapper.insert(category);
        if (i == 0){
            return ResCode.error(Const.CategoryEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null,Const.CategoryEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 修改品类名字
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ResCode setCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null){
            return ResCode.error(Const.CategoryEnum.ID_NULL.getMsg());
        }
        if (categoryId < 0){
            return ResCode.error(Const.CategoryEnum.ID_LESS_ZERO.getMsg());
        }
        if (categoryName == null || "".equals(categoryName)){
            return ResCode.error(Const.CategoryEnum.NAME_NULL.getMsg());
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            return ResCode.error(Const.CategoryEnum.CATEGORY_NOT_EXIST.getMsg());
        }
        category.setName(categoryName);
        int i = categoryMapper.updateByPrimaryKey(category);
        if (i == 0){
            return ResCode.error(Const.CategoryEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null,Const.CategoryEnum.UPDATE_SUCCESS.getMsg());
    }


    /**
     * 获取当前节点及递归子节点
     * @param categoryId
     * @return
     */
    @Override
    public ResCode getDeepCategory(Integer categoryId) {
        if (categoryId == null){
            return ResCode.error(Const.CategoryEnum.ID_NULL.getMsg());
        }
        if (categoryId < 0){
            return ResCode.error(Const.CategoryEnum.ID_LESS_ZERO.getMsg());
        }
        List<Category> list = new ArrayList<Category>();
        List<Category> deepCategory = getDeepCategory(list, categoryId);
        // 当前id节点不存在，且没有子节点时，表示当前id节点不存在(也不是根节点)
        if (deepCategory.size() == 0){
            return ResCode.error(Const.CategoryEnum.CATEGORY_NOT_EXIST.getMsg());
        }
        return ResCode.success(deepCategory);
    }

    // 递归方法
    private List<Category> getDeepCategory(List<Category> list, Integer categoryId) {
        // 先查当前id分类，若存在，加入list
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            list.add(category);
        }
        // 再查当前id一级子节点
        List<Category> children = categoryMapper.selectChildrenByParentId(categoryId);
        // 如果没有子节点，结束当前方法
        if (children == null){
            return list;
        }
        // 子节点递归
        for (Category child : children) {
            getDeepCategory(list,child.getId());
        }
        return list;
    }
}
