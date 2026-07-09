package com.infix.phukiencongnghe.data.dto.response.ai;

import java.math.BigDecimal;

public class ProductChatSummaryDTO {
    private Integer id;
    private String name;
    private BigDecimal minPrice;
    private String mainImageUrl;
    private Integer stock;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public Integer getStock() {
        return stock;
    }
}
