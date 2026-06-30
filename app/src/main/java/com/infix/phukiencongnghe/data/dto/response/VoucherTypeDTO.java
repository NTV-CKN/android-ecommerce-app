package com.infix.phukiencongnghe.data.dto.response;

import com.google.gson.annotations.SerializedName;

public class VoucherTypeDTO {
    @SerializedName("id")
    private int id;
    @SerializedName("code")
    private String code; // "MAIN_ORDER" | "SHIPPING"
    @SerializedName("name")
    private String name;

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
}
