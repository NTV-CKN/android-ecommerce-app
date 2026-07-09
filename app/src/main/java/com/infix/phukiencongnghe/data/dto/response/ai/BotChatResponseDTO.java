package com.infix.phukiencongnghe.data.dto.response.ai;

import java.util.List;

public class BotChatResponseDTO {
    String botReply;
    boolean hasProducts;
    List<ProductChatSummaryDTO> suggestedProducts;

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