package com.infix.phukiencongnghe.data.model;

import com.infix.phukiencongnghe.data.dto.response.ai.ProductChatSummaryDTO;

import java.util.List;

public class MessageModel {
    public static final int TYPE_USER = 1;
    public static final int TYPE_BOT = 2;

    private String text;
    private int type;
    private List<ProductChatSummaryDTO> products;

    public MessageModel(String text, int type, List<ProductChatSummaryDTO> products) {
        this.text = text;
        this.type = type;
        this.products = products;
    }

    public String getText() { return text; }
    public int getType() { return type; }
    public List<ProductChatSummaryDTO> getProducts() { return products; }
}