package com.infix.phukiencongnghe.common;

public enum StatusOrder {
    PENDING_APPROVE("pending_approve"),
    DELIVERING("delivering"),
    COMPLETED("completed"),
    CANCEL("cancel"),
    APPROVE_BY_ADMIN("approve_by_admin"),
    REJECT_BY_ADMIN("reject_by_admin");

    String value;
     StatusOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
