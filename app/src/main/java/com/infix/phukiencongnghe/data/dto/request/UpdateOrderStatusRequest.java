package com.infix.phukiencongnghe.data.dto.request;

public class UpdateOrderStatusRequest {

    private String status;

    public UpdateOrderStatusRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}