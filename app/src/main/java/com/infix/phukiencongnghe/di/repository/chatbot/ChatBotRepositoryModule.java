package com.infix.phukiencongnghe.di.repository.chatbot;

import com.infix.phukiencongnghe.data.repository.ai.ChatBotRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.ai.IChatBotRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class ChatBotRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IChatBotRepository bindChatBotRepository(ChatBotRepositoryImpl chatBotRepository);
}
