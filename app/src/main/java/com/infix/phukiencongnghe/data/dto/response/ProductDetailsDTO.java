package com.infix.phukiencongnghe.data.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailsDTO {
    private Integer id;
    private String name;
    private String subtitle;
    private String description;
    private Integer warrantyPeriod;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> images;
    private List<ProductVariantDTO> productVariants;
    private List<ReviewDTO> reviews;
    private List<RelatedProductDTO> relateProducts;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getWarrantyPeriod() { return warrantyPeriod; }
    public void setWarrantyPeriod(Integer warrantyPeriod) { this.warrantyPeriod = warrantyPeriod; }
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<ProductVariantDTO> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariantDTO> productVariants) {
        this.productVariants = productVariants;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public List<RelatedProductDTO> getRelateProducts() {
        return relateProducts;
    }

    public void setRelateProducts(List<RelatedProductDTO> relateProducts) {
        this.relateProducts = relateProducts;
    }
}