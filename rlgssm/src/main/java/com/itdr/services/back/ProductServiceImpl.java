package com.itdr.services.back;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.mappers.ProductMapper;
import com.itdr.pojo.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Class: ProductServiceImpl
 * version: JDK 1.8
 * create: 2019-09-09 11:29:36
 *
 * @author: Heyuu
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    /**
     * 产品list
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResCode list(Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageNum<0){
            return ResCode.error(Const.ProductEnum.PAGE_NUM_LESS_ZERO.getMsg());
        }
        if (pageSize != null && pageSize<0){
            return ResCode.error(Const.ProductEnum.PAGE_SIZE_LESS_ZERO.getMsg());
        }
        Integer pNum = Const.ProductEnum.PAGE_NUM.getCode();
        Integer pSize = Const.ProductEnum.PAGE_SIZE.getCode();
        if (pageNum != null && pageNum != Const.ProductEnum.PAGE_NUM.getCode()){
            pNum = pageNum;
        }
        if (pageSize != null && pageSize != Const.ProductEnum.PAGE_SIZE.getCode()){
            pSize = pageSize;
        }
        List<Product> list = productMapper.selectByNumAndSize(pNum, pSize);
        if (list == null){
            return ResCode.error(Const.ProductEnum.SELECT_ERROR.getMsg());
        }
        return ResCode.success(list);
    }

    /**
     * 产品搜索
     * @param productId
     * @param productName
     * @return
     */
    @Override
    public ResCode search(Integer productId, String productName) {
        // 这个方法的数据层使用动态SQL查询，不用进行非空判断
        if (productId != null && productId < 0){
            return ResCode.error(Const.ProductEnum.ID_LESS_ZERO.getMsg());
        }
        List<Product> search = productMapper.search(productId, productName);
        if (search == null){
            return ResCode.error(Const.ProductEnum.SELECT_ERROR.getMsg());
        }
        return ResCode.success(search);
    }

    /**
     * 产品详情
     * @param productId
     * @return
     */
    @Override
    public ResCode detail(Integer productId) {
        if (productId == null){
            return ResCode.error(Const.ProductEnum.ID_NULL.getMsg());
        }
        if (productId < 0){
            return ResCode.error(Const.ProductEnum.ID_LESS_ZERO.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ResCode.error(Const.ProductEnum.PRODUCT_NOT_EXIST.getMsg());
        }
        return ResCode.success(product);
    }

    /**
     * 产品上下架
     * @param productId
     * @return
     */
    @Override
    public ResCode setSaleStatus(Integer productId) {
        if (productId == null){
            return ResCode.error(Const.ProductEnum.ID_NULL.getMsg());
        }
        if (productId < 0){
            return ResCode.error(Const.ProductEnum.ID_LESS_ZERO.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ResCode.error(Const.ProductEnum.PRODUCT_NOT_EXIST.getMsg());
        }
        Integer row = null;
        if (product.getStatus() == Const.ProductEnum.STATUS_NORMAL.getCode()){
            row = productMapper.updateStatusById(productId, Const.ProductEnum.STATUS_DISABLE.getCode());
        }
        if (product.getStatus() == Const.ProductEnum.STATUS_DISABLE.getCode()){
            row = productMapper.updateStatusById(productId, Const.ProductEnum.STATUS_NORMAL.getCode());
        }
        if (row == null || row == 0){
            return ResCode.error(Const.ProductEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null, Const.ProductEnum.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 新增OR更新产品
     * @param product
     * @return
     */
    @Override
    public ResCode save(Product product) {
        if (product.getCategoryId() == null){
            return ResCode.error(Const.ProductEnum.CATEGORY_ID_NULL.getMsg());
        }
        if (product.getCategoryId() < 0){
            return ResCode.error(Const.ProductEnum.CATEGORY_ID_LESS_ZERO.getMsg());
        }
        if (product.getName() == null || "".equals(product.getName())){
            return ResCode.error(Const.ProductEnum.NAME_NULL.getMsg());
        }
        if (product.getSubtitle() == null || "".equals(product.getSubtitle())){
            return ResCode.error(Const.ProductEnum.SUBTITLE_NULL.getMsg());
        }
        if (product.getSubtitle() == null || "".equals(product.getSubtitle())){
            return ResCode.error(Const.ProductEnum.SUBTITLE_NULL.getMsg());
        }
        if (product.getMainImage() == null || "".equals(product.getMainImage())){
            return ResCode.error(Const.ProductEnum.MAINIMAGE_NULL.getMsg());
        }
        if (product.getSubImages() == null || "".equals(product.getSubImages())){
            return ResCode.error(Const.ProductEnum.SUBIMAGES_NULL.getMsg());
        }
        if (product.getDetail() == null || "".equals(product.getDetail())){
            return ResCode.error(Const.ProductEnum.DETAIL_NULL.getMsg());
        }
        if (product.getPrice() == null){
            return ResCode.error(Const.ProductEnum.PRICE_NULL.getMsg());
        }
        if (product.getStock() == null){
            return ResCode.error(Const.ProductEnum.STOCK_NULL.getMsg());
        }
        if (product.getStock() < 0){
            return ResCode.error(Const.ProductEnum.STOCK_LESS_ZERO.getMsg());
        }
        int row = 0;
        if (product.getId() == null){
            // 新增一个商品
            row = productMapper.insert(product);
        }
        if (product.getId() != null && product.getId() < 0){
            return ResCode.error(Const.ProductEnum.ID_LESS_ZERO.getMsg());
        }
        // 更新一个商品
        row = productMapper.updateByPrimaryKeySelective(product);
        if (row == 0){
            return ResCode.error(Const.ProductEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null,Const.ProductEnum.UPDATE_SUCCESS.getMsg());
    }
}
