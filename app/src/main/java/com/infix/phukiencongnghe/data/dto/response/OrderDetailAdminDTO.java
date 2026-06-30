package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderDetailAdminDTO {

    private Integer orderId;

    private String customerName;

    private String customerEmail;

    private Double totalPrice;

    private Double shippingFee;

    private String status;

    private String note;

    private Integer paymentMethodId;

    private String orderDate;

    private List<OrderItemDTO> items;

    @SerializedName("appliedVouchers")
    private List<VoucherDTO> appliedVouchers;

    public Integer getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public List<VoucherDTO> getAppliedVouchers() {
        return appliedVouchers;
    }
}