package com.itdr.mappers;

import com.itdr.pojo.Product;
import com.itdr.pojo.ProductWithBLOBs;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductWithBLOBs record);

    int insertSelective(ProductWithBLOBs record);

    ProductWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ProductWithBLOBs record);

    int updateByPrimaryKey(Product record);

    List<ProductWithBLOBs> selectByNumAndSize(Integer num, Integer size);

    List<ProductWithBLOBs> selectByName(String name);

    ProductWithBLOBs selectByIdAndName(Integer id, String name);

    List<ProductWithBLOBs> search(Integer id, String name);

    Integer updateStatusById(Integer id, Integer status);
}