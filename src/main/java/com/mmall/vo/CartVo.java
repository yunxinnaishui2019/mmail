package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
    private List<CartProductVo> cartProductVo;
    private boolean allChecked;
    private BigDecimal cartTotalPrice;
    private String imageHost;

    public List<CartProductVo> getCartProductVo() {
        return cartProductVo;
    }

    public void setCartProductVo(List<CartProductVo> cartProductVo) {
        this.cartProductVo = cartProductVo;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
