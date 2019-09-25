package com.itdr.mappers;

import com.itdr.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    // 插入一条商品记录
    int insert(Product record);

    int insertSelective(Product record);

    // 根据id查一个商品
    Product selectByPrimaryKey(Integer id);

    // 根据id更新一条记录
    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectByNumAndSize(@Param("num")Integer num, @Param("size")Integer size);

    List<Product> selectByName(String name);

    Product selectByIdAndName(Integer id, String name);

    // 根据id精确搜索或根据name模糊搜索商品，若id=null，则查limit0,10
    // 查：id,category_id,name,subtitle,main_image,price
    List<Product> search(@Param("id")Integer id, @Param("name")String name);
    //根据id更新状态
    Integer updateStatusById(@Param("id") Integer id, @Param("status") Integer status);
    // 按categoryId或keyword搜索产品列表，按column进行order排序，分页
    List<Product> selectList(@Param("categoryId") Integer categoryId, @Param("keyword") String keyword);
    // 根据条件动态查询商品详情
    List<Product> selectDetail(@Param("id") Integer id, @Param("is_new") Integer is_new,
                               @Param("is_hot") Integer is_hot, @Param("is_banner") Integer is_banner);

    // 根据ID集合查询
    List<Product> selectByIDs(List<Integer> productIDs);

    // 根据ID更新库存
    int updateStockById(@Param("userId") Integer userId, @Param("stock") Integer stock);
}