package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class FeatureProductDTO {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("minPrice")
    private BigDecimal minPrice;

    @SerializedName("mainImage")
    private String mainImage;

    @SerializedName("avgStar")
    private Double avgStar;

    public Double getAvgStar() {
        return avgStar;
    }

    public Integer getId() {
        return id;
    }

    public String getMainImage() {
        return mainImage;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
