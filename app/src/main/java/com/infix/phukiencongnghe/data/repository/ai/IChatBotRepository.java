package com.infix.phukiencongnghe.data.repository.ai;

import com.infix.phukiencongnghe.data.dto.response.ai.BotChatResponseDTO;

import retrofit2.Call;

public interface IChatBotRepository {
    Call<BotChatResponseDTO> chat(String userMessage);
}
