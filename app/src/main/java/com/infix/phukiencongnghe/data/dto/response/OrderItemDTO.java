package com.infix.phukiencongnghe.data.dto.response;

public class OrderItemDTO {

    private String productName;
    private Integer quantity;
    private Double price;

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }
}