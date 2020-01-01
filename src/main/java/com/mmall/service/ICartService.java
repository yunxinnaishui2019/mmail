package com.mmall.service;

import com.mmall.common.ServerResponse;

public interface ICartService {
    ServerResponse add(Integer userId, Integer productId, Integer count);
    ServerResponse update(Integer userId,Integer productId, Integer count);
    ServerResponse deleteProduct(Integer userId,String productIds);
    ServerResponse list(Integer userId);
    ServerResponse selectOrUnSelect(Integer userId,Integer productId,Integer checked);
    ServerResponse getCartProductCount(Integer userId);

}
