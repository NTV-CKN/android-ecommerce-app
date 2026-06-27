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

    public UserAddressDTO(Integer id, String phoneNumber, String addressDetail, String provinceCity, String receiverName, Double latitude, Double longitude) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.addressDetail = addressDetail;
        this.provinceCity = provinceCity;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "UserAddressDTO{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", provinceCity='" + provinceCity + '\'' +
                ", isDefault=" + isDefault +
                ", receiverName='" + receiverName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
