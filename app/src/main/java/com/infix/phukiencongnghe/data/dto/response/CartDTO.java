package com.infix.phukiencongnghe.data.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class CartDTO {
    private Integer id;
    private List<CartItemDTO> cartItems;
    private BigDecimal totalPrice;

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
