package com.infix.phukiencongnghe.data.dto.response;

import java.math.BigDecimal;

public class ProductVariantDTO {
    private Integer id;
    private String sku;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String color;
    private String size;
    private Integer gram;
    private String imageUrl;

    public ProductVariantDTO(Integer id, String sku, String name, BigDecimal price, Integer stock, String color, String size, Integer gram, String imageUrl) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.color = color;
        this.size = size;
        this.gram = gram;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getGram() {
        return gram;
    }

    public void setGram(Integer gram) {
        this.gram = gram;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
