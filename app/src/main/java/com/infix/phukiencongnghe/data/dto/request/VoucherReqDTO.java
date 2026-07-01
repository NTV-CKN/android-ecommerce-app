package com.infix.phukiencongnghe.data.dto.request;

import com.infix.phukiencongnghe.common.DiscountType;

import java.math.BigDecimal;

public class VoucherReqDTO {
    private String code;
    private String title;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal minPriceAllow;
    private String startDate;
    private String endDate;
    private Integer usageLimit;
    private Integer status;
    private Integer voucherTypeId;

    public VoucherReqDTO() {}

    // Getters and Setters đầy đủ để Mobile mapping dữ liệu
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public BigDecimal getMinPriceAllow() { return minPriceAllow; }
    public void setMinPriceAllow(BigDecimal minPriceAllow) { this.minPriceAllow = minPriceAllow; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getVoucherTypeId() { return voucherTypeId; }
    public void setVoucherTypeId(Integer voucherTypeId) { this.voucherTypeId = voucherTypeId; }
}
