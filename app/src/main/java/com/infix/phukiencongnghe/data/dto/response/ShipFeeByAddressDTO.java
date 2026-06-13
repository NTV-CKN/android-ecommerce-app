package com.infix.phukiencongnghe.data.dto.response;

public class ShipFeeByAddressDTO {
    private Integer id;
    private String provinceCity;
    private String type;
    private Double price;
    private Double latitude;
    private Double longitude;

    public Integer getId() {
        return id;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
