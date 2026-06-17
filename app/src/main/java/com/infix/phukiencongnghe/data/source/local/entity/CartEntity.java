package com.infix.phukiencongnghe.data.source.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "product_variant_id")
    private int productVariantId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "variant_name")
    private String variantName;

    @ColumnInfo(name = "product_image")
    private String productImage;

    private double unitPrice;

    private int quantity;

    @ColumnInfo(name = "total_price")
    private double totalPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(int productVariantId) {
        this.productVariantId = productVariantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }
}
