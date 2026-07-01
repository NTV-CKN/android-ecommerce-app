package com.infix.phukiencongnghe.data.dto.request;

import java.util.List;

public class OrderRequestDTO {
    private Integer addressOrderId;
    private Integer paymentMethodId;
    private String note;
    private List<String> applyVoucher;
    private List<OrderProductItem> products;

    public OrderRequestDTO(Integer addressOrderId, Integer paymentMethodId, String note, List<String> applyVoucher, List<OrderProductItem> products) {
        this.addressOrderId = addressOrderId;
        this.paymentMethodId = paymentMethodId;
        this.note = note;
        this.applyVoucher = applyVoucher;
        this.products = products;
    }

    public static class OrderProductItem {
        private Integer productVariantId;
        private Integer quantity;

        public OrderProductItem(Integer productVariantId, Integer quantity) {
            this.productVariantId = productVariantId;
            this.quantity = quantity;
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

    public Integer getAddressOrderId() {
        return addressOrderId;
    }

    public void setAddressOrderId(Integer addressOrderId) {
        this.addressOrderId = addressOrderId;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public OrderRequestDTO() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getApplyVoucher() {
        return applyVoucher;
    }

    public void setApplyVoucher(List<String> applyVoucher) {
        this.applyVoucher = applyVoucher;
    }

    public List<OrderProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductItem> products) {
        this.products = products;
    }
}