package com.itdr.services.back;

import com.itdr.common.ResCode;
import com.itdr.pojo.Product;

/**
 * Interface: ProductService
 * version: JDK 1.8
 * create: 2019-09-09 11:27:20
 *
 * @author: Heyuu
 */
public interface ProductService {
    // 产品list
    ResCode list(Integer pageNum, Integer pageSize);
    // 产品搜索
    ResCode search(Integer productId, String productName);
    // 产品详情
    ResCode detail(Integer productId);
    // 产品上下架
    ResCode setSaleStatus(Integer productId);

    ResCode save(Product product);
}
