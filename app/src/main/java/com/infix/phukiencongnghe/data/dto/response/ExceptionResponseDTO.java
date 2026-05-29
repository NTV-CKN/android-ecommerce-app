package com.infix.phukiencongnghe.data.dto.response;

import java.time.LocalDateTime;

public class ExceptionResponseDTO {
    private int status;
    private String error;
    private String message;

    public ExceptionResponseDTO(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
