package com.infix.phukiencongnghe.data.dto.request;

public class CartLocalDTO {

    private Integer productVariantId;
    private Integer quantity;

    public CartLocalDTO(Integer selectedVariantId, int selectQuantity) {
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
}
