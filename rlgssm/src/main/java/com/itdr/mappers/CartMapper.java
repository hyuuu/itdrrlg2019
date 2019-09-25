package com.itdr.mappers;

import com.itdr.pojo.Cart;
import com.itdr.pojo.vo.CartProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    /**
     * 根据ID删除记录
     * @param id Integer
     * @return int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 在购物车里新增一条商品数据
     * @param record Cart
     * @return int
     */
    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    /**
     * 根据ID和userId动态更新购物车里的一条商品数据
     * @param record Cart
     * @return int
     */
    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /**
     * 根据用户ID和商品ID查询
     * @param userId 用户ID
     * @param productId 商品ID
     * @return Cart
     */
    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /**
     * 根据用户ID查询
     * @param userId Integer
     * @return List<Cart>
     */
    List<Cart> selectByUserID(Integer userId);

    /**
     * 根据userId和productIdList删除多条记录
     * @param productIdList List<String>
     * @param userId Integer
     * @return int
     */
    int deleteByUserIdAndProductIdList(@Param("productIdList") List<String> productIdList, @Param("userId") Integer userId);
    int deleteList(@Param("productIDs") List<Integer> productIDs, @Param("userId") Integer userId);

    // 查询userID的选中记录
    List<Cart> selectCheck(@Param("userId") Integer userId, @Param("checkedCode") Integer checkedCode);


}