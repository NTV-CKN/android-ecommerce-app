package com.infix.phukiencongnghe.data.dto.request;

import com.google.gson.annotations.SerializedName;

public class RefreshTokenDTO {
    @SerializedName("refresh_token")
    private String refreshToken;

    public RefreshTokenDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
