package com.infix.phukiencongnghe.data.dto.response.ai;

import com.example.pkcn.dto.response.ai.ProductChatSummaryDTO;

import java.util.List;

public class BotChatResponseDTO {
    String botReply;
    boolean hasProducts;
    List<com.example.pkcn.dto.response.ai.ProductChatSummaryDTO> suggestedProducts;

    public String getBotReply() {
        return botReply;
    }

    public boolean isHasProducts() {
        return hasProducts;
    }

    public List<ProductChatSummaryDTO> getSuggestedProducts() {
        return suggestedProducts;
    }
}