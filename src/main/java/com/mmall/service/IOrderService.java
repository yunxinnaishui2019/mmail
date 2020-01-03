package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderProductVo;
import com.mmall.vo.OrderVo;

import java.util.Map;

public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);
    public ServerResponse aliCallBack(Map<String, String> params);
    ServerResponse orderPay(Long orderNo, Integer userId);
    ServerResponse<OrderVo> createOrder(Integer userId, Integer shippingId);
    ServerResponse cancel(Integer userId, Integer orderNo);
    ServerResponse<OrderProductVo> getOrderCartProduct(Integer userId);
    ServerResponse<PageInfo> List(Integer userId, int pageNum, int pageSize);





    ServerResponse<OrderVo> getDetail(Long orderNo);
    ServerResponse<PageInfo> getList(Integer pageSize, Integer pageNum);
    ServerResponse<PageInfo> getSearch(Long orderNo,Integer pageNum,Integer pageSize);

}
