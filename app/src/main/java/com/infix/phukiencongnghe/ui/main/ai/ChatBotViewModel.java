package com.infix.phukiencongnghe.ui.main.ai;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.infix.phukiencongnghe.data.dto.response.ExceptionResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.ai.BotChatResponseDTO;
import com.infix.phukiencongnghe.data.model.MessageModel;
import com.infix.phukiencongnghe.data.repository.ai.IChatBotRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class ChatBotViewModel extends ViewModel {
    private final IChatBotRepository chatBotRepository;

    private final List<MessageModel> currentMessages = new ArrayList<>();
    private final MutableLiveData<List<MessageModel>> _chatHistory = new MutableLiveData<>();
    public final LiveData<List<MessageModel>> chatHistory = _chatHistory;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    @Inject
    public ChatBotViewModel(IChatBotRepository chatBotRepository) {
        this.chatBotRepository = chatBotRepository;
        _chatHistory.setValue(currentMessages);
    }

    public void sendMessage(String userMessage) {
        if (userMessage.trim().isEmpty()) return;

        currentMessages.add(new MessageModel(userMessage, MessageModel.TYPE_USER, null));
        _chatHistory.setValue(currentMessages);

        _isLoading.setValue(true);

        chatBotRepository.chat(userMessage).enqueue(new Callback<BotChatResponseDTO>() {
            @Override
            public void onResponse(@NonNull Call<BotChatResponseDTO> call, @NonNull Response<BotChatResponseDTO> response) {
                if (response.isSuccessful()) {
                    BotChatResponseDTO body = response.body();
                    if (body != null) {
                        currentMessages.add(new MessageModel(
                                body.getBotReply(),
                                MessageModel.TYPE_BOT,
                                body.getSuggestedProducts()
                        ));
                        _chatHistory.setValue(currentMessages);
                    } else {
                        _notifyMsg.setValue("Dữ liệu trả về trống!");
                    }
                } else {
                    parseError(response.errorBody());
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<BotChatResponseDTO> call, @NonNull Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }

    private void parseError(ResponseBody responseBody) {
        if (responseBody == null) {
            _notifyMsg.setValue("Không thể hiểu lỗi");
            return;
        }
        try {
            Gson gson = new Gson();
            ExceptionResponseDTO exc = gson.fromJson(responseBody.string(), ExceptionResponseDTO.class);
            if (exc != null) {
                _notifyMsg.setValue(exc.getMessage());
            }
        } catch (IOException e) {
            _notifyMsg.setValue(e.getMessage());
        }
    }

    public void resetStates() {
        _notifyMsg.setValue(null);
        _isLoading.setValue(null);
    }
}