package com.infix.phukiencongnghe.data.repository.ai;

import com.infix.phukiencongnghe.data.dto.response.ai.BotChatResponseDTO;
import com.infix.phukiencongnghe.data.source.remote.ai.ChatBotService;

import retrofit2.Call;

public class ChatBotRepositoryImpl implements IChatBotRepository{
    private final ChatBotService chatBotService;

    public ChatBotRepositoryImpl(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @Override
    public Call<BotChatResponseDTO> chat(String userMessage) {
        return chatBotService.chat(userMessage);
    }
}
