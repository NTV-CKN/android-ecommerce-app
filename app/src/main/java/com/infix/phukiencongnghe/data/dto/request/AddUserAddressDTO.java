package com.infix.phukiencongnghe.data.dto.request;

import com.google.gson.annotations.SerializedName;

public class AddUserAddressDTO {
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("address_detail")
    private String addressDetail;
    private String provinceCity;
    @SerializedName("is_selected")
    private Boolean isSelected;
    @SerializedName("receiver_name")
    private String receiverName;
    private Double latitude;
    private Double longitude;

    public AddUserAddressDTO() {
    }

    public AddUserAddressDTO(String phoneNumber, String addressDetail,
                             String provinceCity, Boolean isSelected,
                             String receiverName, Double latitude, Double longitude) {
        this.phoneNumber = phoneNumber;
        this.addressDetail = addressDetail;
        this.provinceCity = provinceCity;
        this.isSelected = isSelected;
        this.receiverName = receiverName;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Boolean getSelected() {
        return isSelected;
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
