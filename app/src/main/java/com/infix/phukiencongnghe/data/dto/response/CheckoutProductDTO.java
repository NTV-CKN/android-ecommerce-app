package com.infix.phukiencongnghe.data.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

public class CheckoutProductDTO implements Serializable {
    private Integer productId;
    private Integer productVariantId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String imgUrl;

    public CheckoutProductDTO(Integer productId, Integer productVariantId, String productName, Integer quantity, BigDecimal price, String imgUrl) {
        this.productId = productId;
        this.productVariantId = productVariantId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(Integer productVariantId) {
        this.productVariantId = productVariantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
