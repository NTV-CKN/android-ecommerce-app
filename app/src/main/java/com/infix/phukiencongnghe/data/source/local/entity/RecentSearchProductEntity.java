package com.infix.phukiencongnghe.data.source.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recent_search_product")
public class RecentSearchProductEntity {

    @PrimaryKey
    private int productId;

    private String productName;

    private String productImage;

    private double price;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    public RecentSearchProductEntity(
            int productId,
            String productName,
            String productImage,
            double price,
            long createdAt
    ) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.price = price;
        this.createdAt = createdAt;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public double getPrice() {
        return price;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}