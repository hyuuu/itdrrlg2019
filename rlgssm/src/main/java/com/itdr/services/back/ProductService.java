package com.itdr.services.back;

import com.itdr.common.ResCode;

/**
 * Interface: ProductService
 * version: JDK 1.8
 * create: 2019-09-09 11:27:20
 *
 * @author: Heyuu
 */
public interface ProductService {
    ResCode selectById(Integer id);
    ResCode selectByName(String name);
    ResCode selectByIdAndName(Integer id, String name);

    ResCode list(Integer pageNum, Integer pageSize);

    ResCode search(Integer productId, String productName);


    ResCode set_sale_status(Integer productId);
}
