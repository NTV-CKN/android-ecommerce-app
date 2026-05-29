package com.infix.phukiencongnghe.common;

public enum DiscountType {
    FIXED_AMOUNT("FIXED_AMOUNT"),
    PERCENTAGE("PERCENTAGE");
    private String type;

     DiscountType(String type) {
        this.type = type;
    }
}