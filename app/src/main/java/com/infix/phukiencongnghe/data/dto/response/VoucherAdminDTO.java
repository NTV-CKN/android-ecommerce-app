package com.infix.phukiencongnghe.data.dto.response;

import com.infix.phukiencongnghe.common.DiscountType;

import java.math.BigDecimal;

public class VoucherAdminDTO {

    private Integer id;
    private String code;
    private String title;
    private DiscountType discountType;
    private double discountValue;
    private double minPriceAllow;
    private String startDate;
    private String endDate;
    private Integer usageLimit;
    private Integer usedCount;
    private Integer status;
    private VoucherTypeDTO voucherType;

    public VoucherAdminDTO() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }
    public double getMinPriceAllow() { return minPriceAllow; }
    public void setMinPriceAllow(double minPriceAllow) { this.minPriceAllow = minPriceAllow; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
    public Integer getUsedCount() { return usedCount; }
    public void setUsedCount(Integer usedCount) { this.usedCount = usedCount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public VoucherTypeDTO getVoucherType() { return voucherType; }
    public void setVoucherType(VoucherTypeDTO voucherType) { this.voucherType = voucherType; }
}

