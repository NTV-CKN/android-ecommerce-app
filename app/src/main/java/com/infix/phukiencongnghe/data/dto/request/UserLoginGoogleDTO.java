package com.infix.phukiencongnghe.data.dto.request;

import com.google.gson.annotations.SerializedName;

public class UserLoginGoogleDTO {
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
    private String email;

    public UserLoginGoogleDTO() {
    }

    public UserLoginGoogleDTO(String fullName, String avatar, String email) {
        this.fullName = fullName;
        this.avatar = avatar;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }
}