package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {

    ServerResponse<String> saveOrUpdateProduct(Product product);
    ServerResponse<String> setStatus(Integer productId, Integer status);
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    ServerResponse<PageInfo> manageProductList(Integer pageNum, Integer pageSize);
    ServerResponse manageProductSearch(String productName, Integer productId, Integer pageNum, Integer pageSize);
    ServerResponse productDetail(Integer productId);
    ServerResponse productSearch(Integer categoryId, String keyword, Integer pageNum, Integer pageSize,String orderBy);

}
