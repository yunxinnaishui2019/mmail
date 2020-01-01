package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int countResult = shippingMapper.insert(shipping);
        if (countResult > 0) {
            HashMap map = Maps.newHashMap();
            map.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",map);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        int countResult = shippingMapper.deleteByUserIdAndShippingID(userId,shippingId);
        if (countResult > 0) {
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    public ServerResponse<String> update(Integer userId,Shipping shipping) {
        shipping.setUserId(userId);
        int countResult = shippingMapper.updateByShipping(shipping);
        if (countResult > 0) {
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }
    public ServerResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
        if (shipping != null) {
            return ServerResponse.createBySuccess(shipping);
        }
        return ServerResponse.createByErrorMessage("无法查询到该地址");
    }

    public ServerResponse list(Integer userId, Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);

    }



}
