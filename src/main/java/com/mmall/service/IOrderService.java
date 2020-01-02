package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);
    public ServerResponse aliCallBack(Map<String, String> params);
    ServerResponse orderPay(Long orderNo, Integer userId);


}
