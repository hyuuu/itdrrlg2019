package com.itdr.mappers;

import com.itdr.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    // 插入一个用户
    int insert(Category record);

    int insertSelective(Category record);

    // 根据id查询一个分类
    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    // 根据id更新
    // 更新：parent_id，name，status，sort_order，update_time
    int updateByPrimaryKey(Category record);

    // 根据parent_id查询若干分类
    List<Category> selectChildrenByParentId(Integer parentId);
}