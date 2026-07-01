package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;
import com.infix.phukiencongnghe.common.DiscountType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class VoucherDTO implements Serializable {
    @SerializedName("id")
    private long id;

    @SerializedName("code")
    private String code;

    @SerializedName("title")
    private String title;

    @SerializedName("discountType")
    private DiscountType discountType; // "FIXED_AMOUNT" | "PERCENTAGE"

    @SerializedName("discountValue")
    private double discountValue;

    @SerializedName("minPriceAllow")
    private double minPriceAllow;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("voucherType")
    private VoucherTypeDTO voucherType;

    public long getId() { return id; }
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public DiscountType getDiscountType() { return discountType; }
    public double getDiscountValue() { return discountValue; }
    public double getMinPriceAllow() { return minPriceAllow; }
    public String getEndDate() { return endDate; }
    public VoucherTypeDTO getVoucherType() { return voucherType; }
}
