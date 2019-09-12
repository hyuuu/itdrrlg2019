package com.itdr.mappers;

import com.itdr.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKeyWithBLOBs(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectByNumAndSize(Integer num, Integer size);

    List<Product> selectByName(String name);

    Product selectByIdAndName(Integer id, String name);

    List<Product> search(Integer id, String name);

    Integer updateStatusById(@Param("id") Integer id, @Param("status") Integer status);
}