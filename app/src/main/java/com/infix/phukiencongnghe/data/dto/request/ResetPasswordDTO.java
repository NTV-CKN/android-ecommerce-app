package com.infix.phukiencongnghe.data.dto.request;

public class ResetPasswordDTO {
    private String email;
    private String password;
    private String token;

    public ResetPasswordDTO(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
