package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserAddressDTO implements Serializable {
    private Integer id;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("address_detail")
    private String addressDetail;
    @SerializedName("province_city")
    private String provinceCity;
    @SerializedName("is_default")
    private Boolean isDefault;
    @SerializedName("receiver_name")
    private String receiverName;
    private Double latitude;
    private Double longitude;

    public UserAddressDTO() {
    }

    public UserAddressDTO(Integer id, String phoneNumber,
                          String addressDetail, String provinceCity,
                          Boolean isDefault, String receiverName) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.addressDetail = addressDetail;
        this.provinceCity = provinceCity;
        this.isDefault = isDefault;
        this.receiverName = receiverName;
    }

    public Integer getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
