package com.infix.phukiencongnghe.data.dto.request;

public class UpdateUserDTO {
    private String fullName;

    public UpdateUserDTO(String fullName) {
        this.fullName = fullName;
    }

    public UpdateUserDTO() {
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
