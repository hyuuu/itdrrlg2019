package com.itdr.services;

import com.itdr.common.ResCode;
import com.itdr.mappers.ProductMapper;
import com.itdr.pojo.ProductWithBLOBs;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Override
    public ResCode selectById(Integer id) {
        ResCode resCode = null;
        if (id == null){
            resCode = ResCode.error(1,"参数为空！");
            return resCode;
        }
        ProductWithBLOBs product = productMapper.selectByPrimaryKey(id);
        if (product == null){
            resCode = ResCode.error(1,"无此商品！");
            return resCode;
        }
        resCode = ResCode.success(0,product);
        return resCode;
    }

    @Override
    public ResCode selectByName(String name) {
        ResCode resCode = null;
        if (name == null || name.equals("")){
            resCode = ResCode.error(1,"参数为空！");
            return resCode;
        }
        List<ProductWithBLOBs> products = productMapper.selectByName(name);
        if (products == null){
            resCode = ResCode.error(1,"查询异常！");
            return resCode;
        }
        if (products.size() == 0){
            resCode = ResCode.error(1,"无此商品！");
            return resCode;
        }
        resCode = ResCode.success(0,products);
        return resCode;
    }

    @Override
    public ResCode selectByIdAndName(Integer id, String name) {
        ResCode resCode = null;
        if (id == null || name == null || name.equals("")){
            resCode = ResCode.error(1,"参数为空！");
            return resCode;
        }
        ProductWithBLOBs product = productMapper.selectByIdAndName(id, name);
        if (product == null){
            resCode = ResCode.error(1,"无此商品！");
            return resCode;
        }
        resCode = ResCode.success(0,product);
        return resCode;
    }

    @Override
    public ResCode list(Integer pageNum, Integer pageSize) {
        ResCode resCode = null;
        Integer pNum = 0;
        Integer pSize = 10;
        if (pageNum != null && pageNum != 0){
            pNum = pageNum;
        }
        if (pageSize != null && pageSize != 10 ){
            pSize = pageSize;
        }
        List<ProductWithBLOBs> list = productMapper.selectByNumAndSize(pNum, pSize);
        if (list == null){
            resCode = ResCode.error(1,"查询失败了");
            return resCode;
        }
        resCode = ResCode.success(0,list);
        return resCode;
    }

    /* 以前写搜索的写法
    @Override
    public ResCode search(Integer productId, String productName) {
        ResCode resCode = null;
        // 只有ID
        if (productId != null && (productName == null || productName.equals(""))){
            resCode = selectById(productId);
            return resCode;
        }
        // 只有name
        if (productId == null && productName != null && !productName.equals("")){
            resCode = selectByName(productName);
            System.out.println("只有name");
            return resCode;
        }
        // 两者都有
        if (productId != null && productName != null && !productName.equals("")){
            resCode = selectByIdAndName(productId,productName);
            return resCode;
        }
        // 两者都没有
        resCode = list(0,10);
        return resCode;
    }
    */

    // 现在写搜索的写法：动态SQL
    @Override
    public ResCode search(Integer productId, String productName) {
        ResCode resCode = null;
        List<ProductWithBLOBs> search = productMapper.search(productId, productName);
        if (search == null){
            resCode = ResCode.error(1,"查询异常！");
            return resCode;
        }
        if (search.size() == 0){
            resCode = ResCode.error(1,"无此商品！");
            return resCode;
        }
        resCode = ResCode.success(0,search);
        return resCode;
    }

    @Override
    public ResCode set_sale_status(Integer productId) {
        ResCode resCode = null;
        ProductWithBLOBs product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            resCode = ResCode.error(1,"商品不存在！");
            return resCode;
        }
        Integer row = null;
        if (product.getStatus() == 0){
            row = productMapper.updateStatusById(productId, 1);
        }
        if (product.getStatus() == 1){
            row = productMapper.updateStatusById(productId, 0);
        }
        if (row == null){
            resCode = ResCode.error(1,"更新异常！");
            return resCode;
        }
        if (row == 0){
            resCode = ResCode.error(1,"更新失败！");
            return resCode;
        }
        resCode = ResCode.success(0,"更新成功！ row="+row);
        return resCode;
    }


}
