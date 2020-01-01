package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse<String> delete(Integer userId, Integer shippingId);
    ServerResponse<String> update(Integer userId,Shipping shipping);
    ServerResponse select(Integer userId, Integer shippingId);
    ServerResponse list(Integer userId, Integer pageNum,Integer pageSize);
}
