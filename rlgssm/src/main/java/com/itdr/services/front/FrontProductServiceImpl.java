package com.itdr.services.front;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.mappers.CategoryMapper;
import com.itdr.mappers.ProductMapper;
import com.itdr.pojo.Category;
import com.itdr.pojo.Product;
import com.itdr.pojo.vo.ProductVO;
import com.itdr.utils.PoToVoUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * Class: FrontProductServiceImpl
 * version: JDK 1.8
 * create: 2019-09-17 19:29:10
 *
 * @author: Heyuu
 */
@Service
@Transactional
public class FrontProductServiceImpl implements FrontProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 产品搜索及动态排序List
     * @param categoryId 分类ID
     * @param keyword 关键字
     * @param pageNum 页码
     * @param pageSize 数量
     * @param orderBy 排序方式
     * @return ResCode
     */
    @Override
    public ResCode list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if (pageNum<0){
            return ResCode.error(Const.ProductEnum.PAGE_NUM_LESS_ZERO.getMsg());
        }
        if (pageSize<0){
            return ResCode.error(Const.ProductEnum.PAGE_SIZE_LESS_ZERO.getMsg());
        }
        Integer num = Const.ProductEnum.PAGE_NUM.getCode();
        Integer size = Const.ProductEnum.PAGE_SIZE.getCode();
        if (pageNum != num){
            num = pageNum;
        }
        if (pageSize != size){
            size = pageSize;
        }
        String[] strings = new String[2];
        if (orderBy!= null && !"".equals(orderBy)){
            strings = orderBy.split("_");
        }
        PageHelper.startPage(num,size,strings[0]+" "+strings[1]);
        List<Product> list = productMapper.selectList(categoryId, keyword);
        PageInfo<Product> pageInfo = new PageInfo<Product>(list);
        return ResCode.success(pageInfo);
    }
    /**
     * 商品详情
     * @param productId 商品ID
     * @param is_new 1为最新，0为否
     * @param is_hot 1为最热，0为否
     * @param is_banner 1为轮播，0为否
     * @return ResCode
     */
    @Override
    public ResCode detail(Integer productId, Integer is_new, Integer is_hot, Integer is_banner) {
        if (productId != null && productId<0){
            return ResCode.error(Const.ProductEnum.ID_LESS_ZERO.getMsg());
        }
        if (is_new != null && is_new != Const.ProductEnum.IS_NEW_0.getCode() && is_new != Const.ProductEnum.IS_NEW_1.getCode()){
            return ResCode.error(Const.ProductEnum.IS_NEW_ERROR.getMsg());
        }
        if (is_hot != null && is_hot != Const.ProductEnum.IS_HOT_0.getCode() && is_hot != Const.ProductEnum.IS_HOT_1.getCode()){
            return ResCode.error(Const.ProductEnum.IS_HOT_ERROR.getMsg());
        }
        if (is_banner != null && is_banner != Const.ProductEnum.IS_BANNER_0.getCode() && is_banner != Const.ProductEnum.IS_BANNER_1.getCode()){
            return ResCode.error(Const.ProductEnum.IS_BANNER_ERROR.getMsg());
        }
        List<Product> list = productMapper.selectDetail(productId, is_new, is_hot, is_banner);
        if (list == null){
            return ResCode.error(Const.ProductEnum.SELECT_ERROR.getMsg());
        }
        List<ProductVO> productVOList = PoToVoUtil.ProductListToProductVOList(list);
        return ResCode.success(productVOList);
    }
    /**
     * 获取产品分类，一级子节点
     * @param pid 节点ID
     * @return ResCode
     */
    @Override
    public ResCode topCategory(Integer pid) {
        if (pid != null && pid < Const.CategoryEnum.ROOT_ID.getCode()){
            return ResCode.error(Const.CategoryEnum.ID_LESS_ZERO.getMsg());
        }
        Integer id = Const.CategoryEnum.ROOT_ID.getCode();
        if (pid != null && pid != id){
            id = pid;
            // 如果pid不是根节点，检查这个分类id是否存在
            Category category = categoryMapper.selectByPrimaryKey(id);
            // 不存在就直接返回了
            if (category == null){
                return ResCode.error(Const.CategoryEnum.CATEGORY_NOT_EXIST.getMsg());
            }
        }
        // 存在就拿这个pid去查它的一级子节点
        List<Category> list = categoryMapper.selectChildrenByParentId(id);
        if (list == null){
            ResCode.error(Const.CategoryEnum.SELECT_ERROR.getMsg());
        }
        return ResCode.success(list);
    }
}
