package com.infix.phukiencongnghe.ui.main.ai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infix.phukiencongnghe.databinding.FragmentChatBotBinding;

public class ChatBotFragment extends Fragment {

    private FragmentChatBotBinding binding;
    private ChatBotViewModel chatBotViewModel;
    private ChatAdapter chatAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBotBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initChatBotViewModel();
        initRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        chatBotViewModel.resetStates();
    }

    private void initChatBotViewModel() {

    }

    private void initRecyclerView() {
    }
}