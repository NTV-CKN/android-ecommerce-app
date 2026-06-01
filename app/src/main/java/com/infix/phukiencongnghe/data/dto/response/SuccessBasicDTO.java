package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;

public class SuccessBasicDTO {
    private String message;
    @SerializedName(value = "is_success")
    private boolean isSuccess;

    public SuccessBasicDTO(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
