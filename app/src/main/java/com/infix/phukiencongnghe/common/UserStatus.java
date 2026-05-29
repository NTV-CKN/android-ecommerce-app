package com.infix.phukiencongnghe.common;

public enum UserStatus {
    ACTIVE("ACTIVE"),
    VERIFY_MAIL("VERIFY_MAIL"),
    BANDED("BANDED");

    private String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
