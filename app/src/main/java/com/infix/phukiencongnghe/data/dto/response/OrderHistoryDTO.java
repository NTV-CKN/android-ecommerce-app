package com.infix.phukiencongnghe.data.dto.response;

import java.io.Serializable;

public class OrderHistoryDTO implements Serializable {
    private Integer id;
    private String orderDate;
    private String deliveryDate;
    private double shippingFee;
    private double totalMustPay;
    private String statusOrder;
    private String note;
    private String receiverName;
    private String addressDetail;
    private String provinceCity;

    public OrderHistoryDTO(Integer id, String orderDate, String deliveryDate, double shippingFee, double totalMustPay, String statusOrder, String note, String receiverName, String addressDetail, String provinceCity) {
        this.id = id;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.shippingFee = shippingFee;
        this.totalMustPay = totalMustPay;
        this.statusOrder = statusOrder;
        this.note = note;
        this.receiverName = receiverName;
        this.addressDetail = addressDetail;
        this.provinceCity = provinceCity;
    }

    public Integer getId() { return id; }
    public String getOrderDate() { return orderDate; }
    public String getStatusOrder() { return statusOrder; }
    public double getTotalMustPay() { return totalMustPay; }
    public String getReceiverName() { return receiverName; }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public void setTotalMustPay(double totalMustPay) {
        this.totalMustPay = totalMustPay;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }
}
