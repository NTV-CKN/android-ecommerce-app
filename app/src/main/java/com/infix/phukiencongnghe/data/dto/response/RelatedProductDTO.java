package com.infix.phukiencongnghe.data.dto.response;

import java.math.BigDecimal;

public class RelatedProductDTO {
    private Integer id;
    private String name;
    private BigDecimal minPrice;
    private String mainImage;
    private Double avgStar;

    public RelatedProductDTO() {
    }
    public RelatedProductDTO(Integer id, String name, BigDecimal minPrice, String mainImage, Double avgStar) {
        this.id = id;
        this.name = name;
        this.minPrice = minPrice;
        this.mainImage = mainImage;
        this.avgStar = avgStar;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public String getMainImage() { return mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }

    public Double getAvgStar() { return avgStar; }
    public void setAvgStar(Double avgStar) { this.avgStar = avgStar; }
}
