package com.itdr.services.front;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.mappers.CartMapper;
import com.itdr.mappers.ProductMapper;
import com.itdr.pojo.Cart;
import com.itdr.pojo.Product;
import com.itdr.pojo.vo.CartProductVO;
import com.itdr.pojo.vo.CartVO;
import com.itdr.utils.BigDecimalUtil;
import com.itdr.utils.PoToVoUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class: FrontCartServiceImpl
 * version: JDK 1.8
 * create: 2019-09-18 15:31:17
 *
 * @author: Heyuu
 */
@Service
@Transactional
public class FrontCartServiceImpl implements FrontCartService {

    @Resource
    private CartMapper cartMapper;
    @Resource
    private ProductMapper productMapper;

    /**
     * 获取当前用户的购物车
     * @return ResCode
     */
    @Override
    public ResCode getCartVO(Integer userId){
        CartVO cartVO = new CartVO();
        List<CartProductVO> cartProductVOList = new ArrayList<>();
        List<Cart> cartList  = cartMapper.selectByUserID(userId);
        if (cartList.size() == 0){
            cartVO.setCartProductVoList(cartProductVOList);
            return ResCode.success(cartVO);
        }
        // CartVO类：购物车默认全选
        boolean allChecked = true;
        // CartVO类：购物车总额为0
        BigDecimal cartTotalPrice = new BigDecimal("0");
        for (Cart cart : cartList) {
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product == null){
                // 应该不会出现这种情况吧，前提是管理员不能删掉数据表里的数据了
            }
            // 包装CartProductVO
            CartProductVO cartProductVO = PoToVoUtil.getCartProductVO(cart, product);
            cartProductVOList.add(cartProductVO);
            // 若cartProductVO选中并且商品状态是在售(1)：统计购物车总价；否则：将购物车全选置false
            if (cartProductVO.getProductChecked() == Const.CartEnum.PRODUCT_CHECKED.getCode() &&
                    cartProductVO.getProductStatus() == Const.ProductEnum.STATUS_NORMAL.getCode()){
                cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
            }else {
                allChecked = false;
            }
        }
        // 包装CartVO
        cartVO.setImageHost(Const.imageHost);
        cartVO.setCartProductVoList(cartProductVOList);
        cartVO.setAllChecked(allChecked);
        cartVO.setCartTotalPrice(cartTotalPrice);
        return ResCode.success(cartVO);
    }

    /**
     * 购物车添加商品
     * @param productId 商品ID
     * @param count 数量
     * @param userId 用户ID
     * @return ResCode
     */
    @Override
    public ResCode add(Integer productId, Integer count, Integer userId) {
        if (productId == null){
            return ResCode.error(Const.ProductEnum.ID_NULL.getMsg());
        }
        if (productId < Const.ProductEnum.ID_FIRST.getCode()){
            return ResCode.error(Const.ProductEnum.ID_LESS_FIRST.getMsg());
        }
        if (count == null){
            return ResCode.error(Const.ProductEnum.COUNT_NULL.getMsg());
        }
        if (count < Const.ProductEnum.COUNT_UPDATE_MIN.getCode()){
            return ResCode.error(Const.ProductEnum.COUNT_LESS_ONE.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ResCode.error(Const.ProductEnum.PRODUCT_NOT_EXIST.getMsg());
        }
        if (product.getStatus() != Const.ProductEnum.STATUS_NORMAL.getCode()){
            return ResCode.error(Const.ProductEnum.PRODUCT_NOT_EXIST_2.getMsg());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        Cart c = new Cart();
        // 执行新增或更新的操作的结果
        int res = 0;
        // 新增或更新商品的数量是否大于库存，若大于库存，则将当前所有库存存入当前用户的购物车(相当于重新赋值count = stock)
        boolean isCountOverStock = false;
        // 比较 商品库存 和 新增或更新商品的数量
        if (count > product.getStock()){
            c.setQuantity(product.getStock());
            isCountOverStock = true;
        }else {
            c.setQuantity(count);
        }
        // 执行新增或更新的操作
        if (cart == null){
            // 新增一条商品信息
            c.setUserId(userId);
            c.setProductId(productId);
            res = cartMapper.insert(c);
        }else {
            // 只更新商品数量
            c.setId(cart.getId());
            res = cartMapper.updateByPrimaryKeySelective(c);
        }
        // 执行新增或更新的操作结束
        // 失败的情况
        if (res == 0){
            return ResCode.error(Const.CartEnum.UPDATE_ERROR.getMsg());
        }
        // 成功的情况：包装CartVO类
        ResCode resCode = this.getCartVO(userId);
        if (!isCountOverStock){
            return resCode;
        }
        return ResCode.success(Const.SUCCESS_CODE,resCode.getData(),Const.ProductEnum.STOCK_INSUFFICIENT.getMsg());
    }

    /**
     * 更新购物车某个产品数量
     * @param productId 商品ID
     * @param userId 用户ID
     * @param count 数量
     * @return ResCode
     */
    @Override
    public ResCode update(Integer productId, Integer count, Integer userId) {
        if (productId == null){
            return ResCode.error(Const.ProductEnum.ID_NULL.getMsg());
        }
        if (productId < Const.ProductEnum.ID_FIRST.getCode()){
            return ResCode.error(Const.ProductEnum.ID_LESS_FIRST.getMsg());
        }
        if (count == null){
            return ResCode.error(Const.ProductEnum.COUNT_NULL.getMsg());
        }
        if (count < Const.ProductEnum.COUNT_UPDATE_MIN.getCode()){
            return ResCode.error(Const.ProductEnum.COUNT_LESS_ONE.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ResCode.error(Const.ProductEnum.PRODUCT_NOT_EXIST.getMsg());
        }
        if (product.getStatus() != Const.ProductEnum.STATUS_NORMAL.getCode()){
            return ResCode.error(Const.ProductEnum.PRODUCT_NOT_EXIST_2.getMsg());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
            return ResCode.error(Const.CartEnum.PRODUCT_NOT_EXIST.getMsg());
        }
        // 比较更新商品的数量是否大于库存，若大于库存，则重新赋值count = stock
        boolean isCountOverStock = false;
        if (count > product.getStock()){
            count = product.getStock();
            isCountOverStock = true;
        }
        Cart c = new Cart();
        c.setId(cart.getId());
        c.setQuantity(count);
        int i = cartMapper.updateByPrimaryKeySelective(c);
        // 更新失败
        if (i <= 0){
            return ResCode.error(Const.CartEnum.UPDATE_ERROR.getMsg());
        }
        // 更新成功
        ResCode resCode = this.getCartVO(userId);
        if (!isCountOverStock){
            return resCode;
        }
        return ResCode.success(Const.SUCCESS_CODE,resCode.getData(),Const.ProductEnum.STOCK_INSUFFICIENT.getMsg());
    }

    /**
     * 购物车删除商品
     * @param productIds 商品IDs
     * @param userId 用户ID
     * @return ResCode
     */
    @Override
    public ResCode deleteProduct(String productIds, Integer userId) {
        if (productIds == null || "".equals(productIds)){
            return ResCode.error(Const.ProductEnum.ID_NULL.getMsg());
        }
        String[] split = productIds.split(",");
        List<String> list = Arrays.asList(split);
        int i = cartMapper.deleteByUserIdAndProductIdList(list, userId);
        if (i <= 0){
            return ResCode.error(Const.CartEnum.UPDATE_ERROR.getMsg());
        }
        return this.getCartVO(userId);
    }

    /**
     * 购物车选中某个商品
     * @param productId 商品ID
     * @param userId 用户ID
     * @return ResCode
     */
    @Override
    public ResCode checkProduct(Integer productId, Integer userId) {
        if (productId == null){
            return ResCode.error(Const.ProductEnum.ID_NULL.getMsg());
        }
        if (productId < Const.ProductEnum.ID_FIRST.getCode()){
            return ResCode.error(Const.ProductEnum.ID_LESS_FIRST.getMsg());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
            return ResCode.error(Const.CartEnum.PRODUCT_NOT_EXIST.getMsg());
        }
        Cart c = new Cart();
        c.setId(cart.getId());
        c.setChecked(Const.CartEnum.PRODUCT_CHECKED.getCode());
        int i = cartMapper.updateByPrimaryKeySelective(c);
        if (i <= 0){
            return ResCode.error(Const.CartEnum.UPDATE_ERROR.getMsg());
        }
        return this.getCartVO(userId);
    }

    /**
     * 购物车取消选中某个商品
     * @param productId 商品ID
     * @param userId 用户ID
     * @return ResCode
     */
    @Override
    public ResCode unCheckProduct(Integer productId, Integer userId) {
        if (productId == null){
            return ResCode.error(Const.ProductEnum.ID_NULL.getMsg());
        }
        if (productId < Const.ProductEnum.ID_FIRST.getCode()){
            return ResCode.error(Const.ProductEnum.ID_LESS_FIRST.getMsg());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
            return ResCode.error(Const.CartEnum.PRODUCT_NOT_EXIST.getMsg());
        }
        Cart c = new Cart();
        c.setId(cart.getId());
        c.setChecked(Const.CartEnum.PRODUCT_UNCHECKED.getCode());
        int i = cartMapper.updateByPrimaryKeySelective(c);
        if (i <= 0){
            return ResCode.error(Const.CartEnum.UPDATE_ERROR.getMsg());
        }
        return this.getCartVO(userId);
    }

    /**
     * 查询在购物车里的产品数量
     * @param userId 用户ID
     * @return ResCode
     */
    @Override
    public ResCode getCartProductCount(Integer userId) {
        List<Cart> cartList = cartMapper.selectByUserID(userId);
        if (cartList.size() == 0){
            return ResCode.error(Const.CartEnum.PRODUCT_NOT_EXIST_2.getMsg());
        }
        return ResCode.success(cartList.size());
    }

    /**
     * 购物车全选
     * @param userId 用户ID
     * @return ResCode
     */
    @Override
    public ResCode checkAll(Integer userId) {
        List<Cart> cartList = cartMapper.selectByUserID(userId);
        if (cartList.size() == 0){
            return ResCode.error(Const.CartEnum.PRODUCT_NOT_EXIST_2.getMsg());
        }
        Cart c = new Cart();
        c.setUserId(userId);
        c.setChecked(Const.CartEnum.PRODUCT_CHECKED.getCode());
        int i = cartMapper.updateByPrimaryKeySelective(c);
        if (i <= 0){
            return ResCode.error(Const.CartEnum.UPDATE_ERROR.getMsg());
        }
        return this.getCartVO(userId);
    }

    /**
     * 购物车取消全选
     * @param userId 用户ID
     * @return ResCode
     */
    @Override
    public ResCode unCheckAll(Integer userId) {
        List<Cart> cartList = cartMapper.selectByUserID(userId);
        if (cartList.size() == 0){
            return ResCode.error(Const.CartEnum.PRODUCT_NOT_EXIST_2.getMsg());
        }
        Cart c = new Cart();
        c.setUserId(userId);
        c.setChecked(Const.CartEnum.PRODUCT_UNCHECKED.getCode());
        int i = cartMapper.updateByPrimaryKeySelective(c);
        if (i <= 0){
            return ResCode.error(Const.CartEnum.UPDATE_ERROR.getMsg());
        }
        return this.getCartVO(userId);
    }
}
