package com.infix.phukiencongnghe.data.source.remote.ai;

import com.infix.phukiencongnghe.data.dto.response.ai.BotChatResponseDTO;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatBotService {
    @POST("/api/v1/ai/chat")
    Call<BotChatResponseDTO> chat(@Query("userMessage") String userMessage);
}
