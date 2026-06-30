package com.infix.phukiencongnghe.data.dto;

import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductAdminPageDTO {
    private Integer id;
    private String folderId;
    private String name;
    private Integer warrantyPeriod;
    private String subtitle;
    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean status;
    private Integer stock;
    private String mainImage;

    private List<ProductVariantDTO> productVariantDTOS;
    private List<String> images;
    private List<CategoryDTO> categoriesDTOS;

    public ProductAdminPageDTO() {
    }

    public ProductAdminPageDTO(Integer id, String folderId, String name,
                               Integer warrantyPeriod, String subtitle,
                               String description, BigDecimal minPrice,
                               BigDecimal maxPrice, Boolean status, Integer stock, String mainImage
    ) {
        this.mainImage =mainImage;
        this.id = id;
        this.folderId = folderId;
        this.name = name;
        this.warrantyPeriod = warrantyPeriod;
        this.subtitle = subtitle;
        this.description = description;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.status = status;
        this.stock = stock;

        this.categoriesDTOS = new ArrayList<>();
        this.productVariantDTOS = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(Integer warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getStock() {
        return stock;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<ProductVariantDTO> getProductVariantDTOS() {
        return productVariantDTOS;
    }

    public void setProductVariantDTOS(List<ProductVariantDTO> productVariantDTOS) {
        this.productVariantDTOS = productVariantDTOS;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<CategoryDTO> getCategoriesDTOS() {
        return categoriesDTOS;
    }

    public void setCategoriesDTOS(List<CategoryDTO> categoriesDTOS) {
        this.categoriesDTOS = categoriesDTOS;
    }
}
