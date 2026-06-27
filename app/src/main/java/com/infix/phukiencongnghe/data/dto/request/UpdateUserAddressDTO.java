package com.infix.phukiencongnghe.data.dto.request;

import com.google.gson.annotations.SerializedName;

public class UpdateUserAddressDTO {
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

    public UpdateUserAddressDTO() {
    }

    public UpdateUserAddressDTO(Integer id, String phoneNumber, String addressDetail,
                                String provinceCity, Boolean isDefault, String receiverName,
                                Double latitude, Double longitude) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.addressDetail = addressDetail;
        this.provinceCity = provinceCity;
        this.isDefault = isDefault;
        this.receiverName = receiverName;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getReceiverName() {
        return receiverName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Boolean getDefault() {
        return isDefault;
    }
}
