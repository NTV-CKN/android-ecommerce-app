package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;

public class OrderManageDTO {

    @SerializedName("orderId")
    private Integer orderId;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("totalPrice")
    private Double totalPrice;

    @SerializedName("status")
    private String status;

    @SerializedName("orderDate")
    private String orderDate;

    public Integer getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderDate() {
        return orderDate;
    }

}