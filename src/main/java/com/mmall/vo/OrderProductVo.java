package com.mmall.vo;

import com.mmall.pojo.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderProductVo {

    private String imageHost;
    private BigDecimal productTotalPrice;
    private List<OrderItemVo> orderItemVo;

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public List<OrderItemVo> getOrderItemVo() {
        return orderItemVo;
    }

    public void setOrderItemVo(List<OrderItemVo> orderItemVo) {
        this.orderItemVo = orderItemVo;
    }
}
