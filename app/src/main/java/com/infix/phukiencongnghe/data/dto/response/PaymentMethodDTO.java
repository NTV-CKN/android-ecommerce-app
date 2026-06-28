package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;

import kotlinx.serialization.Serializable;

public class PaymentMethodDTO {
    private Integer id;
    private String nameMethod;
    @SerializedName("subtitle")
    private String subTitle;

    public PaymentMethodDTO(Integer id, String nameMethod, String subTitle) {
        this.id = id;
        this.nameMethod = nameMethod;
        this.subTitle = subTitle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameMethod() {
        return nameMethod;
    }

    public void setNameMethod(String nameMethod) {
        this.nameMethod = nameMethod;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
