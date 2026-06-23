package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;

public class JwtFromLoginDTO {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    private String avatar;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("user_id")
    private Integer userId;

    public JwtFromLoginDTO() {
    }

    public JwtFromLoginDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean checkAccessAndRefreshValid() {
        return accessToken != null && !accessToken.isEmpty()
                && refreshToken != null && !refreshToken.isEmpty();
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "JwtFromLoginDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", avatar='" + avatar + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userId=" + userId +
                '}';
    }
}
