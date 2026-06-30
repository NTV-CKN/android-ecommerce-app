package com.infix.phukiencongnghe.data.dto.response;

import java.math.BigDecimal;

public class OrderDetailsHistoryDTO {
    private Integer id;
    private Integer orderId;
    private BigDecimal totalPrice;
    private Integer productVariantId;
    private Integer quantity;
    private String productVariantName;
    private String productVariantImage;

    public OrderDetailsHistoryDTO(Integer id, Integer orderId, BigDecimal totalPrice, Integer productVariantId, Integer quantity, String productVariantName, String productVariantImage) {
        this.id = id;
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.productVariantName = productVariantName;
        this.productVariantImage = productVariantImage;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(Integer productVariantId) {
        this.productVariantId = productVariantId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductVariantName() {
        return productVariantName;
    }

    public void setProductVariantName(String productVariantName) {
        this.productVariantName = productVariantName;
    }

    public String getProductVariantImage() {
        return productVariantImage;
    }

    public void setProductVariantImage(String productVariantImage) {
        this.productVariantImage = productVariantImage;
    }
}
