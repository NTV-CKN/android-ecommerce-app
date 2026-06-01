package com.infix.phukiencongnghe.data.dto.request;

import com.google.gson.annotations.SerializedName;

public class UserRegisterDTO {
    private String email;
    private String password;
    @SerializedName("type_account")
    private String typeAccount;

    public UserRegisterDTO(String email, String password, String typeAccount) {
        this.email = email;
        this.password = password;
        this.typeAccount = typeAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(String typeAccount) {
        this.typeAccount = typeAccount;
    }
}

